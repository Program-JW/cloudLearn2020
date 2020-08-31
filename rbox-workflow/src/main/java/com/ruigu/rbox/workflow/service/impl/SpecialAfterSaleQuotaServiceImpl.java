package com.ruigu.rbox.workflow.service.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.cloud.kanai.web.page.PageImpl;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.feign.handler.RsClientHandler;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.model.dto.*;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.SpecialAfterSaleQuotaRuleTypeEnum;
import com.ruigu.rbox.workflow.model.enums.YesOrNoOrDefaultEnum;
import com.ruigu.rbox.workflow.model.request.PageableRequest;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleQuotaRuleEnableRequest;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleQuotaRuleHistoryRequest;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleQuotaRuleRequest;
import com.ruigu.rbox.workflow.model.vo.SpecialAfterSaleQuotaRuleVO;
import com.ruigu.rbox.workflow.model.vo.UserExtraRelationshipVO;
import com.ruigu.rbox.workflow.repository.*;
import com.ruigu.rbox.workflow.service.SpecialAfterSaleQuotaService;
import com.ruigu.rbox.workflow.supports.ObjectUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liqingtian
 * @date 2020/08/12 19:37
 */
@Service
public class SpecialAfterSaleQuotaServiceImpl implements SpecialAfterSaleQuotaService {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private SpecialAfterSaleQuotaRuleRepository quotaRuleRepository;

    @Autowired
    private SpecialAfterSaleQuotaRuleScopeRepository ruleScopeRepository;

    @Autowired
    private SpecialAfterSaleQuotaRuleHistoryRepository ruleHistoryRepository;

    @Autowired
    private SpecialAfterSaleQuotaRuleScopeHistoryRepository scopeHistoryRepository;

    @Autowired
    private SpecialAfterSaleQuotaTransactionRepository quotaTransactionRepository;
    @Autowired
    private SpecialAfterSaleQuotaRepository specialAfterSaleQuotaRepository;
    @Autowired
    private SpecialAfterSaleQuotaRepository quotaRepository;
    @Autowired
    private RsClientHandler rsClientHandler;
    @Autowired
    private PassportFeignManager passportFeignManager;
    @Value("${rbox.workflow.rs-client-id}")
    private Integer rsClientId;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initQuota(List<RsGroupGmvDataDTO> groupGmvDataDTO) {
        Integer ym = convertDate(LocalDate.now());
        // 第一版是不考虑bdm组的，因此只有城市组，不会出现groupId重复的情况
        Map<Integer, RsGroupGmvDataDTO> groupGmvMap = groupGmvDataDTO.stream().collect(Collectors.toMap(RsGroupGmvDataDTO::getGroupId, g -> g));
        // 首先获取数据库中已有配置
        List<SpecialAfterSaleQuotaEntity> all = specialAfterSaleQuotaRepository.findAll();
        // 再获取修改后的配置进行覆盖
        List<SpecialAfterSaleQuotaEntity> lastUpdateRule = specialAfterSaleQuotaRepository.queryLastUpdatedRule();
        // 根据 groupId 进行 map 化
        Map<Integer, List<SpecialAfterSaleQuotaEntity>> lastUpdateRuleMap = lastUpdateRule.stream().collect(Collectors.groupingBy(SpecialAfterSaleQuotaEntity::getGroupId));

        // 循环所有初始化
        for (SpecialAfterSaleQuotaEntity quota : all) {
            Integer groupId = quota.getGroupId();
            Integer type = quota.getType();
            // 因为每个区域对电销和直销是两套规则
            List<SpecialAfterSaleQuotaEntity> lastUpdate = lastUpdateRuleMap.getOrDefault(groupId, null);
            if (CollectionUtils.isNotEmpty(lastUpdate)) {
                SpecialAfterSaleQuotaEntity rule = lastUpdate.stream().filter(r -> type.equals(r.getType())).findFirst().orElse(null);
                if (Objects.nonNull(rule)) {
                    // 覆盖值
                    ObjectUtil.extendObject(quota, rule, true);
                    // 删除
                    lastUpdateRule.removeIf(r -> r.getGroupId().equals(groupId) && r.getType().equals(type));
                }
            }
            // 年月
            quota.setYM(ym);
            // 额度配置
            RsGroupGmvDataDTO gmvData = groupGmvMap.getOrDefault(groupId, null);
            if (!Objects.nonNull(gmvData)) {
                continue;
            }
            RsGroupGmvDataDTO.GmvInfo gmvInfo = gmvData.getGmvInfo();
            if (!Objects.nonNull(gmvInfo)) {
                continue;
            }
            quota.setCommonGmv(gmvInfo.getCommonGmv());
            quota.setNonCommonGmv(gmvInfo.getNotCommonGmv());
            quota.setQuota(quota.getCommonGmv().multiply(quota.getCommonCoefficient()).add(quota.getNonCommonGmv().multiply(quota.getNonCommonCoefficient())));
        }
        // 初始化剩余的
        for (SpecialAfterSaleQuotaEntity quota : lastUpdateRule) {
            Integer groupId = quota.getGroupId();
            RsGroupGmvDataDTO gmvData = groupGmvMap.getOrDefault(groupId, null);
            if (!Objects.nonNull(gmvData)) {
                continue;
            }
            RsGroupGmvDataDTO.GmvInfo gmvInfo = gmvData.getGmvInfo();
            if (!Objects.nonNull(gmvInfo)) {
                continue;
            }
            // 年月
            quota.setYM(ym);
            quota.setCommonGmv(gmvInfo.getCommonGmv());
            quota.setNonCommonGmv(gmvInfo.getNotCommonGmv());
            quota.setQuota(quota.getCommonGmv().multiply(quota.getCommonCoefficient()).add(quota.getNonCommonGmv().multiply(quota.getNonCommonCoefficient())));
            all.add(quota);
        }
        specialAfterSaleQuotaRepository.saveAll(all);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void undoTransaction(Long applyId) {
        quotaTransactionRepository.updateStatusByApplyId(applyId, YesOrNoOrDefaultEnum.NO.getCode());
    }

    @Override
    public Map<Integer, List<SpecialAfterSaleGroupQuotaDTO>> queryQuotaByUserId(List<Integer> userIds, Integer type) {
        // 第一版不需要考虑bdm组
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }

        // 查出管理的区域 userId - groupInfo
        Map<Integer, List<RsGroupInfoDTO>> userGroupMap = new HashMap<>(8);
        // 去重使用
        Map<Integer, RsGroupInfoDTO> manageGroup = new HashMap<>(16);
        userIds.forEach(id -> {
            List<RsGroupInfoDTO> manageGroupList = getManageGroupInfo(id);
            userGroupMap.put(id, manageGroupList);
            manageGroupList.forEach(g -> manageGroup.putIfAbsent(g.getGroupId(), g));
        });

        // 查询每个区域的额度 groupId - quotaInfo
        Map<Integer, SpecialAfterSaleGroupQuotaDTO> groupQuotaMap = new HashMap<>();
        Integer yearMonth = convertDate(LocalDate.now());
        manageGroup.forEach((id, groupInfo) -> {
            SpecialAfterSaleGroupQuotaDTO quotaInfo = queryGroupQuota(yearMonth, groupInfo, type);
            groupQuotaMap.put(id, quotaInfo);
        });

        //  userId - groupInfo  [ groupInfo.getId()==groupId ]  groupId - quotaInfo

        // userId - groupQuotaInfo
        Map<Integer, List<SpecialAfterSaleGroupQuotaDTO>> userQuotaMap = new HashMap<>(8);
        userGroupMap.forEach((userId, groups) -> {
            List<SpecialAfterSaleGroupQuotaDTO> quotaInfoList = new ArrayList<>();
            groups.forEach(g -> {
                SpecialAfterSaleGroupQuotaDTO quotaInfo = groupQuotaMap.getOrDefault(g.getGroupId(), null);
                if (Objects.nonNull(quotaInfo)) {
                    quotaInfoList.add(quotaInfo);
                }
            });
            if (!quotaInfoList.isEmpty()) {
                userQuotaMap.put(userId, quotaInfoList);
            }
        });
        return userQuotaMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SpecialAfterSaleQuotaTransactionEntity deductionTransaction(Long applyId, Integer userId, Integer quotaId, BigDecimal applyAmount) {
        // 本月初始化额度
        SpecialAfterSaleQuotaEntity quota = quotaRepository.findById(quotaId)
                .orElseThrow(() -> new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "额度查询失败，无法操作"));
        BigDecimal thisMonthInitQuota = quota.getQuota();
        // 流水扣减额度
        BigDecimal deductionQuota = quotaTransactionRepository.queryDeductionQuota(convertDate(LocalDate.now()), quotaId);
        deductionQuota = Objects.isNull(deductionQuota) ? BigDecimal.ZERO : deductionQuota;
        // 剩余额度
        BigDecimal surplusQuota = thisMonthInitQuota.add(deductionQuota);
        // 如果充足
        if (surplusQuota.compareTo(applyAmount) >= 0) {
            return quotaTransactionRepository.save(buildTransaction(applyId, applyAmount, userId, quota));
        }
        return null;
    }

    private SpecialAfterSaleGroupQuotaDTO queryGroupQuota(Integer yearMonth, RsGroupInfoDTO group, Integer type) {

        SpecialAfterSaleGroupQuotaDTO quotaInfo = new SpecialAfterSaleGroupQuotaDTO();

        Integer groupId = group.getGroupId(), groupType = group.getType();
        quotaInfo.setGroupId(groupId);
        quotaInfo.setGroupName(group.getGroupName());

        SpecialAfterSaleQuotaEntity thisMonthQuotaInfo = quotaRepository.findByYMAndGroupTypeAndGroupIdAndType(yearMonth, groupType, groupId, type);
        quotaInfo.setQuotaId(thisMonthQuotaInfo.getId());

        BigDecimal thisMonthInitQuota = thisMonthQuotaInfo.getQuota();
        quotaInfo.setInitialQuota(thisMonthInitQuota);
        BigDecimal deductionQuota = quotaTransactionRepository.queryDeductionQuota(yearMonth, groupType, groupId, type);
        deductionQuota = Objects.isNull(deductionQuota) ? BigDecimal.ZERO : deductionQuota;
        quotaInfo.setSurplusQuota(thisMonthInitQuota.add(deductionQuota));

        return quotaInfo;
    }

    @Override
    public void markSuccessOrFail(Long applyId, Integer status) {
        quotaTransactionRepository.updateStatusByApplyId(applyId, status);
    }

    private List<RsGroupInfoDTO> getManageGroupInfo(Integer userId) {
        UserExtraRelationshipVO extraUserInfo = passportFeignManager.getExtraUserInfoByUserId(userId);
        List<UserExtraRelationshipVO.ExtraInfo> extraInfos = extraUserInfo.getExtraInfos();
        if (CollectionUtils.isEmpty(extraInfos)) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "查询不到rs用户信息");
        }
        UserExtraRelationshipVO.ExtraInfo rsInfo = extraInfos.stream()
                .filter(i -> rsClientId.equals(i.getClientId())).findFirst()
                .orElseThrow(() -> new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "查询不到rs用户信息"));
        Integer rsUserId = Integer.valueOf(rsInfo.getExtraUserId());
        return rsClientHandler.queryManageGroupInfo(rsUserId);
    }

    private SpecialAfterSaleQuotaTransactionEntity buildTransaction(Long applyId, BigDecimal amount, Integer userId, SpecialAfterSaleQuotaEntity quota) {
        SpecialAfterSaleQuotaTransactionEntity transaction = new SpecialAfterSaleQuotaTransactionEntity();
        transaction.setYM(convertDate(LocalDate.now()));
        transaction.setApplyId(applyId);
        transaction.setQuotaId(quota.getId());
        transaction.setGroupType(quota.getGroupType());
        transaction.setGroupId(quota.getGroupId());
        transaction.setAmount(BigDecimal.ZERO.subtract(amount));
        transaction.setType(quota.getType());
        transaction.setCreatedBy(userId);
        transaction.setLastUpdatedBy(userId);
        LocalDateTime now = LocalDateTime.now();
        transaction.setCreatedAt(now);
        transaction.setLastUpdatedAt(now);
        transaction.setStatus(YesOrNoOrDefaultEnum.DEFAULT.getCode());
        return transaction;
    }

    private int convertDate(LocalDate date) {
        return Integer.parseInt(date.getYear() + String.format("%02d", date.getMonthValue()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addQuotaRule(SpecialAfterSaleQuotaRuleRequest req) {
        Integer userId = UserHelper.getUserId();
        LocalDateTime localDateTime = LocalDateTime.now();
        Integer groupType = req.getGroupType();
        Integer type = req.getType();
        // 插入或更新额度规则主表
        SpecialAfterSaleQuotaRuleEntity quotaRuleEntity = new SpecialAfterSaleQuotaRuleEntity();
        quotaRuleEntity.setId(req.getId());
        quotaRuleEntity.setCommonCoefficient(req.getCommonCoefficient());
        quotaRuleEntity.setNonCommonCoefficient(req.getNonCommonCoefficient());
        quotaRuleEntity.setCreatedAt(localDateTime);
        quotaRuleEntity.setLastUpdateAt(localDateTime);
        quotaRuleEntity.setCreatedBy(userId);
        quotaRuleEntity.setLastUpdateBy(userId);
        quotaRuleEntity.setStatus(YesOrNoEnum.YES.getCode());
        Integer ruleId = quotaRuleRepository.save(quotaRuleEntity).getId();
        // 记录额度规则历史
        SpecialAfterSaleQuotaRuleHistoryEntity ruleHistoryEntity = new SpecialAfterSaleQuotaRuleHistoryEntity();
        ruleHistoryEntity.setCommonCoefficient(req.getCommonCoefficient());
        ruleHistoryEntity.setNonCommonCoefficient(req.getNonCommonCoefficient());
        ruleHistoryEntity.setRuleId(ruleId);
        ruleHistoryEntity.setCreatedAt(localDateTime);
        ruleHistoryEntity.setLastUpdateAt(localDateTime);
        ruleHistoryEntity.setCreatedBy(userId);
        ruleHistoryEntity.setLastUpdateBy(userId);
        Integer ruleHistoryId = ruleHistoryRepository.save(ruleHistoryEntity).getId();
        // 插入或更新额度规则详情表及详情历史表
        // 1、先删除额度规则详情表
        ruleScopeRepository.deleteByRuleId(ruleId);
        // 2、重新插入额度规则详情及详情历史表
        List<SpecialAfterSaleQuotaRuleScopeEntity> scopeList = new ArrayList<>(8);
        List<SpecialAfterSaleQuotaRuleScopeHistoryEntity> scopeHistoryList = new ArrayList<>(8);
        for (Integer groupId : req.getGroupIds()) {
            // 额度规则详情
            SpecialAfterSaleQuotaRuleScopeEntity scopeEntity = new SpecialAfterSaleQuotaRuleScopeEntity();
            scopeEntity.setCreatedAt(localDateTime);
            scopeEntity.setLastUpdateAt(localDateTime);
            scopeEntity.setRuleId(ruleId);
            scopeEntity.setGroupType(groupType);
            scopeEntity.setGroupId(groupId);
            scopeEntity.setCreatedBy(userId);
            scopeEntity.setLastUpdateBy(userId);
            scopeEntity.setType(type);
            scopeList.add(scopeEntity);
            // 额度规则详情历史
            SpecialAfterSaleQuotaRuleScopeHistoryEntity scopeHistoryEntity = new SpecialAfterSaleQuotaRuleScopeHistoryEntity();
            scopeHistoryEntity.setHistoryRuleId(ruleHistoryId);
            scopeHistoryEntity.setCreatedAt(localDateTime);
            scopeHistoryEntity.setLastUpdateAt(localDateTime);
            scopeHistoryEntity.setRuleId(ruleId);
            scopeHistoryEntity.setGroupType(groupType);
            scopeHistoryEntity.setGroupId(groupId);
            scopeHistoryEntity.setCreatedBy(userId);
            scopeHistoryEntity.setLastUpdateBy(userId);
            scopeHistoryEntity.setType(type);
            scopeHistoryList.add(scopeHistoryEntity);
        }
        ;
        ruleScopeRepository.saveAll(scopeList);
        scopeHistoryRepository.saveAll(scopeHistoryList);
    }

    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void enableQuotaRule(SpecialAfterSaleQuotaRuleEnableRequest req) {
        Integer userId = UserHelper.getUserId();
        QSpecialAfterSaleQuotaRuleEntity ruleEntity = QSpecialAfterSaleQuotaRuleEntity.specialAfterSaleQuotaRuleEntity;
        queryFactory.update(ruleEntity)
                .set(ruleEntity.status, req.getEnable())
                .set(ruleEntity.lastUpdateAt, LocalDateTime.now())
                .set(ruleEntity.lastUpdateBy, userId)
                .where(ruleEntity.id.eq(req.getRuleId()))
                .execute();
    }

    /**
     * @param req
     * @return
     */
    @Override
    public PageImpl<SpecialAfterSaleQuotaRuleVO> queryAllQuotaRule(PageableRequest req) {
        Integer page = req.getPageIndex();
        Integer limit = req.getPageSize();
        QSpecialAfterSaleQuotaRuleEntity ruleEntity = QSpecialAfterSaleQuotaRuleEntity.specialAfterSaleQuotaRuleEntity;
        QSpecialAfterSaleQuotaRuleScopeEntity ruleScopeEntity = QSpecialAfterSaleQuotaRuleScopeEntity.specialAfterSaleQuotaRuleScopeEntity;
        // 先按额度id分页查询额度规则历史表的id
        QueryResults<Integer> results = queryFactory.select(ruleEntity.id)
                .from(ruleEntity)
                .offset(page * limit)
                .limit(limit)
                .orderBy(ruleEntity.id.desc())
                .fetchResults();
        // 关联查询额度规则历史信息
        List<SpecialAfterSaleQuotaRuleDTO> quotaRuleList = queryFactory.
                select(
                        Projections.bean(SpecialAfterSaleQuotaRuleDTO.class,
                                ruleEntity.id,
                                // TODO 待添加名称
                                ruleEntity.commonCoefficient,
                                ruleEntity.nonCommonCoefficient,
                                ruleScopeEntity.groupType,
                                ruleScopeEntity.groupId,
                                ruleScopeEntity.type)).
                from(ruleEntity).
                join(ruleScopeEntity).
                on(ruleEntity.id.eq(ruleScopeEntity.ruleId)).
                where(ruleScopeEntity.ruleId.in(results.getResults())).
                fetch();
        return PageImpl.of(getSpecialAfterSaleQuotaRuleRequestList(quotaRuleList, SpecialAfterSaleQuotaRuleTypeEnum.QUOTA_RULE_TYPE.getState()), PageRequest.of(page, limit), (int) results.getTotal());
    }

    @Override
    public PageImpl<SpecialAfterSaleQuotaRuleVO> queryQuotaRuleHistory(SpecialAfterSaleQuotaRuleHistoryRequest req) {
        Integer page = req.getPageIndex();
        Integer limit = req.getPageSize();
        QSpecialAfterSaleQuotaRuleHistoryEntity ruleHistoryEntity = QSpecialAfterSaleQuotaRuleHistoryEntity.specialAfterSaleQuotaRuleHistoryEntity;
        QSpecialAfterSaleQuotaRuleScopeHistoryEntity scopeHistoryEntity = QSpecialAfterSaleQuotaRuleScopeHistoryEntity.specialAfterSaleQuotaRuleScopeHistoryEntity;
        // 先分页查询额度规则表的id
        QueryResults<Integer> results = queryFactory.select(ruleHistoryEntity.id)
                .from(ruleHistoryEntity)
                .where(ruleHistoryEntity.ruleId.eq(req.getRuleId()))
                .offset(page * limit)
                .limit(limit)
                .orderBy(ruleHistoryEntity.id.desc())
                .fetchResults();
        // 关联查询额度规则历史信息
        List<SpecialAfterSaleQuotaRuleDTO> quotaRuleHistoryList = queryFactory.
                select(
                        Projections.bean(SpecialAfterSaleQuotaRuleDTO.class,
                                ruleHistoryEntity.ruleId.as("id"),
                                ruleHistoryEntity.id.as("ruleHistoryId"),
                                // TODO 待添加名称
                                ruleHistoryEntity.commonCoefficient,
                                ruleHistoryEntity.nonCommonCoefficient,
                                scopeHistoryEntity.groupType,
                                scopeHistoryEntity.groupId,
                                scopeHistoryEntity.type)).
                from(ruleHistoryEntity).
                join(scopeHistoryEntity).
                on(ruleHistoryEntity.id.eq(scopeHistoryEntity.historyRuleId)).
                where(scopeHistoryEntity.historyRuleId.in(results.getResults())).
                fetch();
        return PageImpl.of(getSpecialAfterSaleQuotaRuleRequestList(quotaRuleHistoryList, SpecialAfterSaleQuotaRuleTypeEnum.HISTORY_QUOTA_RULE_TYPE.getState()), PageRequest.of(page, limit), (int) results.getTotal());

    }

    /**
     * @param quotaRuleHistoryList
     * @param type                 类型(1-额度规则，2-额度规则历史)
     * @return
     */
    private List<SpecialAfterSaleQuotaRuleVO> getSpecialAfterSaleQuotaRuleRequestList(List<SpecialAfterSaleQuotaRuleDTO> quotaRuleHistoryList, Integer type) {
        Map<Integer, List<SpecialAfterSaleQuotaRuleDTO>> ruleHistoryMap = new HashMap<>(8);
        if (type == SpecialAfterSaleQuotaRuleTypeEnum.QUOTA_RULE_TYPE.getState()) {
            ruleHistoryMap = quotaRuleHistoryList.stream().collect(Collectors.groupingBy(SpecialAfterSaleQuotaRuleDTO::getId));
        } else if (type == SpecialAfterSaleQuotaRuleTypeEnum.HISTORY_QUOTA_RULE_TYPE.getState()) {
            ruleHistoryMap = quotaRuleHistoryList.stream().collect(Collectors.groupingBy(SpecialAfterSaleQuotaRuleDTO::getRuleHistoryId));
        }
        List<RsAreaDTO> areasTree = rsClientHandler.getCities();
        // 将树结构转换为数组
        List<RsAreaDTO> areasList = transformTree2List(areasTree);
        Map<Integer, String> groupMap = areasList.stream().collect(Collectors.toMap(RsAreaDTO::getId, RsAreaDTO::getName));
        List<SpecialAfterSaleQuotaRuleVO> ruleRequestList = new ArrayList<>(8);
        for (Map.Entry<Integer, List<SpecialAfterSaleQuotaRuleDTO>> entry : ruleHistoryMap.entrySet()) {
            SpecialAfterSaleQuotaRuleVO quotaRuleVO = new SpecialAfterSaleQuotaRuleVO();
            SpecialAfterSaleQuotaRuleDTO ruleDTO = entry.getValue().get(0);
            List<Integer> groupIds = entry.getValue().stream().map(SpecialAfterSaleQuotaRuleDTO::getGroupId).collect(Collectors.toList());
            List<String> groupNames = groupIds.stream().map(groupId -> groupMap.get(groupId)).collect(Collectors.toList());
            quotaRuleVO.setId(entry.getKey());
            quotaRuleVO.setCommonCoefficient(ruleDTO.getCommonCoefficient());
            quotaRuleVO.setNonCommonCoefficient(ruleDTO.getNonCommonCoefficient());
            quotaRuleVO.setType(ruleDTO.getType());
            quotaRuleVO.setGroupType(ruleDTO.getGroupType());
            quotaRuleVO.setGroupIds(groupIds);
            quotaRuleVO.setGroupNames(groupNames);
            ruleRequestList.add(quotaRuleVO);
        }
        return ruleRequestList;
    }

    private static List<RsAreaDTO> transformTree2List(List<RsAreaDTO> areasTree) {
        List<RsAreaDTO> areasList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(areasTree)) {
            for (RsAreaDTO var1 : areasTree) {
                areasList.add(var1);
                if (!CollectionUtils.isEmpty(transformTree2List(var1.getChild()))) {
                    for (RsAreaDTO var2 : transformTree2List(var1.getChild())) {
                        areasList.add(var2);
                    }
                }
            }
        }
        return areasList;
    }

}

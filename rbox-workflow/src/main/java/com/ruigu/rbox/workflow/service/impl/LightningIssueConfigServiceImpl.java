package com.ruigu.rbox.workflow.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.manager.LightningIssueConfigManager;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.model.dto.*;
import com.ruigu.rbox.workflow.model.request.DutyUserRequest;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.DutyRuleTypeEnum;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.request.*;
import com.ruigu.rbox.workflow.model.vo.*;
import com.ruigu.rbox.workflow.repository.DutyPlanRepository;
import com.ruigu.rbox.workflow.repository.DutyRuleRepository;
import com.ruigu.rbox.workflow.repository.DutyWeekPlanRepository;
import com.ruigu.rbox.workflow.repository.LightningIssueCategoryRepository;
import com.ruigu.rbox.workflow.service.DistributedLocker;
import com.ruigu.rbox.workflow.service.LightningIssueConfigService;
import com.ruigu.rbox.workflow.strategy.context.DutyConfigHandleContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author liqingtian
 * @date 2020/05/07 17:39
 */
@Slf4j
@Service
public class LightningIssueConfigServiceImpl implements LightningIssueConfigService {

    @Resource
    private LightningIssueConfigManager lightningIssueConfigManager;

    @Resource
    private PassportFeignManager passportFeignManager;

    @Resource
    private LightningIssueCategoryRepository lightningIssueCategoryRepository;

    @Resource
    private DutyRuleRepository dutyRuleRepository;

    @Resource
    private DutyPlanRepository dutyPlanRepository;

    @Resource
    private DutyWeekPlanRepository dutyWeekPlanRepository;

    @Resource
    private JPAQueryFactory queryFactory;

    @Resource
    private DistributedLocker distributedLocker;

    @Resource
    private DutyConfigHandleContext dutyConfigHandleContext;

    @Override
    public List<LightningCategoryConfigVO> selectIssueCategory(String categoryName) {

        // 返回结果
        List<LightningCategoryConfigVO> resultList = new ArrayList<>();
        // 分类和规则
        BooleanBuilder builder = new BooleanBuilder();
        QLightningIssueCategoryEntity qCategory = QLightningIssueCategoryEntity.lightningIssueCategoryEntity;
        if (!StringUtils.isBlank(categoryName)) {
            builder.and(qCategory.name.like("%" + categoryName + "%"));
        }
        QDutyRuleEntity qRule = QDutyRuleEntity.dutyRuleEntity;
        List<Tuple> data = queryFactory
                .select(qCategory.id, qCategory.name, qCategory.userId, qCategory.userName, qCategory.ruleId, qRule.name, qCategory.sort, qCategory.status)
                .from(qCategory).leftJoin(qRule).on(qCategory.ruleId.eq(qRule.id))
                .where(builder).fetch();
        if (CollectionUtils.isEmpty(data)) {
            return resultList;
        }
        // 组装结果
        data.forEach(d -> {
            LightningCategoryConfigVO categoryConfig = new LightningCategoryConfigVO();
            categoryConfig.setCategoryId(d.get(qCategory.id));
            categoryConfig.setCategoryName(d.get(qCategory.name));
            categoryConfig.setUserId(d.get(qCategory.userId));
            categoryConfig.setUserName(d.get(qCategory.userName));
            categoryConfig.setRuleId(d.get(qCategory.ruleId));
            categoryConfig.setRuleName(d.get(qRule.name));
            categoryConfig.setSort(d.get(qCategory.sort));
            categoryConfig.setStatus(d.get(qCategory.status));
            resultList.add(categoryConfig);
        });
        return resultList;
    }

    @Override
    public Integer saveIssueCategory(LightningCategoryRequest request) {
        LightningIssueCategoryEntity categoryEntity = convertEntity(request);
        categoryEntity.setStatus(YesOrNoEnum.NO.getCode());
        // 基础信息
        Integer operator = UserHelper.getUserId();
        categoryEntity.setCreatedBy(operator);
        categoryEntity.setLastUpdatedBy(operator);
        LocalDateTime now = LocalDateTime.now();
        categoryEntity.setCreatedOn(now);
        categoryEntity.setLastUpdatedOn(now);
        try {
            LightningIssueCategoryEntity save = lightningIssueCategoryRepository.save(categoryEntity);
            return save.getId();
        } catch (DataIntegrityViolationException e) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "该问题分类名称已存在");
        }
    }

    @Override
    public void updateIssueCategory(LightningCategoryRequest request) {
        Integer categoryId = request.getCategoryId();
        if (categoryId == null) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "所要修改的问题分类id不能为空");
        }
        LightningIssueCategoryEntity oldCategoryEntity = lightningIssueCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "所有修改问题分类不存在"));

        Integer oldRuleId = oldCategoryEntity.getRuleId();

        // 组装实体类
        LightningIssueCategoryEntity categoryEntity = convertEntity(request);
        categoryEntity.setId(categoryId);
        categoryEntity.setStatus(oldCategoryEntity.getStatus());
        // 基础信息
        categoryEntity.setCreatedBy(oldCategoryEntity.getCreatedBy());
        categoryEntity.setCreatedOn(oldCategoryEntity.getCreatedOn());
        categoryEntity.setLastUpdatedBy(UserHelper.getUserId());
        categoryEntity.setLastUpdatedOn(LocalDateTime.now());

        try {

            LightningIssueCategoryEntity save = lightningIssueCategoryRepository.save(categoryEntity);

            if (oldRuleId != null) {
                dutyRuleRepository.findById(oldRuleId).ifPresent(r -> {
                    dutyConfigHandleContext.removeCache(categoryId, r.getType());
                });
            }

            Integer newRuleId = save.getRuleId();
            if (newRuleId != null && !newRuleId.equals(oldRuleId)) {
                // 更新
                dutyRuleRepository.findById(newRuleId).ifPresent(r -> {
                    dutyConfigHandleContext.updateCache(categoryId, r);
                });
            }
        } catch (DuplicateKeyException e) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "问题分类名称不允许重复");
        }
    }

    @Override
    public void categoryOnOff(OnOffRequest request) {
        Integer categoryId = request.getId();
        Integer status = request.getStatus();
        boolean on = YesOrNoEnum.YES.getCode() == status;
        LightningIssueCategoryEntity category = lightningIssueCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "数据异常，查询不到该分类信息"));
        Integer ruleId = category.getRuleId();
        if (ruleId != null) {
            DutyRuleEntity rule = dutyRuleRepository.findById(ruleId)
                    .orElseThrow(() -> new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "数据异常，查询不到值班策略信息"));
            dutyConfigHandleContext.categoryOnOff(categoryId, rule, on);
        } else {
            // 增加一步校验
            if (category.getUserId() == null) {
                throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "数据异常，该问题分类配置数据异常");
            }
        }
        // 更改分类状态
        lightningIssueCategoryRepository.updateStatus(categoryId, status);
    }

    @Override
    public List<LightningDutyRuleSelectVO> selectDutyRuleList(String ruleName) {
        List<LightningDutyRuleSelectVO> resultList = new ArrayList<>();
        // 动态条件
        if (StringUtils.isBlank(ruleName)) {
            ruleName = null;
        } else {
            ruleName = "%" + ruleName + "%";
        }
        // 查询所有部门信息
        List<PassportGroupInfoDTO> allGroupInfo = passportFeignManager.getAllGroupInfo();
        Map<Integer, List<PassportGroupInfoDTO>> groupInfoMap = allGroupInfo.parallelStream()
                .collect(Collectors.groupingBy(PassportGroupInfoDTO::getId));
        // 查询所有策略
        List<DutyRuleEntity> ruleList = dutyRuleRepository.findAllByRuleNameLike(ruleName);
        for (DutyRuleEntity e : ruleList) {
            LightningDutyRuleSelectVO rule = new LightningDutyRuleSelectVO();
            Integer ruleId = e.getId();
            rule.setRuleId(ruleId);
            rule.setRuleName(e.getName());
            // 部门信息
            Integer departmentId = e.getDepartmentId();
            rule.setDepartmentId(departmentId);
            List<PassportGroupInfoDTO> groupInfo = groupInfoMap.get(departmentId);
            if (CollectionUtils.isNotEmpty(groupInfo)) {
                rule.setDepartmentName(groupInfo.get(0).getDescription());
            }
            rule.setScopeType(e.getScopeType());
            rule.setPreDefined(e.getIsPreDefined());
            rule.setStatus(e.getStatus());
            // 策略类型
            Integer type = e.getType();
            rule.setType(type);
            // todo 策略
            rule.setDutyUser(dutyConfigHandleContext.queryDutyUser(e, 0, 10));

            resultList.add(rule);
        }
        return resultList;
    }

    @Override
    public Page<DutyUserByDayDTO> selectDutyUserByRuleId(Integer ruleId, Integer page, Integer size) {
        return lightningIssueConfigManager.queryPageDutyUserByDay(ruleId, page, size);
    }

    @Override
    public LightningDutyRuleDetailVO getDutyRuleDetail(Integer ruleId) {
        // 基础数据
        DutyRuleEntity dutyRuleEntity = dutyRuleRepository.findById(ruleId)
                .orElseThrow(() -> new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "查询不到该策略详情信息"));
        // 拼装
        LightningDutyRuleDetailVO detailVO = new LightningDutyRuleDetailVO();
        detailVO.setRuleId(dutyRuleEntity.getId());
        detailVO.setRuleName(dutyRuleEntity.getName());
        // 设置部门
        Integer departmentId = dutyRuleEntity.getDepartmentId();
        PassportGroupInfoDTO groupInfo = passportFeignManager.getGroupInfoById(departmentId);
        detailVO.setDepartmentId(departmentId);
        detailVO.setDepartmentName(groupInfo.getDescription());
        detailVO.setScopeType(dutyRuleEntity.getScopeType());
        detailVO.setPreDefined(dutyRuleEntity.getIsPreDefined());
        detailVO.setStatus(dutyRuleEntity.getStatus());
        // 分析类型
        Integer type = dutyRuleEntity.getType();
        detailVO.setType(type);
        // todo 策略
        detailVO.setDutyUser(dutyConfigHandleContext.queryDutyUser(dutyRuleEntity));

        return detailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addDutyRule(AddDutyRuleRequest request) {
        DutyRuleEntity rule = new DutyRuleEntity();
        rule.setName(request.getRuleName());
        rule.setDepartmentId(request.getDepartmentId());
        rule.setIsPreDefined(YesOrNoEnum.NO.getCode());
        rule.setScopeType(YesOrNoEnum.NO.getCode());
        // 策略类型
        Integer type = request.getType();
        rule.setType(type);
        // 默认状态：禁用
        rule.setStatus(YesOrNoEnum.NO.getCode());
        // 基础信息
        Integer operator = UserHelper.getUserId();
        rule.setCreatedBy(operator);
        rule.setLastUpdatedBy(operator);
        LocalDateTime now = LocalDateTime.now();
        rule.setCreatedOn(now);
        rule.setLastUpdatedOn(now);
        try {
            dutyRuleRepository.save(rule);
        } catch (DataIntegrityViolationException e) {
            throw new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "该策略名已存在");
        }
        // 保存值班数据
        DutyUserRequest dutyUserRequest = request.getDutyUser();
        if (dutyUserRequest != null) {
            // todo 策略
            dutyConfigHandleContext.save(rule, dutyUserRequest);
        }

        return rule.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDutyRule(UpdateDutyRuleRequest request) {
        // 首先查询出就的数据
        Integer ruleId = request.getRuleId();
        DutyRuleEntity oldRule = dutyRuleRepository.findById(ruleId)
                .orElseThrow(() -> new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "策略信息查询失败"));
        // 更新前策略类型
        Integer oldType = oldRule.getType();
        Integer newType = request.getType();
        // 是否更改策略类型 （如果更改需要清除）
        if (!oldType.equals(newType)) {
            // 修改逻辑 不允许修改类型
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "不可修改策略类型");
        } else {
            // todo 策略
            dutyConfigHandleContext.update(oldRule, request.getDutyUser());
        }

        // 更新数据
        oldRule.setName(request.getRuleName());
        oldRule.setDepartmentId(request.getDepartmentId());
        oldRule.setType(newType);
        oldRule.setLastUpdatedBy(UserHelper.getUserId());
        oldRule.setLastUpdatedOn(LocalDateTime.now());
        dutyRuleRepository.save(oldRule);
    }

    @Override
    public List<DutyRuleEntity> selectDutyRuleDropBoxList() {
        return dutyRuleRepository.findAllByStatus(YesOrNoEnum.YES.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ruleOnOff(OnOffRequest request) {
        Integer ruleId = request.getId();
        Integer status = request.getStatus();
        // 查询涉及到该策略的分类
        List<LightningIssueCategoryEntity> allCategory = lightningIssueCategoryRepository.findAllByRuleIdAndStatus(ruleId, YesOrNoEnum.YES.getCode());
        boolean on = status == YesOrNoEnum.YES.getCode();
        if (!on && CollectionUtils.isNotEmpty(allCategory)) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "该策略正在使用中，不可关闭，请先关闭使用该策略的问题分类，再关闭该策略");
        }
        // 开启也是有校验的
        if (on) {
            DutyRuleEntity rule = dutyRuleRepository.findByIdAndStatus(ruleId, YesOrNoEnum.NO.getCode());
            if (rule == null) {
                throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "启用失败，数据异常，该策略数据丢失或该策略已启用");
            }
            // todo 策略
            dutyConfigHandleContext.onRule(rule);
        }
    }

    private LightningIssueCategoryEntity convertEntity(LightningCategoryRequest request) {
        // 新建分类 （必须选择值班人 or 值班策略）
        Integer userId = request.getUserId();
        Integer ruleId = request.getRuleId();
        boolean userIdIsEmpty;
        boolean ruleIdIsEmpty;

        if ((userIdIsEmpty = userId == null) & (ruleIdIsEmpty = ruleId == null)) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "受理人或者值班策略必须选择一项");
        }

        if (!userIdIsEmpty && !ruleIdIsEmpty) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "受理人或者值班策略只能选择其中一项");
        }

        // 校验拼装
        LightningIssueCategoryEntity categoryEntity = new LightningIssueCategoryEntity();
        // 名称不能重复 （数据库唯一索引）
        categoryEntity.setName(request.getCategoryName());
        categoryEntity.setSort(request.getSort());
        // 值班人
        if (!userIdIsEmpty) {
            String userName;
            if (userId == 0) {
                userName = "全部";
            } else {
                userName = passportFeignManager.getUserInfoFromRedis(userId).getNickname();
            }
            categoryEntity.setUserId(userId);
            categoryEntity.setUserName(userName);
        }

        // 规则id
        if (!ruleIdIsEmpty) {
            DutyRuleEntity rule = dutyRuleRepository.findByIdAndStatus(ruleId, YesOrNoEnum.YES.getCode());
            if (rule == null) {
                throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "不可选用未启用的策略");
            }
            categoryEntity.setRuleId(ruleId);
        }

        return categoryEntity;
    }

    @Override
    public LightningIssueCategoryVO distributionDutyUser(Integer categoryId) {
        Integer userId = lightningIssueConfigManager.distributionDuty(categoryId);
        if (null == userId) {
            // 如果为空 （则考虑缓存丢失，查询数据库数据，并且因为初始化数据可能导致轮询失效，考虑加锁)
            // 因为线上多实例 因此用分布式锁
            String lock = "temp:initDutyUser:lock:" + categoryId;
            try {
                // 加锁
                distributedLocker.lock(lock, TimeUnit.SECONDS, 5);
                // 双重检验机制 （防止多次更新）
                userId = lightningIssueConfigManager.distributionDuty(categoryId);
                if (null == userId) {

                    DutyRuleEntity plan = dutyRuleRepository.findByCategoryId(categoryId);

                    List<Integer> dutyUserIds = new ArrayList<>();

                    if (plan == null) {
                        throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), "查询不到该分类问题值班人");
                    } else if (DutyRuleTypeEnum.DUTY_POLL.getCode().equals(plan.getType())) {
                        if (StringUtils.isBlank(plan.getUserIds())) {
                            throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), "查询不到该分类问题值班人");
                        }
                        dutyUserIds = JsonUtil.parseArray(plan.getUserIds(), Integer.class);
                        if (CollectionUtils.isEmpty(dutyUserIds)) {
                            throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), "查询不到该分类问题值班人");
                        }
                        lightningIssueConfigManager.initDutyPollUser(categoryId, dutyUserIds);
                    } else if (DutyRuleTypeEnum.DUTY_BY_WEEK.getCode().equals(plan.getType())) {
                        // 查询周的
                        DutyWeekPlanEntity dutyWeekUser = dutyWeekPlanRepository.findByRuleIdAndDayOfWeek(plan.getId(),
                                LocalDateTime.now().getDayOfWeek().getValue());
                        dutyUserIds = JsonUtil.parseArray(dutyWeekUser.getUserIds(), Integer.class);
                        if (CollectionUtils.isEmpty(dutyUserIds)) {
                            throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), "查询不到该分类问题值班人");
                        }
                        lightningIssueConfigManager.initDutyWeekUser(categoryId, dutyUserIds);
                    }
                    log.info(" =============================  数据库初始化后，获取值班人  ============================ ");
                    userId = lightningIssueConfigManager.distributionDuty(categoryId);
                }
            } finally {
                // 解锁
                distributedLocker.unlock(lock);
            }
        }
        PassportUserInfoDTO userInfoFromRedis = passportFeignManager.getUserInfoFromRedis(userId);
        LightningIssueCategoryVO categoryVO = new LightningIssueCategoryVO();
        categoryVO.setId(categoryId);
        categoryVO.setUserId(userInfoFromRedis.getId());
        categoryVO.setUserName(userInfoFromRedis.getNickname());
        categoryVO.setAvatar(userInfoFromRedis.getAvatar());
        return categoryVO;
    }

    @Override
    public void updateRedisConfig(UpdateRedisDutyConfigRequest request) {
        Integer categoryId = request.getCategoryId();

        if (DutyRuleTypeEnum.DUTY_BY_DAY.getCode().equals(request.getType())) {
            lightningIssueConfigManager.replaceDutyByDayUser(categoryId, request.getUserId());
        } else if (DutyRuleTypeEnum.DUTY_POLL.getCode().equals(request.getType())) {
            List<Integer> userIds = request.getUserIds();
            if (CollectionUtils.isNotEmpty(userIds)) {
                lightningIssueConfigManager.removeAndUpdateDutyPollUser(categoryId, request.getUserIds());
            }
        } else if (DutyRuleTypeEnum.DUTY_BY_WEEK.getCode().equals(request.getType())) {
            List<Integer> userIds = request.getUserIds();
            if (CollectionUtils.isNotEmpty(userIds)) {
                lightningIssueConfigManager.removeAndUpdateDutyWeekUser(categoryId, request.getUserIds());
            }
        }
    }

    @Override
    public List<Integer> queryRedisDutyUser(Integer categoryId) {
        DutyRuleEntity rule = dutyRuleRepository.findByCategoryId(categoryId);
        if (rule == null) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "策略查询失败");
        }
        // todo 策略
        return dutyConfigHandleContext.queryTodayDutyUser(categoryId, rule.getType());
    }
}

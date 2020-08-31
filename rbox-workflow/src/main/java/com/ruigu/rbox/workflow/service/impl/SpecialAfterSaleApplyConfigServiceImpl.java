package com.ruigu.rbox.workflow.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.manager.UserManager;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.dto.SpecialAfterSaleApprovalRulesDTO;
import com.ruigu.rbox.workflow.model.dto.UserGroupSimpleDTO;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.SpecialAfterSaleApproverTypeEnum;
import com.ruigu.rbox.workflow.model.enums.Symbol;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleApplyConfigRequest;
import com.ruigu.rbox.workflow.repository.SpecialAfterSaleReviewNodeRepository;
import com.ruigu.rbox.workflow.repository.SpecialAfterSaleReviewPositionRepository;
import com.ruigu.rbox.workflow.repository.SpecialAfterSaleReviewRepository;
import com.ruigu.rbox.workflow.repository.SpecialAfterSaleReviewReposity;
import com.ruigu.rbox.workflow.service.SpecialAfterSaleApplyConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/11 17:07
 */

@Service
public class SpecialAfterSaleApplyConfigServiceImpl implements SpecialAfterSaleApplyConfigService {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private SpecialAfterSaleReviewNodeRepository reviewNodeRepository;

    @Autowired
    private SpecialAfterSaleReviewPositionRepository reviewPositionRepository;

    @Autowired
    private SpecialAfterSaleReviewRepository reviewRepository;

    @Autowired
    private SpecialAfterSaleReviewNodeRepository nodeReposity;

    @Autowired
    private SpecialAfterSaleReviewReposity reviewReposity;


    @Autowired
    private SpecialAfterSaleReviewNodeRepository nodeRepository;

    @Autowired
    private SpecialAfterSaleReviewPositionRepository positionRepository;

    @Autowired
    private PassportFeignManager passportFeignManager;

    @Autowired
    private UserManager userManager;



    /**
     * 保存/更新审批配置
     *
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateApproverConfig(SpecialAfterSaleApplyConfigRequest req) {
        Integer configId = req.getConfigId();
        if (configId == null) {
            // 新建审批规则
            this.addApproverConfig(req);
        } else {
            // 删除审批规则
            this.updateApproverConfig(req);
        }

    }

    /**
     * 更新审批规则
     *
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateApproverConfig(SpecialAfterSaleApplyConfigRequest req) {
        // 先删除原有的审批规则
        this.deleteConfigById(req.getConfigId());
        // 重新插入新的审批规则
        this.addApproverConfig(req);
    }

    /**
     * 删除审批规则
     *
     * @param configId 申请id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteConfigById(Integer configId) {
        QSpecialAfterSaleReviewEntity reviewEntity = QSpecialAfterSaleReviewEntity.specialAfterSaleReviewEntity;
        QSpecialAfterSaleReviewNodeEntity nodeEntity = QSpecialAfterSaleReviewNodeEntity.specialAfterSaleReviewNodeEntity;
        QSpecialAfterSaleReviewPositionEntity positionEntity = QSpecialAfterSaleReviewPositionEntity.specialAfterSaleReviewPositionEntity;
        queryFactory.delete(reviewEntity)
                .where(reviewEntity.id.eq(configId))
                .execute();
        queryFactory.delete(nodeEntity)
                .where(nodeEntity.configId.eq(configId))
                .execute();
        queryFactory.delete(positionEntity)
                .where(positionEntity.configId.eq(configId))
                .execute();
    }

    /**
     * 新增审批规则
     *
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void addApproverConfig(SpecialAfterSaleApplyConfigRequest req) {
        Integer userId = UserHelper.getUserId();
        LocalDateTime localDateTime = LocalDateTime.now();
        SpecialAfterSaleReviewEntity reviewEntity = new SpecialAfterSaleReviewEntity();
        // 插入规则配置主表
        reviewEntity.setName(req.getConfigName());
        reviewEntity.setCreatedBy(userId);
        reviewEntity.setCreatedAt(localDateTime);
        reviewEntity.setLastUpdateAt(localDateTime);
        reviewEntity.setLastUpdateBy(userId);
        reviewEntity.setStatus(YesOrNoEnum.YES.getCode());
        reviewEntity.setCcIds(StringUtils.join(req.getCcList(), Symbol.COMMA.getValue()));
        reviewEntity.setDescription(req.getDescription());
        reviewEntity.setGroupId(req.getGroupId());
        Integer reviewId = reviewRepository.save(reviewEntity).getId();
        // 插入规则配置审批人列表
        List<SpecialAfterSaleReviewNodeEntity> nodeEntityList = new ArrayList<>(8);
        req.getApproverList().forEach(approver -> {
            SpecialAfterSaleReviewNodeEntity reviewNodeEntity = new SpecialAfterSaleReviewNodeEntity();
            reviewNodeEntity.setConfigId(reviewId);
            reviewNodeEntity.setCreatedAt(localDateTime);
            reviewNodeEntity.setCreatedBy(userId);
            reviewNodeEntity.setLastUpdateAt(localDateTime);
            reviewNodeEntity.setLastUpdateBy(userId);
            reviewNodeEntity.setFlag(approver.getFlag());
            reviewNodeEntity.setSort(approver.getSort());
            reviewNodeEntity.setUseQuota(approver.getUseQuota());
            reviewNodeEntity.setName(approver.getName());
            reviewNodeEntity.setStatus(YesOrNoEnum.YES.getCode());
            Integer flag = approver.getFlag();
            reviewNodeEntity.setFlag(flag);
            // 审批人类型(1上级按职位,2单人)
            if (flag == SpecialAfterSaleApproverTypeEnum.POSITION.getState()) {
                List<String> positions = approver.getPositions();
                if (positions == null) {
                    throw new GlobalRuntimeException(ResponseCode.ERROR.getCode(), "审批人类型为职位时职位不能为空");
                }
                reviewNodeEntity.setPositions(StringUtils.join(positions, Symbol.COMMA.getValue()));
            } else if (flag == SpecialAfterSaleApproverTypeEnum.SINGLE.getState()) {
                Integer approverUserId = approver.getId();
                if (approverUserId == null) {
                    throw new GlobalRuntimeException(ResponseCode.ERROR.getCode(), "审批人类型为单人时员工id不能为空");
                }
                reviewNodeEntity.setUserId(approver.getId());
            } else {
                throw new GlobalRuntimeException(ResponseCode.ERROR.getCode(), "审批人类型传入错误");
            }
            nodeEntityList.add(reviewNodeEntity);

        });
        reviewNodeRepository.saveAll(nodeEntityList);

        // 插入position表
        List<String> positions = req.getPositions();
        if (positions == null) {
            throw new GlobalRuntimeException(ResponseCode.ERROR.getCode(), "审批人类型为职位时职位不能为空");
        }
        List<SpecialAfterSaleReviewPositionEntity> positionEntityList = new ArrayList<>(4);
        positions.forEach(position -> {
            SpecialAfterSaleReviewPositionEntity reviewPositionEntity = new SpecialAfterSaleReviewPositionEntity();
            reviewPositionEntity.setConfigId(reviewId);
            reviewPositionEntity.setCreatedAt(localDateTime);
            reviewPositionEntity.setPosition(position);
            positionEntityList.add(reviewPositionEntity);
        });
        reviewPositionRepository.saveAll(positionEntityList);
    }


    /**
     * 查询审批配置列表启用或禁用
     * @param status
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void enableOrDisable(Integer status, Integer id) {
        nodeReposity.updateReviewNodeStatus(status, id);
        reviewRepository.updateReviewStatus(status, id);
    }


    /**
     * 查询审批配置列表
     * @param
     * @return
     */
    @Override
    public List<SpecialAfterSaleApprovalRulesDTO> getSpecialAfterSaleApprovalRulesDetails(Integer status, Pageable pageable) {
        //查到原来的实体类
        Page<SpecialAfterSaleReviewEntity> listPage = reviewReposity.findAllByStatus(status, pageable);
        List<SpecialAfterSaleReviewEntity> entityList = listPage.getContent();
        List<SpecialAfterSaleApprovalRulesDTO> results = new ArrayList<>();
        // 最后更新时间
        for (SpecialAfterSaleReviewEntity entity : entityList) {
            SpecialAfterSaleApprovalRulesDTO rulesDTO = new SpecialAfterSaleApprovalRulesDTO();
            //最后更新时间
            rulesDTO.setLastUpdateAt(entity.getLastUpdateAt());
            //根据group-id查询申请人部门名字
            Integer groupId = entity.getGroupId();
            Map<Integer, UserGroupSimpleDTO> map = userManager.searchUserGroupFromCache(Collections.singleton(groupId));
            for (UserGroupSimpleDTO values : map.values()) {
                for (UserGroupSimpleDTO.GroupInfoVO group : values.getGroups()) {
                    if (groupId.equals(group.getGroupId())) {
                        // 部门名
                        rulesDTO.setGroupName(group.getGroupName());
                        break;
                    }
                }
            }
            //根据configId，查申请人职位
            Integer configId = entity.getId();
            SpecialAfterSaleReviewPositionEntity position = positionRepository.findById(configId.intValue())
                    .orElseThrow(() -> new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "查询不到该申请详情"));
            rulesDTO.setPosition(position.getPosition());

            //根据configId，查审批人
            Optional<SpecialAfterSaleReviewNodeEntity> reviewNodeEntity = nodeRepository.findById(configId.intValue());
            Integer flag = reviewNodeEntity.get().getFlag();
            //如果falg=2,查审批人的姓名，否则查职位
            if (flag == 2) {
                //审批人名字
                Integer userId = reviewNodeEntity.get().getUserId();
                PassportUserInfoDTO userInfoFromRedis = passportFeignManager.getUserInfoFromRedis(userId);
                rulesDTO.setNickname(userInfoFromRedis.getNickname());
            } else {
                //职位
                rulesDTO.setPositions(reviewNodeEntity.get().getPositions());
            }
            //拿到ccIds
            String[] splitCcids = entity.getCcIds().split(",");
            List<Integer> idsList = Stream.of(splitCcids).map(Integer::valueOf).collect(Collectors.toList());
            //根据ccIds查抄送人名字
            List<PassportUserInfoDTO> userInfoListFromRedis = passportFeignManager.getUserInfoListFromRedis(idsList);
            List<String> nicknameList = userInfoListFromRedis.stream().map(PassportUserInfoDTO::getNickname).collect(Collectors.toList());
            rulesDTO.setCcNickname(nicknameList);
            results.add(rulesDTO);
        }
        return results;

    }



}

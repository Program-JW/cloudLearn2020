package com.ruigu.rbox.workflow.service.sale;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.manager.SpecialAfterSaleApplyManager;
import com.ruigu.rbox.workflow.manager.SpecialAfterSaleLogManager;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleReviewNodeEntity;
import com.ruigu.rbox.workflow.model.enums.*;
import com.ruigu.rbox.workflow.repository.SpecialAfterSaleReviewNodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用于查询下一个节点
 *
 * @author liqingtian
 * @date 2020/08/11 12:53
 */
@Slf4j
@Service
public class QueryNextNodeService implements JavaDelegate {

    @Resource
    private SpecialAfterSaleReviewNodeRepository specialAfterSaleReviewNodeRepository;
    @Resource
    private SpecialAfterSaleApplyManager specialAfterSaleApplyManager;
    @Resource
    private SpecialAfterSaleLogManager specialAfterSaleLogManager;
    @Resource
    private PassportFeignManager passportFeignManager;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        // 该类是用来查下一个节点的信息用的
        Integer configId = delegateExecution.getVariable(SpecialAfterSaleUseVariableEnum.CONFIG_ID.getCode(), Integer.class);
        List<SpecialAfterSaleReviewNodeEntity> nodes = specialAfterSaleReviewNodeRepository.findAllByConfigIdOrderBySort(configId);
        if (CollectionUtils.isEmpty(nodes)) {
            log.error("审批节点丢失。instanceId:{}, configId:{}", delegateExecution.getProcessInstanceId(), configId);
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "审批配置节点信息丢失");
        }
        // 查询到所有的node的id
        List<Integer> nodeIdList = nodes.stream().map(SpecialAfterSaleReviewNodeEntity::getId).collect(Collectors.toList());
        final int size = nodeIdList.size();
        // 当前节点
        // 分支1：current node id 为空时说明流程刚创建。 分支2：current node id 不为空时说明有人
        int thisStartIndex = 0;
        Integer currentNodeId = delegateExecution.getVariable(SpecialAfterSaleUseVariableEnum.CURRENT_NODE_ID.getCode(), Integer.class);
        if (Objects.nonNull(currentNodeId)) {
            int index = nodeIdList.indexOf(currentNodeId);
            thisStartIndex = index + 1;
            if (thisStartIndex >= size) {
                // 没有节点了
                delegateExecution.setVariable(SpecialAfterSaleUseVariableEnum.HAVE_NEXT_NODE.getCode(), YesOrNoEnum.NO.getCode());
                return;
            }
        }
        // 最后操作人
        Integer lastOperator = delegateExecution.getVariable(SpecialAfterSaleUseVariableEnum.Last_APPROVER.getCode(), Integer.class);
        // 申请人
        Integer applyUser = delegateExecution.getVariable(SpecialAfterSaleUseVariableEnum.APPLY_USER_ID.getCode(), Integer.class);
        if (!Objects.nonNull(lastOperator)) {
            lastOperator = applyUser;
        }
        Map<String, Object> variable = new HashMap<>(16);
        List<Integer> nextApproverList = new ArrayList<>();
        Integer nextNodeId = null;
        // 循环查找，直到查到具体的审批人
        for (int i = thisStartIndex; i < size; i++) {
            SpecialAfterSaleReviewNodeEntity thisNode = nodes.get(i);
            Integer nodeId = thisNode.getId();
            String specifyUser = delegateExecution.getVariable(SpecialAfterSaleUseVariableEnum.CURRENT_NODE_SPECIFY_USER.getCode() + nodeId, String.class);
            if (StringUtils.isBlank(specifyUser)) {
                // 因为流程是刚开始的
                if (thisNode.getFlag() == SpecialAfterSaleNodeFlagEnum.POSITION.getCode()) {
                    List<PassportUserInfoDTO> manager = getManagerByPosition(lastOperator, StringUtils.split(thisNode.getPositions(), Symbol.COMMA.getValue()));
                    if (CollectionUtils.isNotEmpty(manager)) {
                        nextNodeId = nodeId;
                        nextApproverList = manager.stream().map(PassportUserInfoDTO::getId).collect(Collectors.toList());
                        variable = insertInfo(YesOrNoEnum.YES.getCode(), thisNode.getUseQuota(), nextApproverList, nodeId);
                        break;
                    }
                } else {
                    nextNodeId = nodeId;
                    nextApproverList = Collections.singletonList(thisNode.getUserId());
                    variable = insertInfo(YesOrNoEnum.YES.getCode(), thisNode.getUseQuota(), nextApproverList, nodeId);
                    break;
                }
            } else {
                nextNodeId = nodeId;
                nextApproverList = JsonUtil.parseArray(specifyUser, Integer.class);
                variable = insertInfo(YesOrNoEnum.YES.getCode(), thisNode.getUseQuota(), specifyUser, nodeId);
                break;
            }
        }
        // 最后判断是否有下个节点信息
        if (variable.isEmpty()) {
            delegateExecution.setVariable(SpecialAfterSaleUseVariableEnum.HAVE_NEXT_NODE.getCode(), YesOrNoEnum.NO.getCode());
        } else {
            delegateExecution.setVariables(variable);
            // 记录待审批日志
            Long applyId = delegateExecution.getVariable(SpecialAfterSaleUseVariableEnum.APPLY_ID.getCode(), Long.class);
            specialAfterSaleLogManager.createActionLog(applyId, SpecialAfterSaleLogActionEnum.PENDING_APPROVAL.getValue(), nextNodeId,
                    SpecialAfterSaleLogActionEnum.PENDING_APPROVAL.getCode(), null, YesOrNoEnum.YES.getCode(), nextApproverList);
            // 更新当前审批人
            specialAfterSaleApplyManager.updateCurrentApprover(applyId, nextApproverList);
        }
    }

    private List<PassportUserInfoDTO> getManagerByPosition(Integer userId, String... position) {
        // 查询userId所有leader
        List<List<PassportUserInfoDTO>> allLeader = passportFeignManager.getAllLeader(userId);
        // 职位列表
        List<String> positionList = Arrays.asList(position);
        // 循环查找
        for (List<PassportUserInfoDTO> user : allLeader) {
            List<PassportUserInfoDTO> leader = user.stream()
                    .filter(u -> positionList.contains(u.getPosition()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(leader)) {
                return leader;
            }
        }
        return Collections.emptyList();
    }

    private Map<String, Object> insertInfo(Integer haveNextNode, Integer isNeedQuota, List<Integer> userIds, Integer currentNodeId) {
        Map<String, Object> info = new HashMap<>();
        info.put(SpecialAfterSaleUseVariableEnum.IS_NEED_QUOTA.getCode(), isNeedQuota);
        info.put(SpecialAfterSaleUseVariableEnum.HAVE_NEXT_NODE.getCode(), haveNextNode);
        info.put(SpecialAfterSaleUseVariableEnum.CURRENT_NODE_ID.getCode(), currentNodeId);
        info.put(SpecialAfterSaleUseVariableEnum.CURRENT_NODE_USER.getCode() + currentNodeId, JsonUtil.toJsonString(userIds));
        return info;
    }

    private Map<String, Object> insertInfo(Integer haveNextNode, Integer isNeedQuota, String userIds, Integer currentNodeId) {
        Map<String, Object> info = new HashMap<>();
        info.put(SpecialAfterSaleUseVariableEnum.IS_NEED_QUOTA.getCode(), isNeedQuota);
        info.put(SpecialAfterSaleUseVariableEnum.HAVE_NEXT_NODE.getCode(), haveNextNode);
        info.put(SpecialAfterSaleUseVariableEnum.CURRENT_NODE_ID.getCode(), currentNodeId);
        info.put(SpecialAfterSaleUseVariableEnum.CURRENT_NODE_USER.getCode() + currentNodeId, userIds);
        return info;
    }
}

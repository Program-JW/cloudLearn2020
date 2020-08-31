package com.ruigu.rbox.workflow.service.sale;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.manager.SpecialAfterSaleApplyManager;
import com.ruigu.rbox.workflow.manager.SpecialAfterSaleLogManager;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleReviewEntity;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.SpecialAfterSaleLogActionEnum;
import com.ruigu.rbox.workflow.model.enums.SpecialAfterSaleUseVariableEnum;
import com.ruigu.rbox.workflow.model.enums.Symbol;
import com.ruigu.rbox.workflow.repository.SpecialAfterSaleReviewRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liqingtian
 * @date 2020/08/19 15:56
 */
@Service
public class QueryAndSetExecutorInfoService implements JavaDelegate {

    @Resource
    private SpecialAfterSaleReviewRepository specialAfterSaleReviewRepository;
    @Resource
    private SpecialAfterSaleLogManager specialAfterSaleLogManager;
    @Resource
    private SpecialAfterSaleApplyManager specialAfterSaleApplyManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(DelegateExecution delegateExecution) {
        // 查询配置信息
        Integer configId = delegateExecution.getVariable(SpecialAfterSaleUseVariableEnum.CONFIG_ID.getCode(), Integer.class);
        SpecialAfterSaleReviewEntity configInfo = specialAfterSaleReviewRepository.findById(configId)
                .orElseThrow(() -> new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "审批配置信息查询失败"));
        String executor = configInfo.getExecutorIds();
        if (StringUtils.isBlank(executor)) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "配置异常，执行人信息为空。");
        }
        // 获取执行人id
        List<Integer> executorIdList = Stream.of(StringUtils.split(executor, Symbol.COMMA.getValue()))
                .map(Integer::valueOf).collect(Collectors.toList());
        // 客服的节点id为-1吧
        final int executionNodeId = -1;
        Map<String, Object> var = new HashMap<>(4);
        var.put(SpecialAfterSaleUseVariableEnum.CURRENT_NODE_ID.getCode(), executionNodeId);
        var.put(SpecialAfterSaleUseVariableEnum.CURRENT_NODE_USER.getCode() + executionNodeId, JsonUtil.toJsonString(executorIdList));
        delegateExecution.setVariables(var);
        // 记录待审批日志
        Long applyId = delegateExecution.getVariable(SpecialAfterSaleUseVariableEnum.APPLY_ID.getCode(), Long.class);
        specialAfterSaleLogManager.createActionLog(applyId, SpecialAfterSaleLogActionEnum.PENDING_APPROVAL.getValue(), null,
                SpecialAfterSaleLogActionEnum.PENDING_APPROVAL.getCode(), null, YesOrNoEnum.YES.getCode(), executorIdList);
        // 更新当前审批人
        specialAfterSaleApplyManager.updateCurrentApprover(applyId, executorIdList);
    }
}

package com.ruigu.rbox.workflow.service.stock;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.feign.ScmFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.StockChangeApplyEntity;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.TaskState;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/11/03 15:06
 */
@Slf4j
@Service("updateStockChangeApplyApprovalTask")
public class UpdateStockChangeApplyApprovalTask implements JavaDelegate {

    @Autowired
    private ScmFeignClient scmFeignClient;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.debug("========================================= 更改申请单审批状态 ==============================");
        final String APPROVED = "approved";
        Map<String, Object> variables = delegateExecution.getVariables();
        if (variables.containsKey(APPROVED)) {
            // 查询 申请记录
            final String APPLY_ID = "applyId";
            final String UPDATE_APPROVE_STATUS = "updateApproveStatus";
            int updateApplyStatus = 0;
            try {
                Integer applyId = Integer.valueOf(String.valueOf(variables.get(APPLY_ID)));
                ServerResponse<StockChangeApplyEntity> response = scmFeignClient.getStockChangeApplyById(applyId);
                if (response.getCode() != ResponseCode.SUCCESS.getCode()) {
                    throw new GlobalRuntimeException(response.getCode(), response.getMessage());
                }
                StockChangeApplyEntity apply = response.getData();
                if (apply == null) {
                    throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "数据异常，无法找到该申请详情信息。");
                }
                int approvedStatus = Integer.parseInt(String.valueOf(variables.get(APPROVED)));
                if (approvedStatus == YesOrNoEnum.YES.getCode()) {
                    apply.setStatus(TaskState.APPROVAL.getState());
                } else {
                    apply.setStatus(TaskState.REJECT.getState());
                }
                apply.setApprovalTime(new Date());
                ServerResponse<Integer> serverResponse = scmFeignClient.saveChangeApply(apply);
                if (serverResponse.getCode() == ResponseCode.SUCCESS.getCode()) {
                    updateApplyStatus = YesOrNoEnum.YES.getCode();
                }
            } catch (Exception e) {
                log.error("|- 更改申请单审批状态异常。", e);
            } finally {
                delegateExecution.setVariable(UPDATE_APPROVE_STATUS, updateApplyStatus);
            }
            log.debug("========================================= end ==============================");
        }
    }
}

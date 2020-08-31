package com.ruigu.rbox.workflow.service.stock;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.service.RemoteService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author chenzhenya
 * @date 2019/11/13 20:17
 */
@Slf4j
@Service("updateStockLockApplyApprovalTask")
public class UpdateStockLockApplyApprovalTask extends BaseUpdateApplyApprovalTask {

    @Resource
    private RemoteService remoteService;

    @Override
    public void updateStatus(DelegateExecution delegateExecution) {
        log.debug("========================================= 更改锁库申请单审批状态 ==============================");
        Map<String, Object> variables = delegateExecution.getVariables();
        Integer applyId = JsonUtil.parseObject(String.valueOf(variables.get("businessKey")), Integer.class);
        Integer approvedStatus = Integer.valueOf(String.valueOf(variables.get("approved")));
        log.info("applyId" + applyId + ",approvedStatus" + approvedStatus);
        final String UPDATE_LOCK_APPROVE_STATUS = "updateLockApproveStatus";
        int updateLockApproveStatus = 0;
        try {
            Map<String, Object> param = new HashMap<>(5);

            param.put("approvedStatus",approvedStatus);
            param.put("applyId",applyId);
            log.info("调用scm节点");
            ServerResponse<Object> response = remoteService.request("POST", "http://scm//stock-lock-apply/stock-lock-data-get-save", param);
            if (response.getCode() != ResponseCode.SUCCESS.getCode()) {
                log.error("更新锁定库存请求状态失败");
                throw new GlobalRuntimeException(response.getCode(), "更新锁定库存请求状态失败");
            }
            updateLockApproveStatus = YesOrNoEnum.YES.getCode();
        } catch (Exception e) {
            log.error("|- 更改申请单审批状态异常。", e);
        } finally {
            log.info("updateLockApproveStatus" + updateLockApproveStatus);
            delegateExecution.setVariable(UPDATE_LOCK_APPROVE_STATUS, updateLockApproveStatus);
        }
        log.debug("========================================= end ==============================");
    }
}

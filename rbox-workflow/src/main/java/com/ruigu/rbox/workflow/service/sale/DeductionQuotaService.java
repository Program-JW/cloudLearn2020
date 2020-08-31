package com.ruigu.rbox.workflow.service.sale;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleQuotaTransactionEntity;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.SpecialAfterSaleUseVariableEnum;
import com.ruigu.rbox.workflow.service.SpecialAfterSaleQuotaService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author liqingtian
 * @date 2020/08/11 12:54
 */
@Slf4j
@Service
public class DeductionQuotaService implements JavaDelegate {

    @Resource
    private SpecialAfterSaleQuotaService specialAfterSaleQuotaService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(DelegateExecution delegateExecution) {
        // 该service用于扣减额度，维护流水表
        // 首先拿到最后的审批人，和最后审批状态
        Integer lastApprover = delegateExecution.getVariable(SpecialAfterSaleUseVariableEnum.Last_APPROVER.getCode(), Integer.class);
        String totalAmountStr = delegateExecution.getVariable(SpecialAfterSaleUseVariableEnum.APPLY_TOTAL_AMOUNT.getCode(), String.class);
        BigDecimal totalAmount = new BigDecimal(totalAmountStr);
        Long applyId = delegateExecution.getVariable(SpecialAfterSaleUseVariableEnum.APPLY_ID.getCode(), Long.class);
        // 能进入这个service的都需要扣减额度 (获取审批人使用的额度)
        Integer quotaId = delegateExecution.getVariable(SpecialAfterSaleUseVariableEnum.USE_QUOTA_ID.getCode(), Integer.class);
        if (Objects.isNull(quotaId)) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "操作失败，请先选择使用的额度");
        }
        SpecialAfterSaleQuotaTransactionEntity transaction = specialAfterSaleQuotaService.deductionTransaction(applyId, lastApprover, quotaId, totalAmount);
        // 否则设置额度不足进入下一次审批
        if (Objects.nonNull(transaction)) {
            delegateExecution.setVariable(SpecialAfterSaleUseVariableEnum.IS_ENOUGH_QUOTA.getCode(), YesOrNoEnum.YES.getCode());
        } else {
            delegateExecution.setVariable(SpecialAfterSaleUseVariableEnum.IS_ENOUGH_QUOTA.getCode(), YesOrNoEnum.NO.getCode());
        }
    }
}

package com.ruigu.rbox.workflow.service.stock;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

/**
 * 仓库检验
 * @author alan.zhao
 * @date 2019/11/02 23:42
 */
@Slf4j
@Service("checkStockServiceTask")
public class CheckStockServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("mpCheckStockServiceTask 调用成功");
        delegateExecution.setVariable("verified", 0);
    }

}

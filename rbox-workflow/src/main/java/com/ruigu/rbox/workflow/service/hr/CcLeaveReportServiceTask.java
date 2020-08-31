package com.ruigu.rbox.workflow.service.hr;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 发送信息给抄送人
 *
 * @author alan.zhao
 */
@Slf4j
@Service("ccLeaveReportServiceTask")
public class CcLeaveReportServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        List<Integer> ccUsers = (List<Integer>) delegateExecution.getVariable("ccUsers");
        log.info("抄送请假报备给:"+ JSON.toJSONString(ccUsers));
    }
}

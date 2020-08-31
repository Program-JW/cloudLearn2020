package com.ruigu.rbox.workflow.controller.timer;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.service.GuaranteeSuccessMqMessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liqingtian
 * @date 2020/07/22 10:10
 */
@RestController
public class GuaranteeSuccessFailMessageRetryController {

    @Resource
    private GuaranteeSuccessMqMessageService guaranteeSuccessMqMessageService;

    @GetMapping("/guarantee-success/fail/retry")
    public ServerResponse failRetry() {
        guaranteeSuccessMqMessageService.scanRecordAndRetry();
        return ServerResponse.ok();
    }

}

package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.request.OperationLogRequest;
import com.ruigu.rbox.workflow.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liqingtian
 * @date 2019/10/16 16:37
 */
@RestController
@RequestMapping("/log")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @GetMapping()
    public ServerResponse getAllLogs(OperationLogRequest request) {
        return ServerResponse.ok(operationLogService.getAllLogsPage(request));
    }
}

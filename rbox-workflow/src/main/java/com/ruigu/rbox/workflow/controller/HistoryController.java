package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.request.SearchHistoryRequest;
import com.ruigu.rbox.workflow.service.WorkflowHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 流程历史接口
 * @author alan.zhao
 */
@Controller
@RequestMapping(value = "/history")
public class HistoryController {
    @Autowired
    private WorkflowHistoryService workflowHistoryService;

    @RequestMapping("/list")
    @ResponseBody
    public ServerResponse<Page<Map<String, Object>>> list(SearchHistoryRequest request) {
        return ServerResponse.ok(workflowHistoryService.search(request));
    }
}

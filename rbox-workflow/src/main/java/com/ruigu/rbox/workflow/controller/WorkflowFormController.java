package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.service.WorkflowFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * @author liqingtian
 * @date 2019/10/11 13:51
 */
@Validated
@RestController
@RequestMapping("/form")
public class WorkflowFormController {

    @Autowired
    private WorkflowFormService workflowFormService;

    @GetMapping("/select/content")
    public ServerResponse<String> getSelectFormContent(@NotBlank(message = "流程定义ID不能为空") String definitionId) {
        return ServerResponse.ok(workflowFormService.getSelectFormContentByDefinition(definitionId));
    }
}

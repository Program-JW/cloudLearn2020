package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.model.entity.WorkflowFormEntity;
import com.ruigu.rbox.workflow.repository.WorkflowFormRepository;
import com.ruigu.rbox.workflow.service.WorkflowFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liqingtian
 * @date 2019/10/11 13:56
 */
@Service
public class WorkflowFormServiceImpl implements WorkflowFormService {

    @Autowired
    private WorkflowFormRepository workflowFormRepository;

    @Override
    public String getSelectFormContentByDefinition(String definitionId) {
        WorkflowFormEntity form = workflowFormRepository.findByDefinitionId(definitionId);
        if (form != null) {
            return form.getSelectFormContent();
        }
        return null;
    }
}

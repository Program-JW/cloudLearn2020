package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.model.entity.WorkflowCategoryEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowDefinitionEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowFormEntity;
import com.ruigu.rbox.workflow.model.request.StartInstanceRequest;
import com.ruigu.rbox.workflow.model.request.SubmitFormRequest;
import com.ruigu.rbox.workflow.model.vo.WorkflowCategoryNode;
import com.ruigu.rbox.workflow.repository.WorkflowCategoryRepository;
import com.ruigu.rbox.workflow.repository.WorkflowDefinitionRepository;
import com.ruigu.rbox.workflow.repository.WorkflowFormRepository;
import com.ruigu.rbox.workflow.service.WorkflowCategoryService;
import com.ruigu.rbox.workflow.service.WorkflowInstanceService;
import com.ruigu.rbox.workflow.supports.ValidResult;
import com.ruigu.rbox.workflow.supports.ValidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jianghuilin
 */
@Service
public class WorkflowCategoryServiceImpl implements WorkflowCategoryService {
    @Autowired
    private WorkflowCategoryRepository workflowCategoryRepository;

    @Autowired
    private WorkflowFormRepository workflowFormRepository;

    @Autowired
    private WorkflowDefinitionRepository workflowDefinitionRepository;

    @Autowired
    private WorkflowInstanceService workflowInstanceService;

    @Override
    public List<WorkflowCategoryNode> search() throws Exception {
        List<WorkflowCategoryEntity> entities = workflowCategoryRepository.findByParentId(0);
        List<WorkflowCategoryEntity> allEntities = workflowCategoryRepository.findAll();
        Map<Integer, List<WorkflowCategoryEntity>> map = new HashMap<>(16);
        List<WorkflowCategoryEntity> dics;
        if (allEntities == null) {
            throw new RuntimeException("数据库中没有数据");
        }
        for (WorkflowCategoryEntity entity : allEntities) {
            if (entity.getParentId() != 0) {
                if (map.get(entity.getParentId()) == null) {
                    dics = new ArrayList<>();
                } else {
                    dics = map.get(entity.getParentId());
                }
                dics.add(entity);
                map.put(entity.getParentId(), dics);
            }
        }
        List<WorkflowCategoryNode> nodes = new ArrayList<>();
        if (entities == null) {
            throw new RuntimeException("数据库中没有-没有父分类的数据");
        }
        for (WorkflowCategoryEntity entity : entities) {
            WorkflowCategoryNode node = new WorkflowCategoryNode();
            node.setId(entity.getId());
            node.setName(entity.getName());
            if (StringUtils.isNotBlank(entity.getDefinitionKey())) {
                node.setDefinitionKey(entity.getDefinitionKey());
            }
            List<WorkflowCategoryNode> childNodes = new ArrayList<>();
            if (map.get(entity.getId()) != null) {
                for (WorkflowCategoryEntity child : map.get(entity.getId())) {
                    WorkflowCategoryNode childNode = new WorkflowCategoryNode();
                    childNode.setId(child.getId());
                    childNode.setName(child.getName());
                    if (StringUtils.isNotBlank(child.getDefinitionKey())) {
                        childNode.setDefinitionKey(child.getDefinitionKey());
                    }
                    childNodes.add(childNode);
                }
            }
            node.setChildren(childNodes);
            node.setHideHidden(true);
            nodes.add(node);
        }
        return nodes;
    }

    @Override
    public WorkflowFormEntity json(Integer id) throws Exception {
        if (id == null) {
            throw new RuntimeException("流程分类id没有传");
        }
        WorkflowDefinitionEntity definitionEntity = findDefinition(id);
        WorkflowFormEntity formEntity = workflowFormRepository.findTopBydefinitionId(definitionEntity.getId());
        return formEntity;
    }

    WorkflowDefinitionEntity findDefinition(Integer id) {
        WorkflowCategoryEntity categoryEntity = workflowCategoryRepository.findTopById(id);
        WorkflowDefinitionEntity definitionEntity = workflowDefinitionRepository.findNewDefinition(categoryEntity.getDefinitionKey());
        return definitionEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitForm(SubmitFormRequest submitFormRequest, Long userId) throws Exception {
        ValidResult validResult = ValidUtil.validFields(submitFormRequest,
                new String[]{"variables", "categoryId"},
                new Object[][]{
                        {ValidUtil.REQUIRED},
                        {ValidUtil.REQUIRED}
                });
        if (!validResult.valid) {
            throw new VerificationFailedException(400, validResult.message);
        }
        try {
            WorkflowDefinitionEntity definitionEntity = findDefinition(Integer.valueOf(submitFormRequest.getCategoryId()));
            if (definitionEntity == null) {
                throw new RuntimeException("该流程分类Id没有对应的流程定义！");
            }
            StartInstanceRequest request = new StartInstanceRequest();
            request.setKey(definitionEntity.getInitialCode());
            request.setVariables(submitFormRequest.getVariables());
            request.setBusinessKey("455");
            workflowInstanceService.start(request, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

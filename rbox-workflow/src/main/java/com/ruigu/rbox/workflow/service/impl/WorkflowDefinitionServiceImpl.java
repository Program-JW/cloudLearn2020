package com.ruigu.rbox.workflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruigu.rbox.workflow.exceptions.DeployFailException;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.model.bpmn.BusinessField;
import com.ruigu.rbox.workflow.model.bpmn.BusinessUrl;
import com.ruigu.rbox.workflow.model.bpmn.Button;
import com.ruigu.rbox.workflow.model.bpmn.InitialUrl;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.NoticeType;
import com.ruigu.rbox.workflow.model.request.SaveDefinitionRequest;
import com.ruigu.rbox.workflow.model.request.SaveFormRequest;
import com.ruigu.rbox.workflow.model.request.SearchDefinitionRequest;
import com.ruigu.rbox.workflow.model.response.WorkflowDefinitionDetail;
import com.ruigu.rbox.workflow.repository.*;
import com.ruigu.rbox.workflow.service.WorkflowDefinitionService;
import com.ruigu.rbox.workflow.supports.*;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author alan.zhao
 * @date
 */
@Service
public class WorkflowDefinitionServiceImpl implements WorkflowDefinitionService {

    @Autowired
    private WorkflowDefinitionRepository definitionRepository;

    @Autowired
    private WorkflowDefinitionNativeRepository workflowDefinitionNativeRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private WorkflowFormRepository workflowFormRepository;

    @Autowired
    private DefinitionNoticeConfigRepository definitionNoticeConfigRepository;

    @Autowired
    private NodeNoticeConfigRepository nodeNoticeConfigRepository;

    public final static String ACTIVITI_CATEGORY = "http://www.activiti.org/processdef";

    @Transactional(rollbackFor = Exception.class)
    public Model saveModel(SaveDefinitionRequest request) throws Exception {
        Model model;
        if (request.getId() == null) {
            model = repositoryService.newModel();
            model.setCategory(ACTIVITI_CATEGORY);
            model.setKey(StringUtils.isNotBlank(request.getKey()) ? request.getKey().trim() : UUID.randomUUID().toString());
            model.setVersion(1);
        } else {
            model = repositoryService.getModel(request.getId());
        }
        ObjectUtil.extendObject(model, request, true);
        repositoryService.saveModel(model);
        if (request != null && StringUtils.isNotBlank(request.getGraph())) {
            repositoryService.addModelEditorSource(model.getId(), request.getGraph().getBytes());
        }
        return model;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createDraft(SaveDefinitionRequest request, Long userId) throws Exception {
        ValidResult validResult = ValidUtil.validFields(request,
                new String[]{"id", "name", "description", "status", "key"},
                new Object[][]{
                        {ValidUtil.MUST_NULL},
                        {ValidUtil.REQUIRED, ValidUtil.NOT_BLANK, new ValidUtil.StringLength(0, 30)},
                        {new ValidUtil.StringLength(0, 100)},
                        {ValidUtil.NON_NEGATIVE_INTEGER},
                        {(ValidPlugin) (field, value) -> {
                            ValidResult v = new ValidResult();
                            if (StringUtils.isBlank(value) || !definitionRepository.existsByInitialCode(value)) {
                                v.valid = true;
                            } else {
                                v.message = "流程key=" + value + "已占用";
                                v.valid = false;
                            }
                            return v;
                        }}
                });
        if (!validResult.valid) {
            throw new VerificationFailedException(400, validResult.message);
        }
        request.setGraph(BpmnUtil.addDefaultToBpmnString(request.getKey(), request.getGraph(), String.class));
        BpmnModel bpmnModel = BpmnUtil.toBpmnModel(request.getGraph());
        Map<String, Object> processAttributes = parseProcessAttributes(bpmnModel);
        WorkflowDefinitionEntity entity = new WorkflowDefinitionEntity();
        Model model = saveModel(request);
        entity.setId(model.getId());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setInitialCode(model.getKey());
        entity.setVersion(BigInteger.valueOf(model.getVersion()));
        appendProcessAttributes(entity, processAttributes);
        entity.setIsReleased(0);
        entity.setCreatedBy(userId);
        entity.setCreatedOn(new Date());
        entity.setLastUpdatedBy(userId);
        entity.setLastUpdatedOn(new Date());
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        } else {
            entity.setStatus(0);
        }
        definitionRepository.save(entity);
        saveBusinessFields(model.getId(), processAttributes, userId);
        //更新节点
        if (bpmnModel != null) {
            List<NodeEntity> nodes = parseUserTaskList(bpmnModel, model.getId());
            batchUpdateNodes(model.getId(), nodes, userId);
        }
        return model.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveAsDraft(SaveDefinitionRequest request, Long userId) throws Exception {
        validSaveDefinitionRequest(request);
        boolean newVersionIfReleased = request.getNewVersionIfReleased() != null && request.getNewVersionIfReleased();
        Optional<WorkflowDefinitionEntity> oldEntityOptional = definitionRepository.findById(request.getId());
        WorkflowDefinitionEntity oldEntity = oldEntityOptional.isPresent() ? oldEntityOptional.get() : null;
        boolean lastOneIsReleased = oldEntityOptional.isPresent() ? Integer.valueOf(1).equals(oldEntity.getIsReleased()) : false;
        if (lastOneIsReleased && !newVersionIfReleased) {
            throw new VerificationFailedException(400, "此流程定义[id=" + oldEntity.getId() + "]已发布,不允许修改");
        }

        WorkflowDefinitionEntity entity = new WorkflowDefinitionEntity();
        Model model = null;
        if (!lastOneIsReleased) {
            //直接在原来的数据上更新
            request.setGraph(BpmnUtil.addDefaultToBpmnString(oldEntity.getInitialCode(), request.getGraph(), String.class));
            BpmnModel bpmnModel = BpmnUtil.toBpmnModel(request.getGraph());
            Map<String, Object> processAttributes = parseProcessAttributes(bpmnModel);
            model = saveModel(request);
            entity.setId(model.getId());
            entity.setName(request.getName());
            entity.setDescription(request.getDescription());
            appendProcessAttributes(entity, processAttributes);
            entity.setLastUpdatedBy(userId);
            entity.setLastUpdatedOn(new Date());
            entity.setStatus(request.getStatus());
            ObjectUtil.extendObject(oldEntity, entity, true);
            definitionRepository.save(oldEntity);
            saveBusinessFields(model.getId(), processAttributes, userId);
            //更新节点
            if (bpmnModel != null) {
                List<NodeEntity> nodes = parseUserTaskList(bpmnModel, model.getId());
                batchUpdateNodes(model.getId(), nodes, userId);
            }
        } else {
            //另存为新的数据
            String fromId = request.getId();
            Integer maxVersion = definitionRepository.maxVersion(request.getId());
            WorkflowDefinitionEntity latestDraft = definitionRepository.latestDraft(oldEntity.getInitialCode());
            request.setGraph(loadGraph(oldEntity.getId()));
            request.setGraph(BpmnUtil.addDefaultToBpmnString(oldEntity.getInitialCode(), request.getGraph(), String.class));
            request.setKey(oldEntity.getInitialCode());
            if (StringUtils.isBlank(request.getName())) {
                request.setName(oldEntity.getName());
            }
            if (StringUtils.isBlank(request.getDescription())) {
                request.setDescription(oldEntity.getDescription());
            }
            if (latestDraft == null) {
                model = saveModel(request.cloneAndSetValues(null, request.getGraph()));
                BpmnModel bpmnModel = BpmnUtil.toBpmnModel(request.getGraph());
                Map<String, Object> processAttributes = parseProcessAttributes(bpmnModel);
                appendProcessAttributes(entity, processAttributes);
                int version = 1;
                if (maxVersion != null) {
                    version = maxVersion + 1;
                }
                entity.setId(model.getId());
                entity.setName(model.getName());
                entity.setInitialCode(oldEntity.getInitialCode());
                entity.setDescription(request.getDescription());
                entity.setVersion(BigInteger.valueOf(version));
                entity.setIsReleased(0);
                entity.setCreatedBy(userId);
                entity.setCreatedOn(new Date());
                entity.setLastUpdatedBy(userId);
                entity.setLastUpdatedOn(new Date());
                entity.setStatus(0);
                definitionRepository.save(entity);
                saveBusinessFields(model.getId(), processAttributes, userId);
            } else {
                model = saveModel(request.cloneAndSetValues(latestDraft.getId(), request.getGraph()));
                BpmnModel bpmnModel = BpmnUtil.toBpmnModel(request.getGraph());
                Map<String, Object> processAttributes = parseProcessAttributes(bpmnModel);
                latestDraft.setName(model.getName());
                latestDraft.setDescription(request.getDescription());
                appendProcessAttributes(latestDraft, processAttributes);
                latestDraft.setLastUpdatedBy(userId);
                latestDraft.setLastUpdatedOn(new Date());
                definitionRepository.save(latestDraft);
                saveBusinessFields(model.getId(), processAttributes, userId);
            }
            // 复制启动表单
            WorkflowFormEntity workflowFormEntity = workflowFormRepository.findByDefinitionId(fromId);
            if (workflowFormEntity != null) {
                WorkflowFormEntity workflowFormEntityCopy = new WorkflowFormEntity();
                workflowFormEntityCopy.setDefinitionId(model.getId());
                workflowFormEntityCopy.setFormContent(workflowFormEntity.getFormContent());
                workflowFormEntityCopy.setCreatedBy(userId);
                workflowFormEntityCopy.setCreatedOn(new Date());
                workflowFormEntityCopy.setLastUpdatedBy(userId);
                workflowFormEntityCopy.setLastUpdatedOn(new Date());
                workflowFormEntityCopy.setStatus(1);
                workflowFormRepository.save(workflowFormEntityCopy);
            }
            //复制节点
            BpmnModel bpmnModel = BpmnUtil.toBpmnModel(request.getGraph());
            if (bpmnModel != null) {
                List<NodeEntity> nodes = parseUserTaskList(bpmnModel, model.getId());
                batchCopyNodes(fromId, model.getId(), nodes, userId);
            }
            // 更新通知配置信息
            updateNoticeConfig(request.getId(), oldEntity, model, userId);
        }
        return model.getId();
    }

    private void updateNoticeConfig(String fromId, WorkflowDefinitionEntity oldEntity, Model model, Long userId) {
        // 更新定义通知配置信息操作
        // 获取原定义通知配置集合
        List<DefinitionNoticeConfigEntity> oldDefinitionNoticeConfigEntityList = definitionNoticeConfigRepository.findAllByDefinitionId(oldEntity.getId());
        // 深度克隆成新的集合
        List<DefinitionNoticeConfigEntity> newDefinitionNoticeConfigEntityList = oldDefinitionNoticeConfigEntityList.stream().map(SerializationUtils::clone).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(newDefinitionNoticeConfigEntityList)) {
            // 更新成新的通知配置信息
            newDefinitionNoticeConfigEntityList.forEach(definitionNoticeConfigEntity -> {
                definitionNoticeConfigEntity.setId(null);
                definitionNoticeConfigEntity.setLastUpdatedOn(new Date());
                definitionNoticeConfigEntity.setCreatedBy(userId);
                definitionNoticeConfigEntity.setDefinitionId(model.getId());
            });
            // 保存新复制的定义通知配置
            definitionNoticeConfigRepository.saveAll(newDefinitionNoticeConfigEntityList);
        }
        // 更新节点通知配置信息操作
        // 获取原节点集合
        List<NodeEntity> nodeEntityList = nodeRepository.findAllByModelId(fromId);
        if (CollectionUtils.isNotEmpty(nodeEntityList)) {
            // 循环节点，获取对应的节点通知配置信息，一个节点对应多个通知配置
            nodeEntityList.forEach(nodeEntity -> {
                // 获取原节点通知配置信息集合
                List<NodeNoticeConfigEntity> oldNodeNoticeConfigEntityList = nodeNoticeConfigRepository.findAllByNodeId(nodeEntity.getId());
                // 深度克隆成新的对象集合
                List<NodeNoticeConfigEntity> newNodeNoticeConfigEntityList = oldNodeNoticeConfigEntityList.stream().map(SerializationUtils::clone).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(newNodeNoticeConfigEntityList)) {
                    // 设置成新的通知配置信息
                    newNodeNoticeConfigEntityList.forEach(nodeNoticeConfigEntity -> {
                        nodeNoticeConfigEntity.setId(null);
                        nodeNoticeConfigEntity.setLastUpdatedOn(new Date());
                        nodeNoticeConfigEntity.setLastUpdatedBy(userId);
                        nodeNoticeConfigEntity.setNodeId(model.getId() + ":" + nodeEntity.getGraphId());
                    });
                    // 保存新复制的节点通知配置
                    nodeNoticeConfigRepository.saveAll(newNodeNoticeConfigEntityList);
                }
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String publish(SaveDefinitionRequest request, Long userId) throws Exception {
        validSaveDefinitionRequest(request);
        Optional<WorkflowDefinitionEntity> oldEntityOptional = definitionRepository.findById(request.getId());
        WorkflowDefinitionEntity lastEntity = oldEntityOptional.isPresent() ? oldEntityOptional.get() : null;
        boolean lastOneIsReleased = oldEntityOptional.isPresent() ? Integer.valueOf(1).equals(lastEntity.getIsReleased()) : false;
        String deploymentId;
        if (lastOneIsReleased) {
            deploymentId = deploy(lastEntity.getId(), userId);
        } else {
            request.setGraph(BpmnUtil.addDefaultToBpmnString(lastEntity.getInitialCode(), request.getGraph(), String.class));
            Integer maxVersion = definitionRepository.maxVersion(request.getId());
            int version = 1;
            if (maxVersion != null) {
                version = maxVersion + 1;
            }
            WorkflowDefinitionEntity entity = new WorkflowDefinitionEntity();
            Model model = saveModel(request);
            entity.setId(model.getId());
            entity.setName(request.getName());
            entity.setDescription(request.getDescription());
            entity.setIsReleased(1);
            entity.setLastUpdatedBy(userId);
            entity.setLastUpdatedOn(new Date());
            if (request.getStatus() != null) {
                entity.setStatus(request.getStatus());
            } else {
                entity.setStatus(1);
            }
            entity.setVersion(BigInteger.valueOf(version));
            ObjectUtil.extendObject(lastEntity, entity, true);
            definitionRepository.save(lastEntity);
            //更新节点
            BpmnModel bpmnModel = BpmnUtil.toBpmnModel(request.getGraph());
            if (bpmnModel != null) {
                List<NodeEntity> nodes = parseUserTaskList(bpmnModel, model.getId());
                batchUpdateNodes(model.getId(), nodes, userId);
            }
            //发布流程
            deploymentId = deploy(model.getId(), userId);
        }
        WorkflowDefinitionEntity updateParam = new WorkflowDefinitionEntity();
        updateParam.setId(lastEntity.getId());
        updateParam.setDeploymentId(deploymentId);
        updateParam.setLastUpdatedBy(userId);
        updateParam.setLastUpdatedOn(new Date());
        ObjectUtil.extendObject(lastEntity, updateParam, true);
        definitionRepository.save(lastEntity);
        return lastEntity.getId();
    }

    public void appendProcessAttributes(WorkflowDefinitionEntity entity, Map<String, Object> processAttributes) {
        if (processAttributes == null) {
            return;
        }

        BusinessUrl businessUrl = (BusinessUrl) processAttributes.get("businessUrl");
        if (businessUrl != null) {
            if (StringUtils.isBlank(businessUrl.getMobileValue())) {
                entity.setMobileBusinessUrl(businessUrl.getValue());
            } else {
                entity.setMobileBusinessUrl(businessUrl.getMobileValue());
            }
            if (StringUtils.isBlank(businessUrl.getPcValue())) {
                entity.setPcBusinessUrl(businessUrl.getValue());
            } else {
                entity.setPcBusinessUrl(businessUrl.getPcValue());
            }
            entity.setBusinessUrl(businessUrl.getPcValue());
        }

        InitialUrl initialUrl = (InitialUrl) processAttributes.get("initialUrl");
        if (initialUrl != null) {
            if (StringUtils.isBlank(initialUrl.getMobileValue())) {
                entity.setMobileInitialUrl(initialUrl.getValue());
            } else {
                entity.setMobileInitialUrl(initialUrl.getMobileValue());
            }
            if (StringUtils.isBlank(initialUrl.getPcValue())) {
                entity.setPcInitialUrl(initialUrl.getValue());
            } else {
                entity.setPcInitialUrl(initialUrl.getPcValue());
            }
            entity.setInitialUrl(initialUrl.getValue());
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setForm(SaveFormRequest request, Long userId) throws Exception {
        ValidResult validResult = ValidUtil.validFields(request,
                new String[]{"type", "id", "form"},
                new Object[][]{
                        {ValidUtil.REQUIRED, ValidUtil.NON_NEGATIVE_INTEGER},
                        {ValidUtil.REQUIRED, ValidUtil.NOT_BLANK},
                        {ValidUtil.REQUIRED, ValidUtil.NOT_BLANK}
                });
        if (!validResult.valid) {
            throw new VerificationFailedException(400, validResult.message);
        }
        final int startForm = 1;
        final int nodeForm = 2;
        if (request.getType() == startForm) {
            WorkflowFormEntity workflowFormEntity = workflowFormRepository.findByDefinitionId(request.getId());
            if (workflowFormEntity == null) {
                workflowFormEntity = new WorkflowFormEntity();
                workflowFormEntity.setDefinitionId(request.getId());
                workflowFormEntity.setFormContent(request.getForm());
                workflowFormEntity.setCreatedBy(userId);
                workflowFormEntity.setCreatedOn(new Date());
                workflowFormEntity.setStatus(1);
            } else {
                workflowFormEntity.setFormContent(request.getForm());
            }
            workflowFormEntity.setLastUpdatedBy(userId);
            workflowFormEntity.setLastUpdatedOn(new Date());
            workflowFormRepository.save(workflowFormEntity);
        } else if (request.getType() == nodeForm) {
            NodeEntity nodeEntity = nodeInfo(request.getId());
            if (nodeEntity == null) {
                throw new VerificationFailedException(400, "数据不存在");
            }
            nodeEntity.setFormContent(request.getForm());
            nodeEntity.setLastUpdatedBy(userId);
            nodeEntity.setLastUpdatedOn(new Date());
            nodeRepository.save(nodeEntity);
        } else {
            throw new VerificationFailedException(400, "type只能为1或者2");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBusinessFields(String definitionId, Map<String, Object> processAttributes, Long userId) throws Exception {
        String attributeName = "businessField";
        if (processAttributes == null || processAttributes.get(attributeName) == null) {
            return;
        }
        List<BusinessField> fields = (List<BusinessField>) processAttributes.get(attributeName);
        WorkflowFormEntity workflowFormEntity = workflowFormRepository.findByDefinitionId(definitionId);
        if (workflowFormEntity == null) {
            workflowFormEntity = new WorkflowFormEntity();
            workflowFormEntity.setDefinitionId(definitionId);
            workflowFormEntity.setSelectFormContent(JSON.toJSONString(fields));
            workflowFormEntity.setCreatedBy(userId);
            workflowFormEntity.setCreatedOn(new Date());
            workflowFormEntity.setStatus(1);
        } else {
            workflowFormEntity.setSelectFormContent(JSON.toJSONString(fields));
        }
        workflowFormEntity.setLastUpdatedBy(userId);
        workflowFormEntity.setLastUpdatedOn(new Date());
        workflowFormRepository.save(workflowFormEntity);
    }

    @Override
    public WorkflowDefinitionDetail info(String modelId) {
        Optional<WorkflowDefinitionEntity> oldEntityOptional = definitionRepository.findById(modelId);
        if (!oldEntityOptional.isPresent()) {
            throw new VerificationFailedException(400, "不存在id=" + modelId + "的流程定义");
        }
        WorkflowDefinitionDetail info = new WorkflowDefinitionDetail();
        WorkflowDefinitionEntity entity = oldEntityOptional.get();
        ObjectUtil.extendObject(info, entity, true);
        info.setGraph(loadGraph(modelId));
        WorkflowFormEntity workflowFormEntity = workflowFormRepository.findByDefinitionId(modelId);
        if (workflowFormEntity != null) {
            info.setForm(workflowFormEntity.getFormContent());
        }
        return info;
    }

    @Override
    public WorkflowDefinitionDetail latest(String modelId) {
        Optional<WorkflowDefinitionEntity> oldEntityOptional = definitionRepository.findById(modelId);
        if (!oldEntityOptional.isPresent()) {
            throw new VerificationFailedException(400, "不存在id=" + modelId + "的流程定义");
        }
        WorkflowDefinitionDetail info = new WorkflowDefinitionDetail();
        WorkflowDefinitionEntity entity = oldEntityOptional.get();
        WorkflowDefinitionEntity latest = definitionRepository.latestReleased(entity.getInitialCode());
        ObjectUtil.extendObject(info, latest, true);
        info.setGraph(loadGraph(latest.getId()));
        WorkflowFormEntity workflowFormEntity = workflowFormRepository.findByDefinitionId(latest.getId());
        if (workflowFormEntity != null) {
            info.setForm(workflowFormEntity.getFormContent());
        }
        return info;
    }

    @Override
    public WorkflowDefinitionDetail latestReleased(String key) {
        WorkflowDefinitionDetail info = new WorkflowDefinitionDetail();
        WorkflowDefinitionEntity latest = definitionRepository.latestReleased(key);
        ObjectUtil.extendObject(info, latest, true);
        info.setGraph(loadGraph(latest.getId()));
        WorkflowFormEntity workflowFormEntity = workflowFormRepository.findByDefinitionId(latest.getId());
        if (workflowFormEntity != null) {
            info.setForm(workflowFormEntity.getFormContent());
        }
        return info;
    }

    @Override
    public Page<WorkflowDefinitionEntity> list(SearchDefinitionRequest request) {
        return workflowDefinitionNativeRepository.listPage(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteDraft(SaveDefinitionRequest request) {
        ValidResult validResult = ValidUtil.validFields(request,
                new String[]{"id"},
                new Object[][]{
                        {ValidUtil.REQUIRED, ValidUtil.NOT_BLANK, (ValidPlugin) (field, value) -> {
                            ValidResult v = new ValidResult();
                            if (StringUtils.isBlank(value) || definitionRepository.existsById(value)) {
                                v.valid = true;
                            } else {
                                v.message = "不存在id=" + value + "的流程定义";
                                v.valid = false;
                            }
                            return v;
                        }, (ValidPlugin) (field, value) -> {
                            ValidResult v = new ValidResult();
                            if (StringUtils.isBlank(value) || definitionRepository.isDraft(value) > 0) {
                                v.valid = true;
                            } else {
                                v.message = "id=" + value + "的流程定义,不是草稿";
                                v.valid = false;
                            }
                            return v;
                        }}
                });
        if (!validResult.valid) {
            throw new VerificationFailedException(400, validResult.message);
        }
        definitionRepository.deleteById(request.getId());
        workflowFormRepository.deleteAllByDefinitionId(request.getId());
        nodeRepository.deleteAllByModelId(request.getId());
        repositoryService.deleteModel(request.getId());
        return request.getId();
    }

    @Override
    public List<Map<String, Object>> getDefinitionList() {
        List<WorkflowDefinitionEntity> definitionList = definitionRepository.selectAllNameAndIdAndVersion();
        List<Map<String, Object>> definitions = new ArrayList<>();
        definitionList.forEach(definition -> {
            Map<String, Object> map = new HashMap<>(8);
            map.put("label", definition.getName() + " 版本：" + definition.getVersion());
            map.put("value", definition.getId());
            definitions.add(map);
        });
        return definitions;
    }

    @Override
    public NodeEntity nodeInfo(String nodeId) {
        Optional<NodeEntity> lastNodeEntityOptional = nodeRepository.findById(nodeId);
        if (!lastNodeEntityOptional.isPresent()) {
            throw new VerificationFailedException(400, "此节点数据不存在");
        }
        return lastNodeEntityOptional.get();
    }

    @Override
    public WorkflowDefinitionEntity getDefinitionEntityById(String id) {
        return definitionRepository.findById(id).orElse(null);
    }

    @Override
    public List<WorkflowDefinitionEntity> getDefinitionByIds(List<String> ids) {
        return definitionRepository.findAllByIdIn(ids);
    }

    @Override
    public String loadGraph(String modelId) {
        byte[] bpmn = repositoryService.getModelEditorSource(modelId);
        if (bpmn == null) {
            return null;
        }
        return new String(repositoryService.getModelEditorSource(modelId));
    }

    private Map<String, Object> parseProcessAttributes(BpmnModel bpmnModel) throws Exception {
        if (bpmnModel == null) {
            return null;
        }
        List<Process> ps = bpmnModel.getProcesses();
        if (ps == null || ps.size() == 0) {
            return null;
        } else {
            return BpmnUtil.parseExtensionAttributes(ps.get(0));
        }
    }

    private List<NodeEntity> parseUserTaskList(BpmnModel bpmnModel, String definitionId) throws Exception {
        List<Process> ps = bpmnModel.getProcesses();
        if (ps == null || ps.size() == 0) {
            return null;
        } else {
            List<NodeEntity> nodes = new ArrayList<>();
            for (Process process : ps) {
                Collection<FlowElement> elements = process.getFlowElements();
                if (elements != null && elements.size() > 0) {
                    for (FlowElement el : elements) {
                        if (el instanceof UserTask) {
                            UserTask task = (UserTask) el;
                            NodeEntity def = new NodeEntity();
                            def.setId(definitionId + ":" + task.getId());
                            def.setGraphId(task.getId());
                            def.setType(1);
                            def.setName(task.getName());
                            if (task.getCandidateUsers() != null && task.getCandidateUsers().size() > 0) {
                                def.setCandidateUsers(StringUtils.join(task.getCandidateUsers(), ","));
                            }
                            if (task.getCandidateGroups() != null && task.getCandidateGroups().size() > 0) {
                                def.setCandidateGroups(StringUtils.join(task.getCandidateGroups(), ","));
                            }
                            def.setDescription(task.getDocumentation());
                            List<Button> buttons = BpmnUtil.parseUserTaskButtons(task);
                            Map<String, String> taskAttributes = BpmnUtil.parseNodeAttributes(task);
                            if (buttons != null && buttons.size() > 0) {
                                def.setNoticeType(NoticeType.TASK_CARD.getState());
                                def.setNoticeContent(JSON.toJSONString(buttons));
                            } else {
                                def.setNoticeType(NoticeType.TEXT.getState());
                                def.setNoticeContent(null);
                            }
                            String timeout = taskAttributes.get("timeout");
                            if (StringUtils.isNotBlank(timeout)) {
                                def.setDueTime(timeout.trim());
                            } else {
                                def.setDueTime(null);
                            }
                            if (taskAttributes.containsKey("approvalNode") && "1".equalsIgnoreCase(taskAttributes.get("approvalNode"))) {
                                def.setApprovalNode(1);
                            } else {
                                def.setApprovalNode(0);
                            }
                            def.setSummary(BpmnUtil.parseUserTaskSummary(task));
                            nodes.add(def);
                        } else if (el instanceof CallActivity) {
                            CallActivity task = (CallActivity) el;
                            NodeEntity def = new NodeEntity();
                            def.setId(definitionId + ":" + task.getId());
                            def.setGraphId(task.getId());
                            def.setType(2);
                            def.setName(task.getName());
                            def.setDescription(task.getDocumentation());
                            nodes.add(def);
                        } else if (el instanceof ManualTask) {
                            ManualTask task = (ManualTask) el;
                            NodeEntity def = new NodeEntity();
                            def.setId(definitionId + ":" + task.getId());
                            def.setGraphId(task.getId());
                            def.setType(3);
                            def.setName(task.getName());
                            def.setDescription(task.getDocumentation());
                            nodes.add(def);
                        }
                    }
                }
            }
            return nodes;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public String deploy(String modelId, Long userId) throws Exception {
        Model model = repositoryService.getModel(modelId);
        byte[] bpmnBytes = repositoryService.getModelEditorSource(modelId);
        String deploymentId = null;
        if (bpmnBytes != null) {
            String bpmn = new String(bpmnBytes);
            Deployment deployment = repositoryService.createDeployment()
                    .addString(model.getName() + ".bpmn", bpmn)
                    .name(model.getName()).key(model.getKey()).category(model.getCategory())
                    .deploy();
            deploymentId = deployment.getId();
        } else {
            throw new DeployFailException("此流程定义[ID=" + modelId + "]没有设计图");
        }
        return deploymentId;
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchCopyNodes(String fromModelId, String modelId, List<NodeEntity> nodes, Long userId) throws Exception {
        List<NodeEntity> entities = new ArrayList<>();
        for (NodeEntity node : nodes) {
            node.setModelId(modelId);
            Optional<NodeEntity> lastNodeEntityOptional = nodeRepository.findById(fromModelId + ":" + node.getGraphId());
            NodeEntity last = null;
            node.setLastUpdatedBy(userId);
            node.setLastUpdatedOn(new Date());
            if (lastNodeEntityOptional.isPresent()) {
                last = lastNodeEntityOptional.get();
                NodeEntity now = new NodeEntity();
                ObjectUtil.extendObject(now, last, true);
                ObjectUtil.extendObject(now, node, true);
                entities.add(now);
            } else {
                node.setCreatedBy(userId);
                node.setCreatedOn(new Date());
                node.setStatus(1);
                entities.add(node);
            }
        }
        nodeRepository.saveAll(entities);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateNodes(String modelId, List<NodeEntity> nodes, Long userId) throws Exception {
        Map<String, Boolean> dic = new HashMap<>(16);
        List<NodeEntity> entities = new ArrayList<>();
        for (NodeEntity node : nodes) {
            dic.put(node.getId(), true);
            node.setModelId(modelId);
            Optional<NodeEntity> lastNodeEntityOptional = nodeRepository.findById(node.getId());
            NodeEntity last = null;
            node.setLastUpdatedBy(userId);
            node.setLastUpdatedOn(new Date());
            if (lastNodeEntityOptional.isPresent()) {
                last = lastNodeEntityOptional.get();
                ObjectUtil.extendObject(last, node, true);
                entities.add(last);
            } else {
                node.setCreatedBy(userId);
                node.setCreatedOn(new Date());
                node.setStatus(1);
                entities.add(node);
            }
        }
        nodeRepository.saveAll(entities);
        List<NodeEntity> oldList = nodeRepository.findAllByModelId(modelId);
        List<String> ids = new ArrayList<>();
        if (oldList != null && oldList.size() > 0) {
            for (NodeEntity nodeDef : oldList) {
                if (!dic.containsKey(nodeDef.getId())) {
                    ids.add(nodeDef.getId());
                }
            }
            if (ids.size() > 0) {
                nodeRepository.deleteAllByIdIn(ids);
            }
        }
    }

    private void validSaveDefinitionRequest(SaveDefinitionRequest request) throws Exception {
        ValidResult validResult = ValidUtil.validFields(request,
                new String[]{"id", "name", "description", "status", "key"},
                new Object[][]{
                        {ValidUtil.REQUIRED, ValidUtil.NOT_BLANK, (ValidPlugin) (field, value) -> {
                            ValidResult v = new ValidResult();
                            if (StringUtils.isBlank(value) || definitionRepository.existsById(value)) {
                                v.valid = true;
                            } else {
                                v.message = "不存在id=" + value + "的流程定义";
                                v.valid = false;
                            }
                            return v;
                        }},
                        {ValidUtil.NOT_BLANK, new ValidUtil.StringLength(0, 30)},
                        {new ValidUtil.StringLength(0, 100)},
                        {ValidUtil.NON_NEGATIVE_INTEGER},
                        {ValidUtil.MUST_NULL}
                });
        if (!validResult.valid) {
            throw new VerificationFailedException(400, validResult.message);
        }
    }
}

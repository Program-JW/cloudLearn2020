package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.model.entity.WorkflowHistoryEntity;
import com.ruigu.rbox.workflow.model.enums.InstanceState;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.request.SearchHistoryRequest;
import com.ruigu.rbox.workflow.repository.WorkflowDefinitionRepository;
import com.ruigu.rbox.workflow.repository.WorkflowHistoryRepository;
import com.ruigu.rbox.workflow.service.WorkflowHistoryService;
import com.ruigu.rbox.workflow.supports.ValidPlugin;
import com.ruigu.rbox.workflow.supports.ValidResult;
import com.ruigu.rbox.workflow.supports.ValidUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * 流程历史实现
 *
 * @author alan.zhao
 */
@Service
public class WorkflowHistoryServiceImpl implements WorkflowHistoryService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private WorkflowDefinitionRepository definitionRepository;

    @Resource
    private WorkflowHistoryRepository workflowHistoryRepository;


    @Override
    public Page<Map<String, Object>> search(SearchHistoryRequest request) {
        ValidResult validResult = ValidUtil.validFields(request,
                new String[]{"name", "definitionId", "status", "pageIndex", "pageSize"},
                new Object[][]{
                        {new ValidUtil.StringLength(0, 30)},
                        {(ValidPlugin) (field, value) -> {
                            ValidResult v = new ValidResult();
                            if (StringUtils.isBlank(value) || definitionRepository.existsById(value)) {
                                v.valid = true;
                            } else {
                                v.message = "不存在id=" + value + "的流程定义";
                                v.valid = false;
                            }
                            return v;
                        }},
                        {ValidUtil.NON_NEGATIVE_INTEGER},
                        {ValidUtil.REQUIRED, ValidUtil.NON_NEGATIVE_INTEGER},
                        {ValidUtil.REQUIRED, ValidUtil.NON_NEGATIVE_INTEGER}
                });
        if (!validResult.valid) {
            throw new VerificationFailedException(400, validResult.message);
        }
        String whereSql = " where 1=1 ";
        if (request.getStatus() != null) {
            whereSql += " and history.`status` =" + request.getStatus() + "\n";
        }
        if (StringUtils.isNotBlank(request.getName())) {
            whereSql += " and history.`name` like '%" + request.getName() + "%'\n";
        }
        if (StringUtils.isNotBlank(request.getDefinitionId())) {
            whereSql += " and history.definition_id = '" + request.getDefinitionId() + "'\n";
        }
        String sqlList = "select history.id ,\n" +
                "       history.`name`,\n" +
                "       history.business_key           businessKey,\n" +
                "       history.definition_id          definitionId,\n" +
                "       history.definition_version     definitionVersion,\n" +
                "       history.definition_code        definitionCode,\n" +
                "       history.start_time             startTime,\n" +
                "       history.end_time               endTime,\n" +
                "       history.created_on             createdOn,\n" +
                "       history.created_by             createdBy,\n" +
                "       history.last_updated_on        lastUpdatedOn,\n" +
                "       history.last_updated_by        lastUpdatedBy,\n" +
                "       history.`status`\n" +
                " from workflow_history history" + whereSql;
        String sqlCount = "select count(*) from workflow_history history" + whereSql;
        Query query = em.createNativeQuery(sqlList);
        if (request.getPageIndex() < 0) {
            request.setPageIndex(0);
        }
        query.setFirstResult((request.getPageIndex()) * request.getPageSize());
        query.setMaxResults(request.getPageSize());
        List<Map<String, Object>> list = (List<Map<String, Object>>) query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).getResultList();
        Page<Map<String, Object>> result = new PageImpl<>(list, PageRequest.of(request.getPageIndex(), request.getPageSize()), count(sqlCount));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WorkflowHistoryEntity abandonedHistoryInstance(String definitionKey, String businessKey) {
        Integer abandonedStatus = InstanceState.INVALID.getState();
        WorkflowHistoryEntity historyEntity = workflowHistoryRepository.findByDefinitionCodeAndBusinessKeyAndStatusNot(definitionKey, businessKey, abandonedStatus);
        if (historyEntity == null) {
            throw new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "没有查询到未作废的历史流程");
        }
        historyEntity.setStatus(abandonedStatus);
        workflowHistoryRepository.save(historyEntity);
        return historyEntity;
    }

    public long count(String sql) {
        Query query = em.createNativeQuery(sql);
        return Long.valueOf(query.getSingleResult().toString());
    }
}

package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.model.entity.WorkOrderEntity;
import com.ruigu.rbox.workflow.model.enums.WorkOrderState;
import com.ruigu.rbox.workflow.model.request.CreateWorkOrderRequest;
import com.ruigu.rbox.workflow.model.request.StartInstanceRequest;
import com.ruigu.rbox.workflow.repository.WorkOrderRepository;
import com.ruigu.rbox.workflow.service.WorkOrderService;
import com.ruigu.rbox.workflow.service.WorkflowInstanceService;
import com.ruigu.rbox.workflow.supports.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * @author alan.zhao
 */
@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    WorkflowInstanceService workflowInstanceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long create(CreateWorkOrderRequest request, Long userId) throws Exception {
        WorkOrderEntity entity = new WorkOrderEntity();
        entity.setStatus(WorkOrderState.TO_BE_PROCESSED.getState());
        entity.setCreatedBy(userId);
        entity.setCreatedOn(new Date());
        entity.setLastUpdatedBy(userId);
        entity.setLastUpdatedOn(new Date());
        workOrderRepository.save(entity);
        StartInstanceRequest startInstanceRequest = new StartInstanceRequest();
        startInstanceRequest.setBusinessKey(entity.getId().toString());
        startInstanceRequest.setName(entity.getTitle());
        if (request.getOwnerId() != null) {
            startInstanceRequest.setOwnerId(request.getOwnerId());
        } else {
            startInstanceRequest.setOwnerId(userId);
        }
        workflowInstanceService.start(startInstanceRequest, userId);
        entity.setStatus(WorkOrderState.PROCESSING.getState());
        entity.setLastUpdatedBy(userId);
        entity.setLastUpdatedOn(new Date());
        workOrderRepository.save(entity);
        return entity.getId();
    }

    @Override
    public void close(Long id, Long userId) throws Exception {
        WorkOrderEntity entity = new WorkOrderEntity();
        entity.setId(id);
        entity.setStatus(WorkOrderState.CLOSED.getState());
        entity.setLastUpdatedBy(userId);
        entity.setLastUpdatedOn(new Date());
        workOrderRepository.save(entity);
    }

    @Override
    public void delete(Long id, Long userId) throws Exception {
        WorkOrderEntity entity = new WorkOrderEntity();
        entity.setId(id);
        entity.setStatus(WorkOrderState.INVALID.getState());
        entity.setLastUpdatedBy(userId);
        entity.setLastUpdatedOn(new Date());
        workOrderRepository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveIgnoreNull(WorkOrderEntity entity, Long userId) throws Exception {
        Optional<WorkOrderEntity> optional = workOrderRepository.findById(entity.getId());
        if (optional.isPresent()) {
            WorkOrderEntity last = optional.get();
            ObjectUtil.extendObject(last, entity, true);
            workOrderRepository.save(last);
        } else {
            throw new VerificationFailedException(400, "数据不存在");
        }
    }
}

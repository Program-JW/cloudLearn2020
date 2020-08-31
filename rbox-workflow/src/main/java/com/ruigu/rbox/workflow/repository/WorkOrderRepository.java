package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.WorkOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author alan.zhao
 */
public interface WorkOrderRepository extends JpaRepository<WorkOrderEntity, Long>, JpaSpecificationExecutor<WorkOrderEntity> {
}

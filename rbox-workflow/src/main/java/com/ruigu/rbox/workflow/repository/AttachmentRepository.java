package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author alan.zhao
 */
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long>, JpaSpecificationExecutor<AttachmentEntity> {
}

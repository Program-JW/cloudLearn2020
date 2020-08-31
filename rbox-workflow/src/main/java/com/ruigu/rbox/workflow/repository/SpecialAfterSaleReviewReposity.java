package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleReviewEntity;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleReviewPositionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpecialAfterSaleReviewReposity extends JpaRepository<SpecialAfterSaleReviewEntity,Long >, JpaSpecificationExecutor<SpecialAfterSaleReviewPositionEntity> {



        Page<SpecialAfterSaleReviewEntity> findAllByStatus(Integer status, Pageable pageable);

}

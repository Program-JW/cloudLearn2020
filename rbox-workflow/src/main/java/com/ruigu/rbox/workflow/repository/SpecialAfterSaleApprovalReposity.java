package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleReviewNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialAfterSaleApprovalReposity extends JpaRepository<SpecialAfterSaleReviewNodeEntity,Integer> {
}

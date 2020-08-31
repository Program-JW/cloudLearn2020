package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleApplyEntity;
import com.ruigu.rbox.workflow.repository.custom.CustomSpecialAfterSaleApplyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author liqingtian
 * @date 2020/08/11 13:00
 */
public interface SpecialAfterSaleApplyRepository extends JpaRepository<SpecialAfterSaleApplyEntity, Long>, CustomSpecialAfterSaleApplyRepository {
    Page<SpecialAfterSaleApplyEntity> findAllByCreatedBy(Integer createBy, Pageable pageable);
}

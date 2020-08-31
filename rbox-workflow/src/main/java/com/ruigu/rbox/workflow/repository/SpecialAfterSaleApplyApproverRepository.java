package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleApplyApproverEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/13 1:05
 */
public interface SpecialAfterSaleApplyApproverRepository extends JpaRepository<SpecialAfterSaleApplyApproverEntity, Long> {

    void deleteByApplyId(Long applyId);

    List<SpecialAfterSaleApplyApproverEntity> findAllByApplyId(Long applyId);
}

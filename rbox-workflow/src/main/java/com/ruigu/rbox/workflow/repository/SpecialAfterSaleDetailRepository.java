package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/11 13:00
 */
public interface SpecialAfterSaleDetailRepository extends JpaRepository<SpecialAfterSaleDetailEntity, Long> {

    /**
     * 查询详情信息
     *
     * @param applyId 申请id
     * @return 详情
     */
    List<SpecialAfterSaleDetailEntity> findAllByApplyId(Long applyId);
}

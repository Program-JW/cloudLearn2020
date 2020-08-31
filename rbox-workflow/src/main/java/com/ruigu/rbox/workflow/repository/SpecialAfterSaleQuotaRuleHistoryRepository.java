package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleQuotaRuleHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/12 20:29
 */
@Repository
public interface SpecialAfterSaleQuotaRuleHistoryRepository extends JpaRepository<SpecialAfterSaleQuotaRuleHistoryEntity, Integer> {


}

package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleQuotaRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/12 20:29
 */
@Repository
public interface SpecialAfterSaleQuotaRuleRepository extends JpaRepository<SpecialAfterSaleQuotaRuleEntity, Integer> {


}

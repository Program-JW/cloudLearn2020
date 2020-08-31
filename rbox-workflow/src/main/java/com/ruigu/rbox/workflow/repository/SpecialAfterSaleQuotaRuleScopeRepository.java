package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleQuotaRuleScopeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/14 18:14
 */
public interface SpecialAfterSaleQuotaRuleScopeRepository extends JpaRepository<SpecialAfterSaleQuotaRuleScopeEntity, Integer> {

    /**
     * 通过额度配置编号删除
     * @param ruleId
     */
    void deleteByRuleId(Integer ruleId);

}

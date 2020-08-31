package com.ruigu.rbox.workflow.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruigu.rbox.workflow.model.entity.QSpecialAfterSaleQuotaEntity;
import com.ruigu.rbox.workflow.model.entity.QSpecialAfterSaleQuotaRuleEntity;
import com.ruigu.rbox.workflow.model.entity.QSpecialAfterSaleQuotaRuleScopeEntity;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleQuotaEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/13 17:48
 */
public class CustomSpecialAfterSaleQuotaRepositoryImpl implements CustomSpecialAfterSaleQuotaRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    private final QSpecialAfterSaleQuotaEntity qQuota = QSpecialAfterSaleQuotaEntity.specialAfterSaleQuotaEntity;

    private final QSpecialAfterSaleQuotaRuleEntity qQuotaRule = QSpecialAfterSaleQuotaRuleEntity.specialAfterSaleQuotaRuleEntity;

    private final QSpecialAfterSaleQuotaRuleScopeEntity qQuotaRuleScope = QSpecialAfterSaleQuotaRuleScopeEntity.specialAfterSaleQuotaRuleScopeEntity;

    @Override
    public List<SpecialAfterSaleQuotaEntity> queryLastUpdatedRule() {

        return queryFactory.select(
                Projections.bean(SpecialAfterSaleQuotaEntity.class,
                        qQuotaRuleScope.groupType,
                        qQuotaRuleScope.groupId,
                        qQuotaRuleScope.type,
                        qQuotaRuleScope.createdAt,
                        qQuotaRuleScope.createdBy,
                        qQuotaRule.commonCoefficient,
                        qQuotaRule.nonCommonCoefficient,
                        qQuotaRule.lastUpdateAt,
                        qQuotaRule.lastUpdateBy))
                .from(qQuotaRuleScope)
                .leftJoin(qQuotaRule).on(qQuotaRuleScope.ruleId.eq(qQuotaRule.id))
                .fetch();
    }
}

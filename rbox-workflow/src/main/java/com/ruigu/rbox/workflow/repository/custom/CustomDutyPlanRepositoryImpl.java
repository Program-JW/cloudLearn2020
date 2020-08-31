package com.ruigu.rbox.workflow.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruigu.rbox.workflow.model.entity.DutyPlanEntity;
import com.ruigu.rbox.workflow.model.entity.QDutyPlanEntity;
import com.ruigu.rbox.workflow.model.entity.QDutyRuleEntity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liqingtian
 * @date 2020/08/03 11:13
 */
public class CustomDutyPlanRepositoryImpl implements CustomDutyPlanRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    private final QDutyPlanEntity qDutyPlan = QDutyPlanEntity.dutyPlanEntity;

    @Override
    public DutyPlanEntity queryTechnicalLatestDutyPlan(Integer scopeType, Integer status) {

        QDutyRuleEntity qDutyRule = QDutyRuleEntity.dutyRuleEntity;

        return queryFactory.select(qDutyPlan).from(qDutyPlan)
                .leftJoin(qDutyRule).on(qDutyPlan.ruleId.eq(qDutyRule.id))
                .where(qDutyRule.scopeType.eq(scopeType).and(qDutyRule.status.eq(status)))
                .orderBy(qDutyPlan.dutyDate.desc())
                .limit(1).fetchFirst();
    }
}

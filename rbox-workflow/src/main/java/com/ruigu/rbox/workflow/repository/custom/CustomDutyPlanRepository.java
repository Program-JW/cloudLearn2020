package com.ruigu.rbox.workflow.repository.custom;

import com.querydsl.core.QueryResults;
import com.ruigu.rbox.workflow.model.entity.DutyPlanEntity;

/**
 * @author liqingtian
 * @date 2020/08/03 11:10
 */
public interface CustomDutyPlanRepository {

    /**
     * 查询技术部值班计划最后一条
     *
     * @param scopeType 是否技术部计划
     * @param status    状态
     */
    DutyPlanEntity queryTechnicalLatestDutyPlan(Integer scopeType, Integer status);
}

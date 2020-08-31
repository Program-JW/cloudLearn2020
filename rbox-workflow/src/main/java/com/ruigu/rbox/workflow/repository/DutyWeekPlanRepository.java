package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.DutyWeekPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/06/05 11:36
 */
public interface DutyWeekPlanRepository extends JpaRepository<DutyWeekPlanEntity, Integer> {

    /**
     * 通过策略id查询值班计划
     *
     * @param ruleId 策略id
     * @return 计划
     */
    List<DutyWeekPlanEntity> findAllByRuleId(Integer ruleId);

    /**
     * 查询值班计划
     *
     * @param ruleId    策略id
     * @param dayOfWeek 周几
     * @return 计划
     */
    DutyWeekPlanEntity findByRuleIdAndDayOfWeek(Integer ruleId, Integer dayOfWeek);

    /**
     * 查询值班计划
     *
     * @param dayOfWeek 周几
     * @return 计划
     */
    @Query("select w from DutyWeekPlanEntity w " +
            " left join DutyRuleEntity r on w.ruleId = r.id " +
            " where r.status = 1 and w.dayOfWeek = :dayOfWeek ")
    List<DutyWeekPlanEntity> findAllEnableByDayOfWeek(@Param("dayOfWeek") Integer dayOfWeek);
}

package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.DutyPlanEntity;
import com.ruigu.rbox.workflow.repository.custom.CustomDutyPlanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/05/08 15:00
 */
public interface DutyPlanRepository extends JpaRepository<DutyPlanEntity, Integer>, CustomDutyPlanRepository {


    /**
     * 获取某规则的值班表（按天值班）
     *
     * @param ruleId 规则id
     * @return 值班表
     */
    List<DutyPlanEntity> findAllByRuleIdOrderByDutyDateDesc(Integer ruleId);

    /**
     * 获取某规则的值班表（按天值班）
     *
     * @param ruleId 规则id
     * @return 值班表
     */
    List<DutyPlanEntity> findAllByRuleId(Integer ruleId);

    /**
     * 获取某规则的值班表（按天值班）
     *
     * @param ruleIds 规则id
     * @return 值班表
     */
    List<DutyPlanEntity> findAllByRuleIdIn(List<Integer> ruleIds);

    /**
     * 获取某规则的值班表（按天值班）
     *
     * @param ruleId   规则id
     * @param dutyDate 值班日期
     * @return 值班表
     */
    DutyPlanEntity findByRuleIdAndDutyDate(Integer ruleId, LocalDateTime dutyDate);

    /**
     * 获取值班表 （分页）
     *
     * @param ruleId   策略id
     * @param pageable 分页
     * @return 值班表
     */
    Page<DutyPlanEntity> findAllByRuleIdOrderByDutyDateDesc(Integer ruleId, Pageable pageable);

    /**
     * 查询每个分类
     *
     * @param dutyDate 值班日期
     * @return 值班数据
     */
    @Query("select p " +
            " from DutyPlanEntity p left join DutyRuleEntity r on p.ruleId = r.id " +
            " where r.status = 1 and p.dutyDate = :dutyDate ")
    List<DutyPlanEntity> findAllByDutyDate(@Param("dutyDate") LocalDateTime dutyDate);

    /**
     * 查询值班计划
     *
     * @param ruleId   策略id
     * @param dutyDate 值班日期
     * @return 值班
     */
    @Query("select p " +
            " from DutyPlanEntity p left join DutyRuleEntity r on r.id = p.ruleId" +
            " where r.status = 1 and p.ruleId = :ruleId and p.dutyDate = :dutyDate")
    DutyPlanEntity findByEnableRuleIdAndDutyDate(@Param("ruleId") Integer ruleId, @Param("dutyDate") LocalDateTime dutyDate);


    /**
     * 查询技术部当天值班人
     *
     * @param scopeType 技术部
     * @param dutyDate  值班日期
     * @param status    状态
     * @return 值班计划
     */
    @Query("select p from DutyPlanEntity p " +
            " left join DutyRuleEntity r on p.ruleId = r.id " +
            " where r.scopeType = :scopeType and r.status = :status and p.dutyDate = :dutyDate")
    DutyPlanEntity findTechnicalTodayDutyUser(@Param("scopeType") Integer scopeType, @Param("dutyDate") LocalDateTime dutyDate, @Param("status") Integer status);

    /**
     * 通过问题分类查询当天值班人 （按天值班）
     *
     * @param categoryId 分类id
     * @param dutyDate   值班日期
     * @return 当天值班人
     */
    @Query("select p from DutyPlanEntity p " +
            " left join LightningIssueCategoryEntity c on c.ruleId = p.ruleId " +
            " where c.id = :categoryId and c.status = 1 and p.dutyDate = :dutyDate ")
    DutyPlanEntity findByCategoryIdAndDutyDate(@Param("categoryId") Integer categoryId, @Param("dutyDate") LocalDateTime dutyDate);

    /**
     * 通过策略删除值班计划
     *
     * @param ruleId 策略id
     */
    void deleteByRuleId(Integer ruleId);

    /**
     * 删除通过id
     *
     * @param ids id
     */
    void deleteAllByIdIn(List<Integer> ids);

    /**
     * 查询今天以后的值班计划
     *
     * @param ruleId 策略id
     * @param date   日期
     * @return 值班计划
     */
    List<DutyPlanEntity> findAllByRuleIdAndDutyDateAfter(Integer ruleId, LocalDateTime date);
}


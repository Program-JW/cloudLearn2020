package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.DutyRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/05/08 13:18
 */
public interface DutyRuleRepository extends JpaRepository<DutyRuleEntity, Integer> {

    /**
     * 修改策略状态
     *
     * @param id     id
     * @param status 状态
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update DutyRuleEntity set status = :status where id = :id ")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    /**
     * 查询启用中的策略
     *
     * @param status 状态
     * @return 策略列表（启用中）
     */
    List<DutyRuleEntity> findAllByStatus(@Param("status") Integer status);

    /**
     * 查询启用中的策略
     *
     * @param id     id
     * @param status 状态
     * @return 策略列表（启用中）
     */
    DutyRuleEntity findByIdAndStatus(Integer id, Integer status);

    /**
     * 查询所有策略
     *
     * @param ruleName 策略名称
     * @return 策略列表
     */
    @Query("from DutyRuleEntity where name like :ruleName or :ruleName is null ")
    List<DutyRuleEntity> findAllByRuleNameLike(@Param("ruleName") String ruleName);

    /**
     * 根据问题分类id和类型查询策略
     *
     * @param categoryId 问题类型id
     * @param type       类型
     * @return 策略
     */
    @Query("select r " +
            " from DutyRuleEntity r left join LightningIssueCategoryEntity c on r.id = c.ruleId " +
            " where c.id = :categoryId and r.status = 1 and r.type = :type ")
    DutyRuleEntity findByCategoryIdAndType(@Param("categoryId") Integer categoryId, @Param("type") Integer type);

    /**
     * 根据问题分类id查询策略
     *
     * @param categoryId 问题类型id
     * @return 策略
     */
    @Query("select r " +
            " from DutyRuleEntity r left join LightningIssueCategoryEntity c on r.id = c.ruleId " +
            " where c.id = :categoryId and r.status = 1 ")
    DutyRuleEntity findByCategoryId(@Param("categoryId") Integer categoryId);

    /**
     * 查询策略
     *
     * @param scopeType  问题类型id
     * @param preDefined 是否提前设定
     * @return 策略
     */
    DutyRuleEntity findByScopeTypeAndIsPreDefined(Integer scopeType, Integer preDefined);
}

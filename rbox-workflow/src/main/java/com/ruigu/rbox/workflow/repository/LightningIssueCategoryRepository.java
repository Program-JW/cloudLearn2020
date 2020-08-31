package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.LightningIssueCategoryEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 闪电链问题分类DAO
 *
 * @author alan.zhao
 */
public interface LightningIssueCategoryRepository extends JpaRepository<LightningIssueCategoryEntity, Integer>, JpaSpecificationExecutor<LightningIssueCategoryEntity> {
    List<LightningIssueCategoryEntity> findAllByStatusEqualsOrderBySortDesc(Integer status);

    /**
     * 获取分类名称
     *
     * @param categoryName 模糊匹配
     * @return 结果
     */
    @Query("from LightningIssueCategoryEntity c where c.name like :categoryName or :categoryName is null ")
    List<LightningIssueCategoryEntity> findAllByCategoryName(@Param("categoryName") String categoryName);

    /***
     * 修改状态
     * @param id id
     * @param status 状态
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update LightningIssueCategoryEntity set status = :status where id =:id ")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    /**
     * 通过策略查问题分类
     *
     * @param ruleId 策略id
     * @param status 状态
     * @return 问题分类
     */
    List<LightningIssueCategoryEntity> findAllByRuleIdAndStatus(Integer ruleId, Integer status);

    /**
     * 查询分类通过策略id和状态
     *
     * @param ruleIds 策略id列表
     * @param status  状态
     * @return 分类
     */
    List<LightningIssueCategoryEntity> findAllByRuleIdInAndStatus(Collection<Integer> ruleIds, Integer status);
}

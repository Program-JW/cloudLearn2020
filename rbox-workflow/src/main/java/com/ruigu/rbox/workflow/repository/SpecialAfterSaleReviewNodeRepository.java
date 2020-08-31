package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleReviewNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * @author liqingtian
 * @date 2020/08/11 14:47
 */
public interface SpecialAfterSaleReviewNodeRepository extends JpaRepository<SpecialAfterSaleReviewNodeEntity, Integer> {

    /**
     * 获取第一个配置节点
     *
     * @param configId 配置id
     * @return 配置节点信息
     */
    SpecialAfterSaleReviewNodeEntity findTopByConfigIdOrderBySort(Integer configId);

    /**
     * 获取所有配置节点
     *
     * @param configId 配置id
     * @return 配置节点信息
     */
    List<SpecialAfterSaleReviewNodeEntity> findAllByConfigIdOrderBySort(Integer configId);

    /**
     * @param status
     * @param configId
     * @return
     */
    @Modifying
    @Query(value = "update SpecialAfterSaleReviewNodeEntity set status = :status where configId = :configId ")
    Integer updateReviewNodeStatus(Integer status, Integer configId);

}


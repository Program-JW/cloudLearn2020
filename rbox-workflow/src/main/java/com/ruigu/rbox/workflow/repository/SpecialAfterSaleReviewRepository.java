package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author liqingtian
 * @date 2020/08/11 14:47
 */
public interface SpecialAfterSaleReviewRepository extends JpaRepository<SpecialAfterSaleReviewEntity, Integer> {

    /**
     * 根据状态查找所有配置
     *
     * @param status 状态
     * @return 配置信息
     */
    List<SpecialAfterSaleReviewEntity> findAllByStatusOrderByIdDesc(Integer status);

    /**
     * 更新审批人列表的抄送人列表
     *
     * @param ccIdList
     * @param id
     */
    @Query("update SpecialAfterSaleReviewEntity E set E.ccIds = :ccIdList where E.id = :id")
    void updateApproverConfigCcIdList(String ccIdList, Integer id);

    @Modifying
    @Query(value="update SpecialAfterSaleReviewEntity set status=:status where id=:id")
    Integer updateReviewStatus(Integer status,Integer id);

}

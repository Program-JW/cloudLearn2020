package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleQuotaTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author liqingtian
 * @date 2020/08/11 13:00
 */
public interface SpecialAfterSaleQuotaTransactionRepository extends JpaRepository<SpecialAfterSaleQuotaTransactionEntity, Long> {

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update SpecialAfterSaleQuotaTransactionEntity set status = :status where applyId = :applyId")
    void updateStatusByApplyId(@Param("applyId") Long applyId, @Param("status") Integer status);

    @Query(" select sum(t.amount) " +
            " from SpecialAfterSaleQuotaTransactionEntity t " +
            " where t.groupId = :groupId and t.groupType = :groupType and t.yM = :yM and t.status in (0,1) and t.type = :type" +
            " group by t.groupId ")
    BigDecimal queryDeductionQuota(@Param("yM") Integer yM, @Param("groupType") Integer groupType, @Param("groupId") Integer groupId, @Param("type") Integer type);

    @Query(" select sum(t.amount) " +
            " from SpecialAfterSaleQuotaTransactionEntity t " +
            " where t.yM = :yM and t.status in (0,1) and t.quotaId = :quotaId")
    BigDecimal queryDeductionQuota(@Param("yM") Integer yM, @Param("quotaId") Integer quotaId);
}

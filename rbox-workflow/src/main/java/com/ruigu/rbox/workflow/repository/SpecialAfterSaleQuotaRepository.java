package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleQuotaEntity;
import com.ruigu.rbox.workflow.repository.custom.CustomSpecialAfterSaleQuotaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author liqingtian
 * @date 2020/08/12 19:52
 */
public interface SpecialAfterSaleQuotaRepository extends JpaRepository<SpecialAfterSaleQuotaEntity, Integer>, CustomSpecialAfterSaleQuotaRepository {

    /**
     * 查询初始化额度
     *
     * @param yM        年月
     * @param groupType 组类型
     * @param groupId   组id
     * @param type      类型
     */
    SpecialAfterSaleQuotaEntity findByYMAndGroupTypeAndGroupIdAndType(Integer yM, Integer groupType, Integer groupId, Integer type);

}

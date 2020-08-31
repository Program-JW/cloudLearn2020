package com.ruigu.rbox.workflow.repository.custom;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleQuotaEntity;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/13 17:40
 */
public interface CustomSpecialAfterSaleQuotaRepository {

    /**
     * 查询最新修改记录
     *
     * @return 最新修改记录
     */
    List<SpecialAfterSaleQuotaEntity> queryLastUpdatedRule();
}

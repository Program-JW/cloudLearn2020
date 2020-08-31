package com.ruigu.rbox.workflow.repository.custom;

import com.querydsl.core.QueryResults;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleApplyEntity;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleSearchRequest;

/**
 * @author liqingtian
 * @date 2020/08/12 22:38
 */
public interface CustomSpecialAfterSaleApplyRepository {

    /**
     * 查询已审批
     *
     * @param request 请求
     * @return 查询结果
     */
    QueryResults<SpecialAfterSaleApplyEntity> queryListMyApproved(SpecialAfterSaleSearchRequest request);

    /**
     * 查询待审批
     *
     * @param request 请求
     * @return 查询结果
     */
    QueryResults<SpecialAfterSaleApplyEntity> queryListMyPendingApprove(SpecialAfterSaleSearchRequest request);
}

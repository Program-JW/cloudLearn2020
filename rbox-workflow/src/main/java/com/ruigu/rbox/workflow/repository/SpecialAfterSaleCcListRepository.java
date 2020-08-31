package com.ruigu.rbox.workflow.repository;

import com.querydsl.core.QueryResults;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleApplyEntity;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleSearchRequest;

/**
 * @author liqingtian
 * @date 2020/08/11 13:00
 */
public interface SpecialAfterSaleCcListRepository {

    /**
     * 分页查询抄送我列表
     *
     * @param req    请求参数
     * @param userId 用户id
     * @return 抄送我列表
     */
    QueryResults<SpecialAfterSaleApplyEntity> queryMyCcApplyList(SpecialAfterSaleSearchRequest req, Integer userId);
}

package com.ruigu.rbox.workflow.repository.custom;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.model.entity.QSpecialAfterSaleApplyEntity;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleApplyEntity;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.SpecialAfterSaleApplyStatusEnum;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleApplyExportRequest;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleApplyRequest;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

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

    /**
     * 分页查询特殊售后列表
     *
     * @param req 请求参数
     * @return 返回参数
     */
    QueryResults<SpecialAfterSaleApplyEntity> queryAfterSaleListByPage(SpecialAfterSaleApplyRequest req);

    /**
     * 查询所有特殊售后列表
     *
     * @param req 请求参数
     * @return 返回参数
     */
    List<SpecialAfterSaleApplyEntity> queryAllAfterSaleList(SpecialAfterSaleApplyExportRequest req);


    /**
     * 更新申请表状态
     *
     * @param status  状态
     * @param applyId 申请id
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    void updateAfterSaleApplyStatus(Integer status, Long applyId);
}

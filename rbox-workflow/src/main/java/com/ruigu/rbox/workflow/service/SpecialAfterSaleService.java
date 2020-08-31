package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.cloud.kanai.web.page.PageImpl;
import com.ruigu.rbox.workflow.model.dto.SpecialAfterSaleApplyMySubmmitDTO;
import com.ruigu.rbox.workflow.model.request.*;
import com.ruigu.rbox.workflow.model.request.lightning.AddSpecialAfterSaleApplyRequest;
import com.ruigu.rbox.workflow.model.vo.SpecialAfterSaleApplyRecordVO;
import com.ruigu.rbox.workflow.model.vo.SpecialAfterSaleDetailApplyPcVO;
import com.ruigu.rbox.workflow.model.vo.SpecialAfterSaleDetailApplyVO;
import com.ruigu.rbox.workflow.model.vo.SpecialAfterSaleSimpleApplyVO;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/11 14:52
 */
public interface SpecialAfterSaleService {

    /**
     * 提交申请
     *
     * @param request 请求参数
     */
    void apply(AddSpecialAfterSaleApplyRequest request);

    /**
     * 查询我已审批列表
     *
     * @param request 请求
     * @return 我受理列表
     */
    Page<SpecialAfterSaleSimpleApplyVO> queryListMyApproved(SpecialAfterSaleSearchRequest request);

    /**
     * 查询我待审批列表
     *
     * @param request 请求
     * @return 我受理列表
     */
    Page<SpecialAfterSaleSimpleApplyVO> queryListMyPendingApproval(SpecialAfterSaleSearchRequest request);

    /**
     * 查询详情
     *
     * @param applyId 申请id
     * @return 详情
     */
    SpecialAfterSaleDetailApplyVO detail(Long applyId);

    /**
     * 详情 pc
     *
     * @param applyId 申请id
     * @return 详情pc
     */
    SpecialAfterSaleDetailApplyPcVO pcDetail(Long applyId);

    /**
     * 审批
     */
    void submit(SpecialAfterSaleApprovalRequest request) throws Exception;

    /**
     * 转审
     */
    void transfer(SpecialAfterSaleTransferRequest request) throws Exception;

    /**
     * 电销主管转接
     */
    void routingAndStart(SpecialAfterSaleDxTransferRequest request);

    /**
     * 催办特殊售后申请
     *
     * @param applyId 申请编号
     */
    void urgeSpecialSaleApply(Long applyId);

    /**
     * 取消特殊售后审批
     *
     * @param applyId 申请编号
     */
    void cancelSpecialSaleApply(Long applyId);

    /**
     * 查询抄送我的列表
     *
     * @param req 请求参数
     * @return 抄送我的列表
     */
    Page<SpecialAfterSaleSimpleApplyVO> queryMyCcApplyList(SpecialAfterSaleSearchRequest req);

    /**
     * 查询特殊售后审批记录
     *
     * @param req 请求参数
     * @return 特殊售后审批列表
     */
    PageImpl<SpecialAfterSaleApplyRecordVO> queryAfterSaleList(SpecialAfterSaleApplyRequest req);

    /**
     * 导出所有特殊售后记录
     *
     * @param req 请求参数
     */
    List<SpecialAfterSaleApplyRecordVO> exportAfterSaleApplyList(SpecialAfterSaleApplyExportRequest req);

    /**
     * 查询我提交的申请列表
     *
     * @param request
     * @return
     */
    Page<SpecialAfterSaleSimpleApplyVO> findAllByCreatedBy(SpecialAfterSaleSearchRequest request);
}

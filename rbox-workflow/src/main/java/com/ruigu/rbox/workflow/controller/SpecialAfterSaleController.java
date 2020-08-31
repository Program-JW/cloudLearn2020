package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.request.*;
import com.ruigu.rbox.workflow.model.request.lightning.AddSpecialAfterSaleApplyRequest;
import com.ruigu.rbox.workflow.model.vo.SpecialAfterSaleDetailApplyVO;
import com.ruigu.rbox.workflow.model.vo.SpecialAfterSaleSimpleApplyVO;
import com.ruigu.rbox.workflow.service.SpecialAfterSaleService;
import com.ruigu.rbox.workflow.supports.ExportUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author liqingtian
 * @date 2020/08/10 12:47
 */
@Slf4j
@Api(value = "特殊售后审批API", tags = {"特殊售后审批API"})
@RestController
@RequestMapping("/special-after-sale")
public class SpecialAfterSaleController {

    @Resource
    private SpecialAfterSaleService specialAfterSaleService;

    @ApiOperation(value = "特殊售后审批详情接口", notes = "特殊售后审批详情接口")
    @GetMapping("/{applyId}")
    public ServerResponse<SpecialAfterSaleDetailApplyVO> detail(@PathVariable(value = "applyId", required = true) Long applyId) {
        return ServerResponse.ok(specialAfterSaleService.detail(applyId));
    }

    @ApiOperation(value = "我的待审批列表接口", notes = "我待审批列表接口")
    @GetMapping("/my-pending-approval")
    public ServerResponse<Page<SpecialAfterSaleSimpleApplyVO>> myPendingApprovalList(@Valid SpecialAfterSaleSearchRequest request) {
        return ServerResponse.ok(specialAfterSaleService.queryListMyPendingApproval(request));
    }

    @ApiOperation(value = "我的已审批列表接口", notes = "我的已审批列表接口")
    @GetMapping("/my-approved")
    public ServerResponse<Page<SpecialAfterSaleSimpleApplyVO>> myApprovedList(@Valid SpecialAfterSaleSearchRequest request) {
        return ServerResponse.ok(specialAfterSaleService.queryListMyApproved(request));
    }

    @ApiOperation(value = "审批", notes = "审批")
    @PostMapping("/submit")
    public ServerResponse submit(@Valid @RequestBody SpecialAfterSaleApprovalRequest request) throws Exception {
        specialAfterSaleService.submit(request);
        return ServerResponse.ok();
    }

    @ApiOperation(value = "转审", notes = "转审")
    @PostMapping("/transfer")
    public ServerResponse transfer(@Valid @RequestBody SpecialAfterSaleTransferRequest request) throws Exception {
        specialAfterSaleService.transfer(request);
        return ServerResponse.ok();
    }

    @ApiOperation(value = "电销主管转审", notes = "电销主管转审")
    @PostMapping("/dx-manager/transfer")
    public ServerResponse dxTransfer(@Valid @RequestBody SpecialAfterSaleDxTransferRequest request) throws Exception {
        specialAfterSaleService.routingAndStart(request);
        return ServerResponse.ok();
    }

    @ApiOperation(value = "催办接口", notes = "催办接口")
    @PostMapping("/urge/{applyId}")
    public ServerResponse urgeSpecialSaleApply(@PathVariable(value = "applyId", required = true) Long applyId) {
        specialAfterSaleService.urgeSpecialSaleApply(applyId);
        return ServerResponse.ok();
    }

    @ApiOperation(value = "撤销申请接口", notes = "撤销申请接口")
    @PostMapping("/cancel/{applyId}")
    public ServerResponse cancelSpecialSaleApply(@PathVariable(value = "applyId", required = true) Long applyId) {
        specialAfterSaleService.cancelSpecialSaleApply(applyId);
        return ServerResponse.ok();
    }

    @ApiOperation(value = "查询抄送我列表", notes = "查询抄送列表")
    @PostMapping("/cc-apply-list")
    public ServerResponse<Page<SpecialAfterSaleSimpleApplyVO>> queryCopyApproverList(@Valid @RequestBody SpecialAfterSaleSearchRequest req) {
        return ServerResponse.ok(specialAfterSaleService.queryMyCcApplyList(req));
    }

    @ApiOperation(value = "提交特殊售后申请", notes = "提交特殊售后申请")
    @PostMapping("/apply")
    public ServerResponse addSpecialAfterSale(@Valid @RequestBody AddSpecialAfterSaleApplyRequest req) throws Exception {
        specialAfterSaleService.apply(req);
        return ServerResponse.ok();
    }

    @ApiOperation(value = "查询特殊售后列表", notes = "查询特殊售后列表")
    @GetMapping("/apply-list")
    public ServerResponse findAfterSaleList(@Valid SpecialAfterSaleApplyRequest req) {
        return ServerResponse.ok(specialAfterSaleService.queryAfterSaleList(req));
    }

    @ApiOperation(value = "导出特殊售后审批数据", notes = "导出全部特殊售后审批数据")
    @GetMapping("/apply-excel")
    public ServerResponse exportAfterSaleApplyList(@Valid SpecialAfterSaleApplyExportRequest req) {
        ExportUtil.writeEasyExcelResponse("全部特殊售后审批列表", specialAfterSaleService.exportAfterSaleApplyList(req));
        return ServerResponse.ok();
    }

    @ApiOperation(value = "查询我提交的申请列表", notes = "查询我提交的申请列表")
    @GetMapping("/my-submitted")
    public ServerResponse findMySubmittedApplyList(@Valid SpecialAfterSaleSearchRequest req) {
        return ServerResponse.ok(specialAfterSaleService.findAllByCreatedBy(req));
    }
}

package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.SpecialAfterSaleApprovalRulesDTO;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleApplyConfigRequest;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleApprovallistRequest;
import com.ruigu.rbox.workflow.service.SpecialAfterSaleApplyConfigService;
import com.ruigu.rbox.workflow.service.SpecialAfterSaleConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/10 12:47
 */
@Slf4j
@Api(value = "特殊售后审批配置API", tags = {"特殊售后审批配置API"})
@RestController
@RequestMapping("/special-after-sale-config")
public class SpecialAfterSaleConfigController {

    @Autowired
    private SpecialAfterSaleApplyConfigService specialAfterSaleApplyConfigService;

    @Autowired
    private SpecialAfterSaleConfigService specialAfterSaleConfigService;

    @ApiOperation(value = "新建/更新审批配置", notes = "新建/保存审批配置")
    @PostMapping("/quota-config-saved")
    public ServerResponse saveOrUpdateApplyList(@RequestBody SpecialAfterSaleApplyConfigRequest req) {
        specialAfterSaleApplyConfigService.saveOrUpdateApproverConfig(req);
        return ServerResponse.ok();
    }

    @ApiOperation(value = "状态启用或禁用", notes = "状态启用或禁用")
    @GetMapping("/is-enable/{status}")
    public ServerResponse enableOrDisable(@RequestParam(value = "status") Integer status, @RequestParam(value = "configId") Integer configId) {
        specialAfterSaleApplyConfigService.enableOrDisable(status, configId);
        return ServerResponse.ok();
    }

    @ApiOperation(value = "查询审批配置列表", notes = "查询审批配置列表")
    @PostMapping("/approval-list")
    public ServerResponse approvalIist(@Valid SpecialAfterSaleApprovallistRequest req) {
        Pageable page = PageRequest.of(req.getPage(), req.getSize());
        List<SpecialAfterSaleApprovalRulesDTO> specialAfterSaleApprovalRulesDetails = specialAfterSaleApplyConfigService.getSpecialAfterSaleApprovalRulesDetails(req.getStatus(), page);
        return ServerResponse.ok(specialAfterSaleApprovalRulesDetails);
    }

    @ApiOperation(value = "通过当前操作人id查询审批配置", notes = "通过当前操作人id查询审批配置")
    @PostMapping("/query-by-operator-id")
    public ServerResponse queryConfigByUserId() {
        return ServerResponse.ok(specialAfterSaleConfigService.queryNextNodeAndCcInfo());
    }
}

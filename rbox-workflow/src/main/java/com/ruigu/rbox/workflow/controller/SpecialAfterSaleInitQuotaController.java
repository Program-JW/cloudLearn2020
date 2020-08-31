package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.cloud.kanai.web.page.PageImpl;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.RsGroupGmvDataDTO;
import com.ruigu.rbox.workflow.model.request.PageableRequest;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleQuotaRuleEnableRequest;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleQuotaRuleHistoryRequest;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleQuotaRuleRequest;
import com.ruigu.rbox.workflow.model.vo.SpecialAfterSaleQuotaRuleVO;
import com.ruigu.rbox.workflow.service.SpecialAfterSaleQuotaService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/13 17:18
 */
@RestController
@RequestMapping("/special-after-apply-quota")
public class SpecialAfterSaleInitQuotaController {

    @Resource
    private SpecialAfterSaleQuotaService specialAfterSaleQuotaService;

    @PostMapping("/init")
    public ServerResponse initQuota(@RequestBody List<RsGroupGmvDataDTO> groupGmvData) {
        specialAfterSaleQuotaService.initQuota(groupGmvData);
        return ServerResponse.ok();
    }

    @ApiOperation(value = "新建/更新额度规则", notes = "新建/更新额度规则")
    @PostMapping("/quota-rule-added")
    public ServerResponse addQuotaRuleScope(@Valid @RequestBody SpecialAfterSaleQuotaRuleRequest req) {
        specialAfterSaleQuotaService.addQuotaRule(req);
        return ServerResponse.ok();
    }

    @ApiOperation(value = "查询额度规则", notes = "查询额度规则")
    @GetMapping("/quota-rule-scope")
    public ServerResponse<PageImpl<SpecialAfterSaleQuotaRuleVO>> findQuotaRuleScope(@Valid PageableRequest req) {
        return ServerResponse.ok(specialAfterSaleQuotaService.queryAllQuotaRule(req));
    }

    @ApiOperation(value = "查询额度规则历史", notes = "查询额度规则历史")
    @GetMapping("/quota-rule-history")
    public ServerResponse<PageImpl<SpecialAfterSaleQuotaRuleVO>> findQuotaRuleScopeHistory(@Valid SpecialAfterSaleQuotaRuleHistoryRequest req) {
        return ServerResponse.ok(specialAfterSaleQuotaService.queryQuotaRuleHistory(req));
    }

    @ApiOperation(value = "启用禁用额度规则", notes = "启用禁用额度规则")
    @GetMapping("/enable/quota-rule")
    public ServerResponse enableQuotaRule(@Valid SpecialAfterSaleQuotaRuleEnableRequest req) {
        specialAfterSaleQuotaService.enableQuotaRule(req);
        return ServerResponse.ok();
    }


}

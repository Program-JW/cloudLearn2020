package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.vo.SpecialAfterSaleDetailApplyPcVO;
import com.ruigu.rbox.workflow.service.SpecialAfterSaleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liqingtian
 * @date 2020/08/19 10:26
 */
@Slf4j
@Api(value = "特殊售后审批PC端 API", tags = {"特殊售后审批PC端 API"})
@RestController
@RequestMapping("/special-after-sale-pc")
public class SpecialAfterSalePcController {

    @Resource
    private SpecialAfterSaleService specialAfterSaleService;

    @ApiOperation(value = "特殊售后审批详情接口", notes = "特殊售后审批详情接口")
    @GetMapping("/{applyId}")
    public ServerResponse<SpecialAfterSaleDetailApplyPcVO> detail(@PathVariable(value = "applyId", required = true) Long applyId) {
        return ServerResponse.ok(specialAfterSaleService.pcDetail(applyId));
    }

}

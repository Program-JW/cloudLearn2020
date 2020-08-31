//package com.ruigu.rbox.workflow.controller;
//
//import com.ruigu.rbox.workflow.model.ServerResponse;
//import com.ruigu.rbox.workflow.service.MpService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.constraints.NotBlank;
//import java.util.Map;
//
///**
// * MP接口包装
// * @author alan.zhao
// */
//@Slf4j
//@Api(value = "MP接口包装", tags = {"MP接口包装"})
//@RestController
//@Validated
//@RequestMapping(value = "/mp")
//public class MpController {
//
//    @Autowired
//    private MpService mpService;
//
//    @ApiOperation(value = "采购单详情")
//    @GetMapping("/purchase/detail")
//    public ServerResponse<Map<String, Object>> queryPurchaseOrderDetail(
//            @ApiParam(value = "采购单编号", required = true)
//            @RequestParam("orderNumber") @NotBlank(message = "采购单号不能为空") String orderNumber,
//            @ApiParam(value = "任务id", required = true)
//            @RequestParam("taskId") @NotBlank(message = "任务ID不能为空") String taskId) throws Exception {
//        return mpService.detail(orderNumber, taskId);
//    }
//
//    @ApiOperation(value = "采购单流程")
//    @GetMapping("/purchase/instance")
//    public ServerResponse<Map<String, Object>> queryPurchaseOrderInstance(
//            @ApiParam(value = "采购单编号", required = true)
//            @NotBlank(message = "采购单号不能为空")
//            @RequestParam("orderNumber") String orderNumber,
//            @ApiParam(value = "实例id", required = true)
//            @NotBlank(message = "实例ID不能为空")
//            @RequestParam("instanceId") String instanceId) throws Exception {
//        return mpService.instance(orderNumber, instanceId);
//    }
//}

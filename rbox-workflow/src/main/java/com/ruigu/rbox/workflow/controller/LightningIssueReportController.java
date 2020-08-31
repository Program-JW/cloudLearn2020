package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.service.LightningIssueReportNewService;
import com.ruigu.rbox.workflow.service.LightningIssueReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;


/**
 * @author caojinghong
 * @date 2020/01/09 17:11
 */
@Api(value = "导出报表接口", tags = {"导出报表接口"})
@RestController
@RequestMapping("/issue-report")
public class LightningIssueReportController {
    @Autowired
    private LightningIssueReportNewService reportNewService;
    @Autowired
    private LightningIssueReportService reportService;

    @ApiOperation(value = "新导出报表", notes = "新导出报表")
    @ApiImplicitParam(name = "queryType", value = "查询类型,0-查询上月的报表，1-查询本月的报表，2-昨日，3-用户自定义时间", required = true, dataType = "Integer")
    @GetMapping("/new/{queryType}")
    public ServerResponse reportForm(@PathVariable("queryType") Integer queryType, @RequestParam(name = "startTime",required = false)LocalDateTime startTime, @RequestParam(name = "endTime",required = false) LocalDateTime endTime) throws Exception {
         return reportNewService.exportExcel(queryType, startTime, endTime);
    }

    @ApiOperation(value = "旧导出报表", notes = "旧导出报表")
    @ApiImplicitParam(name = "queryType", value = "查询类型,0-查询上月的报表，1-查询本月的报表", required = true, dataType = "Integer")
    @GetMapping("/{queryType}")
    public ServerResponse reportForm(@PathVariable("queryType") Integer queryType) throws Exception {
        return reportService.exportExcel(queryType);
    }
    @ApiOperation(value = "获取公司全体员工所在一二级部门名称", notes = "获取公司全体员工所在一二级部门名称")
    @GetMapping("/allUser")
    public ServerResponse getAllUserDepartmentInfo() throws IOException {
        return reportNewService.getAllUserDepartmentInfo();
    }
}

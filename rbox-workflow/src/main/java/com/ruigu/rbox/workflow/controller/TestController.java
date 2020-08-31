package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.service.RemoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试接口
 * @Author alan
 */
@Slf4j
@Api(value = "测试", tags = {"测试"})
@RestController
@RequestMapping(value = "test")
public class TestController {

    @Resource
    private RemoteService remoteService;

    @ApiOperation(value = "查询jira详情")
    @GetMapping("/jira/{jiraCode}")
    public ServerResponse<Object> jiraDetail(@PathVariable("jiraCode") String jiraCode) {
        Map<String, Object> param = new HashMap<>(5);
        param.put("jiraCode", jiraCode);
        ServerResponse<Object> response = remoteService.request("GET", "http://rbox-hr/jira/issue/detail/{jiraCode}", param);
        log.info("response={}", response);
        return response;
    }

    @ApiOperation(value = "查询下一个审批节点")
    @GetMapping("{applyId}/next-node")
    public ServerResponse<Object> nextNode(@PathVariable("applyId") Integer applyId, @RequestParam(value = "nodeId",required = false) Integer nodeId) {
        Map<String, Object> param = new HashMap<>(5);
        param.put("applyId", applyId);
        param.put("nodeId", nodeId);
        return remoteService.request("GET", "http://rbox-hr/leaveApply/{applyId}/next-node?nodeId={nodeId}", param);
    }

}

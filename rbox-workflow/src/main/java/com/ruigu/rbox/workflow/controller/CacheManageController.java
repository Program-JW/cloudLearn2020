package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.service.CacheManageService;
import com.ruigu.rbox.workflow.service.LightningIssueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/06 10:49
 */
@Api(value = "缓存管理", tags = {"缓存管理API"})
@RestController
@RequestMapping("/cache/manage")
public class CacheManageController {

    @Resource
    private LightningIssueService lightningIssueService;
    @Resource
    private CacheManageService cacheManageService;

    @ApiOperation(value = "清除用户我受理列表缓存", notes = "清除用户我受理列表缓存")
    @DeleteMapping("/my-acceptance-id")
    public ServerResponse clearMyAcceptListCache(@RequestParam("userIds") List<Integer> userIds) throws Exception {
        lightningIssueService.clearRedisCache(userIds);
        return ServerResponse.ok();
    }

    @ApiOperation(value = "查询用户我受理列表缓存", notes = "查询用户我受理列表缓存")
    @GetMapping("/my-acceptance-id/{userId}")
    public ServerResponse queryMyAcceptListCache(@PathVariable("userId") Integer userId) throws Exception {
        return ServerResponse.ok(lightningIssueService.queryRedisMyAcceptCache(userId));
    }

    @ApiOperation(value = "查询用户缓存信息", notes = "查询用户缓存信息")
    @GetMapping("/user/{userId}")
    public ServerResponse queryUserInfo(@PathVariable("userId") Integer userId) throws Exception {
        return ServerResponse.ok(cacheManageService.queryCacheUserInfo(Collections.singleton(userId)));
    }

    @ApiOperation(value = "查询用户部门缓存信息", notes = "查询用户部门缓存信息")
    @GetMapping("/user-group/{userId}")
    public ServerResponse queryUserGroupInfo(@PathVariable("userId") Integer userId) throws Exception {
        return ServerResponse.ok(cacheManageService.queryCacheUserAndGroupInfo(Collections.singleton(userId)));
    }

    @ApiOperation(value = "移除用户缓存信息", notes = "移除用户缓存信息")
    @GetMapping("/user/remove/{userId}")
    public ServerResponse removeUserInfo(@PathVariable("userId") Integer userId) throws Exception {
        cacheManageService.removeUserInfo(Collections.singleton(userId));
        return ServerResponse.ok();
    }

    @ApiOperation(value = "清除用户部门缓存信息", notes = "清除用户部门缓存信息")
    @GetMapping("/user-group/remove/{userId}")
    public ServerResponse removeUserGroupInfo(@PathVariable("userId") Integer userId) throws Exception {
        cacheManageService.removeUserAndGroupInfo(Collections.singleton(userId));
        return ServerResponse.ok();
    }
}

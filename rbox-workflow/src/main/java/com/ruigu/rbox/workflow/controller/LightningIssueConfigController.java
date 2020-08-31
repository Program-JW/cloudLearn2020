package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.DutyUserByDayDTO;
import com.ruigu.rbox.workflow.model.entity.DutyRuleEntity;
import com.ruigu.rbox.workflow.model.request.*;
import com.ruigu.rbox.workflow.model.vo.LightningCategoryConfigVO;
import com.ruigu.rbox.workflow.model.vo.LightningDutyRuleDetailVO;
import com.ruigu.rbox.workflow.model.vo.LightningDutyRuleSelectVO;
import com.ruigu.rbox.workflow.service.LightningIssueConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 闪电链问题配置请求
 *
 * @author liqingtian
 * @date 2020/05/07 13:48
 */
@RestController
@Api(value = "闪电链问题配置API", tags = {"闪电链问题配置API"})
@RequestMapping("/config")
public class LightningIssueConfigController {

    @Resource
    private LightningIssueConfigService lightningIssueConfigService;

    /**
     * 问题分类查询接口
     */
    @ApiOperation(value = "问题分类查询接口", notes = "问题分类查询接口")
    @GetMapping("/category/list")
    public ServerResponse<List<LightningCategoryConfigVO>> queryIssueCategory(
            @RequestParam(name = "categoryName", required = false) String categoryName) {
        return ServerResponse.ok(lightningIssueConfigService.selectIssueCategory(categoryName));
    }

    /**
     * 问题分类查询接口
     */
    @ApiOperation(value = "问题分类新增接口", notes = "问题分类新增接口")
    @PostMapping("/category")
    public ServerResponse addIssueCategory(@Valid @RequestBody LightningCategoryRequest request) {
        return ServerResponse.ok(lightningIssueConfigService.saveIssueCategory(request));
    }

    /**
     * 问题分类查询接口
     */
    @ApiOperation(value = "问题分类修改接口", notes = "问题分类修改接口")
    @PutMapping("/category")
    public ServerResponse updateIssueCategory(@Valid @RequestBody LightningCategoryRequest request) {
        lightningIssueConfigService.updateIssueCategory(request);
        return ServerResponse.ok();
    }

    /**
     * 问题分类启用关闭接口
     */
    @ApiOperation(value = "问题分类启用关闭接口", notes = "问题分类启用关闭接口")
    @PutMapping("/category/on-off")
    public ServerResponse categoryOnOff(@Valid @RequestBody OnOffRequest request) {
        lightningIssueConfigService.categoryOnOff(request);
        return ServerResponse.ok();
    }

    /**
     * 策略新增接口
     */
    @ApiOperation(value = "策略新增接口", notes = "策略新增接口")
    @PostMapping("/rule")
    public ServerResponse addCategoryDutyRule(@Valid @RequestBody AddDutyRuleRequest request) {
        return ServerResponse.ok(lightningIssueConfigService.addDutyRule(request));
    }

    /**
     * 策略修改接口
     */
    @ApiOperation(value = "策略修改接口", notes = "策略修改接口")
    @PutMapping("/rule")
    public ServerResponse updateCategoryDutyRule(@Valid @RequestBody UpdateDutyRuleRequest request) {
        lightningIssueConfigService.updateDutyRule(request);
        return ServerResponse.ok();
    }

    /**
     * 策略修改接口
     */
    @ApiOperation(value = "策略详情查询接口", notes = "策略详情查询接口")
    @GetMapping("/rule/{ruleId}")
    public ServerResponse<LightningDutyRuleDetailVO> updateCategoryDutyRule(
            @PathVariable("ruleId") Integer ruleId) {
        return ServerResponse.ok(lightningIssueConfigService.getDutyRuleDetail(ruleId));
    }

    /**
     * 策略列表接口 （下拉框使用接口）
     */
    @ApiOperation(value = "策略列表接口（下拉框使用）", notes = "策略列表接口（下拉框使用）")
    @GetMapping("/rule/select")
    public ServerResponse<List<DutyRuleEntity>> queryRuleDropBoxList() {
        return ServerResponse.ok(lightningIssueConfigService.selectDutyRuleDropBoxList());
    }

    /**
     * 策略详情查询 （策略配置页面）
     */
    @ApiOperation(value = "策略列表查询 （策略配置页面）", notes = "策略列表查询 （策略配置页面）")
    @GetMapping("/rule/list")
    public ServerResponse<List<LightningDutyRuleSelectVO>> queryRuleList(
            @RequestParam(name = "ruleName", required = false) String ruleName) {
        return ServerResponse.ok(lightningIssueConfigService.selectDutyRuleList(ruleName));
    }

    @ApiOperation(value = "策略值班表人员查询 （按天值班专用）", notes = "策略值班表人员查询 （按天值班专用）")
    @GetMapping("/rule/duty-user/{ruleId}")
    public ServerResponse<Page<DutyUserByDayDTO>> queryRuleDutyUserList(@PathVariable("ruleId") Integer ruleId,
                                                                        @RequestParam("page") Integer page,
                                                                        @RequestParam("size") Integer size) {
        return ServerResponse.ok(lightningIssueConfigService.selectDutyUserByRuleId(ruleId, page, size));
    }

    /**
     * 策略启用关闭接口
     */
    @ApiOperation(value = "策略启用关闭接口", notes = "策略启用关闭接口")
    @PutMapping("/rule/on-off")
    public ServerResponse ruleOnOff(@Valid @RequestBody OnOffRequest request) {
        lightningIssueConfigService.ruleOnOff(request);
        return ServerResponse.ok();
    }

    @ApiOperation(value = "分配某个问题分类的值班人（轮询方式专用）", notes = "分配某个问题分类的值班人（轮询方式专用）")
    @GetMapping("/distribution/duty-user/{categoryId}")
    public ServerResponse distributionDuty(@PathVariable("categoryId") Integer categoryId) {
        return ServerResponse.ok(lightningIssueConfigService.distributionDutyUser(categoryId));
    }

    @ApiOperation(value = "手动更新值班redis", notes = "手动更新值班redis")
    @PutMapping("/duty-user/redis")
    public ServerResponse updateRedis(@RequestBody UpdateRedisDutyConfigRequest request) {
        lightningIssueConfigService.updateRedisConfig(request);
        return ServerResponse.ok();
    }

    @ApiOperation(value = "查询值班策略缓存redis", notes = "查询值班策略缓存redis")
    @GetMapping("/duty-user/redis/{categoryId}")
    public ServerResponse queryDutyUserCache(@PathVariable("categoryId") Integer categoryId) {
        return ServerResponse.ok(lightningIssueConfigService.queryRedisDutyUser(categoryId));
    }
}

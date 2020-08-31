package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.cloud.kanai.web.page.PageImpl;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.LightningMySolvedDTO;
import com.ruigu.rbox.workflow.model.entity.DutyRosterEntity;
import com.ruigu.rbox.workflow.model.request.BatchRevokeRequest;
import com.ruigu.rbox.workflow.model.request.DepartmentsAndEmployeesRequest;
import com.ruigu.rbox.workflow.model.request.LightningMyAcceptanceRequest;
import com.ruigu.rbox.workflow.model.request.lightning.*;
import com.ruigu.rbox.workflow.model.vo.GroupAndUserAndCountVO;
import com.ruigu.rbox.workflow.model.vo.LightningIssueCategoryVO;
import com.ruigu.rbox.workflow.model.vo.lightning.*;
import com.ruigu.rbox.workflow.model.vo.LightningMyAcceptanceVO;
import com.ruigu.rbox.workflow.service.LightningIssueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author caojinghong
 * @date 2019/12/27 14:19
 */
@Slf4j
@Api(value = "闪电链问题API", tags = {"闪电链问题API"})
@RestController
@RequestMapping(value = "/lightning-issue")
public class LightningIssueController {
    @Autowired
    private LightningIssueService lightningIssueService;

    @ApiOperation(value = "问题提交接口", notes = "问题提交接口")
    @PostMapping("/submit")
    public ServerResponse addIssue(@Valid @RequestBody LightningIssueApplyReq req) throws Exception {
        return lightningIssueService.insertIssueApply(req);
    }

    @ApiOperation(value = "问题重新提交接口", notes = "问题重新提交接口")
    @PostMapping("/resubmit")
    public ServerResponse reSubmitIssue(@Valid @RequestBody IssueResubmitReq req) throws Exception {
        return ServerResponse.ok(lightningIssueService.resubmitIssueApply(req));
    }

    @ApiOperation(value = "申请人确认问题是否解决", notes = "申请人确认问题是否解决")
    @PostMapping("/confirm")
    public ServerResponse confirmIsResolved(@Valid IssueConfirmReq req) {
        return lightningIssueService.confirmIssue(req);
    }

    @ApiOperation(value = "查询已提交列表", notes = "查询已提交列表")
    @GetMapping("/my-submitted")
    public ServerResponse<PageImpl<LightningMySolvedDTO>> queryMySubmitList(@Valid QueryMySubmittedReq req) {
        return ServerResponse.ok(lightningIssueService.listMySubmitted(req));
    }

    @ApiOperation(value = "我受理列表查询接口", notes = "我受理列表查询接口")
    @GetMapping("/my-acceptance")
    public ServerResponse<Page<LightningMyAcceptanceVO>> queryMyAcceptanceList(@Valid LightningMyAcceptanceRequest req) {
        return ServerResponse.ok(lightningIssueService.listMyAcceptance(req));
    }

    @ApiOperation(value = "查询问题详情", notes = "查询问题详情")
    @ApiImplicitParam(name = "issueId", value = "问题id", required = true, example = "1")
    @GetMapping("/{issueId}")
    public ServerResponse<LightningIssueItemVO> queryIssueItem(@PathVariable("issueId") Integer issueId) throws Exception {
        return lightningIssueService.getIssueItem(issueId);
    }

    @ApiOperation(value = "获取该问题对应的受理人集合", notes = "获取该问题对应的受理人集合")
    @ApiImplicitParam(name = "issueId", value = "问题id", required = true, example = "1")
    @GetMapping("/issue-solvers/{issueId}")
    public ServerResponse<List<LightningUserInfoVO>> querySolversForIssueItem(@PathVariable("issueId") Integer issueId) {
        return lightningIssueService.listSolvers(issueId);
    }

    @ApiOperation(value = "处理人交接接口", notes = "处理人交接接口")
    @PostMapping("/transfer")
    public ServerResponse transferIssue(@RequestBody @Valid IssueTransferReq req) throws Exception {
        return lightningIssueService.transferIssueToAssignee(req);
    }

    @ApiOperation(value = "处理人已解决接口", notes = "处理人已解决接口")
    @PostMapping("/finish")
    public ServerResponse finishIssue(@RequestBody @Valid IssueResolvedReq req) throws Exception {
        return lightningIssueService.finishIssue(req);
    }

    @ApiOperation(value = "申请人评价接口", notes = "申请人评价接口")
    @PostMapping("/evaluate")
    public ServerResponse evaluateIssue(@RequestBody @Valid IssueEvaluateReq req) {
        return lightningIssueService.evaluateIssue(req);
    }

    @ApiOperation(value = "查询当日值班人员", notes = "查询当日值班人员")
    @GetMapping("/duty-roster")
    public ServerResponse<DutyRosterEntity> queryCurrentDutyRoster() {
        return ServerResponse.ok(lightningIssueService.getCurrentDutyRoster());
    }

    @ApiOperation(value = "查询通讯录", notes = "查询通讯录")
    @GetMapping("/departmentAndEmployee")
    public ServerResponse<List<GroupAndUserAndCountVO>> queryDeptAndEmp(DepartmentsAndEmployeesRequest request) {
        return ServerResponse.ok(lightningIssueService.getAddressBook(request));
    }

    @ApiOperation(value = "催办", notes = "催办")
    @GetMapping("/urge/{issueId}")
    public ServerResponse urgeIssue(@PathVariable("issueId") Integer issueId) {
        return lightningIssueService.urgeIssue(issueId);
    }

    @ApiOperation(value = "撤销问题", notes = "撤销问题")
    @PostMapping("/revoke")
    public ServerResponse revokeIssue(@Valid @RequestBody BatchRevokeRequest request) {
        return ServerResponse.ok(lightningIssueService.batchRevokeIssue(request));
    }

    @ApiOperation(value = "获取当前用户的群id集合", notes = "获取当前用户的群id集合")
    @GetMapping("/context")
    public ServerResponse<LightningUsersGroupIdsVO> queryCurrentUserGroupIds() {
        return lightningIssueService.getGroupIds();
    }

    @ApiOperation(value = "维护问题受理人员列表")
    @PostMapping("/addUser")
    public ServerResponse addUser(@Valid @RequestBody IssueAddUserReq req) {
        lightningIssueService.addUser(req.getIssueId(), req.getUserId());
        return ServerResponse.ok();
    }

    @ApiOperation(value = "搜索当前用户参与的问题", notes = "搜索当前用户参与的问题")
    @GetMapping("/my-participated")
    public ServerResponse<Page<LightningMyParticipatedVO>> queryMyParticipatedList(@Valid QueryMySubmittedReq req) {
        return ServerResponse.ok(lightningIssueService.getParticipatedList(req));
    }

    @ApiOperation(value = "领导已超时列表", notes = "领导已超时列表")
    @GetMapping("/leader-overtime")
    public ServerResponse<Page<LightningMyAcceptanceVO>> queryOverTimeForLeaderList(@Valid LightningMyAcceptanceRequest req) {
        return ServerResponse.ok(lightningIssueService.listLeaderOverTime(req));
    }

    @ApiOperation(value = "删除问题接口", notes = "删除问题接口", httpMethod = "DELETE")
    @ApiImplicitParam(name = "issueId", value = "要删除的问题id", required = true, example = "1", dataType = "Integer")
    @DeleteMapping("/{issueId}")
    public ServerResponse deleteUserIssue(@PathVariable("issueId") Integer issueId) {
        return lightningIssueService.deleteIssue(issueId);
    }

    @ApiOperation(value = "根据群组查询问题", notes = "根据群组查询问题")
    @GetMapping("/list/item")
    public ServerResponse getIssueItemByGroupId(@Valid QueryByGroupReq req) {
        return ServerResponse.ok(lightningIssueService.getIssueItemByGroupId(req));
    }

    @GetMapping("/relevant-user/{issueId}")
    @ApiImplicitParam(name = "issueId", value = "问题id", required = true, example = "10")
    @ApiOperation(value = "根据问题id查询该问题相关成员", notes = "根据问题id查询该问题相关成员")
    public ServerResponse<List<LightningIssueRelevantUserVO>> getIssueRelevantUser(@PathVariable("issueId") Integer issueId) {
        return ServerResponse.ok(lightningIssueService.getRelevantUserByIssueId(issueId));
    }

    @ApiOperation(value = "查询问题分类", notes = "查询问题分类")
    @GetMapping("/category")
    public ServerResponse<List<LightningIssueCategoryVO>> selectCategory(@RequestParam(value = "includeAvatar", defaultValue = "1", required = false) String includeAvatar) {
        return ServerResponse.ok(lightningIssueService.selectCategory("1".equals(includeAvatar)));
    }

    @ApiOperation(value = "领导邀请人接口", notes = "领导邀请人接口")
    @PostMapping("/leader-addUser")
    public ServerResponse leaderAddUser(@Valid @RequestBody IssueAddUserReq req) throws Exception {
        return lightningIssueService.leaderInvite(req.getIssueId(), req.getUserId());
    }
}

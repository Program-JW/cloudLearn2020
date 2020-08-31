package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.cloud.kanai.security.UserInfo;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.WorkflowInstanceEntity;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.request.*;
import com.ruigu.rbox.workflow.model.vo.*;
import com.ruigu.rbox.workflow.service.WorkflowInstanceService;
import io.swagger.annotations.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;


/**
 * @author alan.zhao
 */
@Api(value = "流程实例API", tags = {"流程实例"})
@Controller
@RequestMapping(value = "/instance")
public class InstanceController extends BaseController {

    @Resource
    private WorkflowInstanceService workflowInstanceService;

    @ApiOperation(value = "启动实例")
    @PostMapping(value = "/start")
    @ApiResponses({
            @ApiResponse(code = 200, message = "实例ID", response = String.class),
            @ApiResponse(code = 500, message = "该单号已有审批记录或在审批中，请先撤销审批再重新提交。"),
            @ApiResponse(code = 404, message = "业务筛选表单JSON字符串格式错误，无法获取SQL操作符。"),
            @ApiResponse(code = 400, message = "流程不存在或者未发布。")}
    )
    @ResponseBody
    public ServerResponse<String> start(@RequestBody StartInstanceRequest data) throws Exception {
        if (StringUtils.isNotBlank(data.getBusinessKey())) {
            List<String> instances = workflowInstanceService.getEffectiveInstanceByBusinessKey(Collections.singletonList(data.getBusinessKey()));
            if (CollectionUtils.isNotEmpty(instances)) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), "该单号已有审批记录或在审批中，请先撤销审批再重新提交。");
            }
        }
        setOperator(data.getCreatorId());
        Long userId = UserHelper.getUserId().longValue();
        data.setCreatorId(userId);
        return ServerResponse.ok(workflowInstanceService.start(data, userId));
    }

    @ApiOperation(value = "启动实例(外部调用)")
    @PostMapping(value = "/start-external-call")
    @ResponseBody
    public ServerResponse<DefinitionAndInstanceIdVO> startExternalCall(@RequestBody StartInstanceRequest request) {
        // 设置当前操作人
        Integer userId = setOperator(request.getCreatorId());
        // 回写
        request.setCreatorId(userId.longValue());
        // 启动
        return ServerResponse.ok(workflowInstanceService.startExternalCall(request, userId));
    }

    @ApiOperation(value = "启动实例")
    @PostMapping(value = "/lightning/start")
    @ResponseBody
    public ServerResponse<DefinitionAndInstanceIdVO> start(@RequestBody StartLightningInstanceRequest request) throws Exception {
        String businessKey = request.getInstanceInfo().getBusinessKey();
        if (StringUtils.isNotBlank(businessKey)) {
            List<String> instances = workflowInstanceService.getEffectiveInstanceByBusinessKey(Collections.singletonList(businessKey));
            if (CollectionUtils.isNotEmpty(instances)) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), "该单号已有审批记录或在审批中，请先撤销审批再重新提交。");
            }
        }
        setOperator(request.getCreatorId());
        return ServerResponse.ok(workflowInstanceService.lightningStart(request, UserHelper.getUserId().longValue()));
    }

    @ApiOperation(value = "启动实例")
    @PostMapping(value = "/lightning/batch/start")
    @ResponseBody
    public ServerResponse<List<DefinitionAndInstanceIdVO>> batchStart(@Valid @RequestBody BatchStartLightningInstanceRequest request) throws Exception {
        setOperator(request.getCreatorId());
        return ServerResponse.ok(workflowInstanceService.lightningBatchStart(request));
    }

    @ApiOperation(value = "挂起流程实例")
    @ApiIgnore
    @PostMapping(value = "/suspend", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServerResponse<String> suspend(@RequestBody WorkflowInstanceEntity data) throws Exception {
        String pk = workflowInstanceService.suspend(data.getId(), userId());
        return ServerResponse.ok(pk);
    }

    @ApiOperation(value = "激活流程实例")
    @ApiIgnore
    @PostMapping(value = "/activate", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServerResponse<String> activate(@RequestBody WorkflowInstanceEntity data) throws Exception {
        String pk = workflowInstanceService.activate(data, userId());
        return ServerResponse.ok(pk);
    }

    @ApiOperation(value = "删除流程实例")
    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServerResponse delete(@Valid @RequestBody DeleteInstanceRequest data) throws Exception {
        // 设置用户信息（内部调用接口）
        setOperator(data.getUserId().longValue());
        // 调用delete
        workflowInstanceService.delete(data, userId());

        return ServerResponse.ok("删除成功！");
    }

    @ApiIgnore
    @GetMapping("/list")
    @ResponseBody
    public ServerResponse<Page<WorkflowInstanceVO>> list(@Validated SearchInstanceRequest request) throws Exception {
        return ServerResponse.ok(workflowInstanceService.search(request));
    }

    @ApiIgnore
    @GetMapping("/owner/list")
    @ResponseBody
    public ServerResponse<Page<WorkflowInstanceVO>> ownerList(@Validated SearchInstanceRequest request) throws Exception {
        return ServerResponse.ok(workflowInstanceService.searchOwn(request));
    }

    @GetMapping("/my/submit/list")
    @ResponseBody
    public ServerResponse<Page<WorkflowInstanceVO>> mySubmitList(@Validated SearchInstanceRequest request) throws Exception {
        return ServerResponse.ok(workflowInstanceService.searchMySubmit(request));
    }

    @ApiIgnore
    @GetMapping("/list/candidate")
    @ResponseBody
    public ServerResponse<List<TaskVO>> getInstanceList() {
        return ServerResponse.ok(workflowInstanceService.getInstanceListByCandidateId());
    }

    @ApiIgnore
    @GetMapping("/info")
    @ResponseBody
    public ServerResponse<InstanceDetailVO> getInstanceDetail(String instanceId) throws Exception {
        return ServerResponse.ok(workflowInstanceService.getInstanceDetailInfo(instanceId));
    }

    @ApiOperation(value = "查询流程日志")
    @ApiImplicitParam(name = "businessKey", value = "业务key", required = true, dataType = "String")
    @GetMapping("/logs")
    @ResponseBody
    public ServerResponse<List<PurchaseOrderStatusVO>> logs(@NotBlank @RequestParam("businessKey") String businessKey) throws Exception {
        return ServerResponse.ok(workflowInstanceService.getPurchaseProcess(businessKey));
    }

    /**
     * 发送 解决确认中 信号
     */
    @ApiOperation(value = "发送确认信号")
    @PostMapping(value = "/question/confirm")
    @ResponseBody
    public ServerResponse sendConfirmIsSolveSignal(@Valid @RequestBody ConfirmSolveSignalRequest confirmSolveSignalRequest) {
        // 发送信号
        workflowInstanceService.sendConfirmIsSolveSignal(confirmSolveSignalRequest);
        return ServerResponse.ok();
    }

    /**
     * 设置当前操作人
     * 如果
     */
    private Integer setOperator(Long operatorId) {
        if (UserHelper.getUserId() == null || UserHelper.getUserId().longValue() == 0 || UserHelper.getUserId().longValue() == -1) {
            if (operatorId == null || operatorId == 0) {
                throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "创建人ID不能为空或ID不能为0");
            }
            UserInfo info = new UserInfo();
            info.setUserId(operatorId.intValue());
            UserHelper.setUserInfo(info);
            return operatorId.intValue();
        }
        return UserHelper.getUserId();
    }
}

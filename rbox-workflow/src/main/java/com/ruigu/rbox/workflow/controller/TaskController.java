package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.TaskSubmitBatchLogEntity;
import com.ruigu.rbox.workflow.model.request.MyTaskRequest;
import com.ruigu.rbox.workflow.model.request.TaskForm;
import com.ruigu.rbox.workflow.model.request.TaskRequest;
import com.ruigu.rbox.workflow.model.request.TaskTransferRequest;
import com.ruigu.rbox.workflow.model.response.TaskDetail;
import com.ruigu.rbox.workflow.model.vo.TaskVO;
import com.ruigu.rbox.workflow.service.TaskSubmitBatchService;
import com.ruigu.rbox.workflow.service.WorkflowTaskBatchService;
import com.ruigu.rbox.workflow.service.WorkflowTaskService;
import io.swagger.annotations.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author alan.zhao
 */
@Validated
@Api(value = "流程任务API", tags = {"流程任务API"})
@RestController
public class TaskController extends BaseController {

    @Resource
    private WorkflowTaskService workflowTaskService;

    @Resource
    private WorkflowTaskBatchService workflowTaskBatchService;

    @Resource
    private TaskSubmitBatchService taskSubmitBatchService;

    @ApiOperation(value = "任务详情")
    @RequestMapping(value = "/task/info")
    @ApiImplicitParam(name = "id", value = "任务ID", required = true, dataType = "String")
    public ServerResponse<TaskDetail> detail(@NotBlank(message = "任务ID不能为空") @RequestParam("id") String id) throws Exception {
        TaskDetail detail = workflowTaskService.detail(id);
        return ServerResponse.ok(detail);
    }

    @ApiOperation(value = "保存任务")
    @RequestMapping(value = "/task/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ServerResponse<TaskDetail> saveTask(@RequestBody TaskForm task) throws Exception {
        String pk = workflowTaskService.saveTask(task, false, false, false, userId());
        return detail(pk);
    }

    @ApiOperation(value = "提交任务")
    @RequestMapping(value = "/task/submit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ServerResponse<TaskDetail> submitTask(@RequestBody TaskForm task) throws Exception {
        String pk = workflowTaskService.saveTask(task, true, false, false, userId());
        return detail(pk);
    }

    @ApiOperation(value = "自动受理并提交任务")
    @RequestMapping(value = "/task/directsubmit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ServerResponse<TaskDetail> autoBeginSubmitTask(@RequestBody TaskForm task) throws Exception {
        String pk = workflowTaskService.saveTask(task, true, false, false, userId());
        return detail(pk);
    }

    @ApiOperation(value = "批量提交任务")
    @PostMapping(value = "/task/submits")
    public ServerResponse submitTaskList(@RequestBody TaskForm taskForm) throws Exception {
        if (CollectionUtils.isEmpty(taskForm.getIds())) {
            throw new VerificationFailedException(400, "id不能为空");
        }
        return ServerResponse.ok(workflowTaskBatchService.saveTaskList(taskForm, true, userId()));
    }

    @ApiOperation(value = "转办任务")
    @PostMapping("/task/transfer")
    public ServerResponse transfer(@RequestBody TaskTransferRequest form) throws Exception {
        workflowTaskService.transfer(form.getId(), form.getUserId(), (int) userId());
        return ServerResponse.ok();
    }

    @ApiOperation(value = "搜索我的任务")
    @PostMapping("/task/list")
    public ServerResponse<Page<TaskVO>> getMyTaskList(@RequestBody MyTaskRequest myTaskRequest) throws Exception {
        return ServerResponse.ok(workflowTaskService.getAllTaskByCandidateId(myTaskRequest));
    }

    @ApiOperation(value = "开始操作（更新任务状态）")
    @PostMapping("/task/begin")
    public ServerResponse updateStatus(@RequestBody TaskRequest task) throws Exception {
        workflowTaskService.updateBeginStatus(task.getTaskId());
        return ServerResponse.ok();
    }

    @ApiOperation(value = "查询批处理提交进度")
    @ApiImplicitParam(name = "batchId", value = "批处理日志id", required = true, dataType = "Integer")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = TaskSubmitBatchLogEntity.class),
            @ApiResponse(code = 400, message = "所要查询的批处理Id不存在")}
    )
    @GetMapping("/task/submits/progress")
    public ServerResponse<TaskSubmitBatchLogEntity> getBatchInfo(@RequestParam("batchId") Integer batchLogId) throws Exception {
        return ServerResponse.ok(taskSubmitBatchService.getBatchProgress(batchLogId));
    }
}


package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.dto.InstanceTaskInfoDTO;
import com.ruigu.rbox.workflow.model.entity.NodeEntity;
import com.ruigu.rbox.workflow.model.entity.TaskEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowDefinitionEntity;
import com.ruigu.rbox.workflow.model.request.MyTaskRequest;
import com.ruigu.rbox.workflow.model.request.TaskForm;
import com.ruigu.rbox.workflow.model.response.TaskDetail;
import com.ruigu.rbox.workflow.model.vo.TaskVO;
import com.ruigu.rbox.workflow.model.vo.WorkflowInstanceVO;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.task.Task;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author alan.zhao
 */
public interface WorkflowTaskService {

    /**
     * 任务详情
     *
     * @param id 任务ID
     * @return 任务详情
     * @throws Exception 任务不存在时抛出Exception
     */
    TaskDetail detail(String id) throws Exception;

    /**
     * 保存或提交
     *
     * @param taskForm 参数体
     * @param submit   是否是提交 false为暂存 true为提交
     * @param batch    是否批处理
     * @param userId   操作人ID
     * @return 任务ID
     * @throws Exception 验证失败时抛出Exception
     */
    // String saveTask(TaskForm taskForm, boolean submit, boolean batch, Long userId) throws Exception;

    /**
     * 保存或提交（可选择是否自动开始处理）
     *
     * @param taskForm  参数体
     * @param submit    是否是提交 false为暂存 true为提交
     * @param batch     是否批处理
     * @param autoBegin 是否自动开始
     * @param userId    操作人ID
     * @return 任务ID
     * @throws Exception 验证失败时抛出Exception
     */
    String saveTask(TaskForm taskForm, boolean submit, boolean batch, boolean autoBegin, Long userId) throws Exception;

    /**
     * 查询节点
     *
     * @param delegateTask activiti任务信息
     * @return 查询节点信息
     */
    NodeEntity queryNode(DelegateTask delegateTask);

    /**
     * 查询节点
     *
     * @param task 任务(activity)
     * @return 查询节点信息
     */
    NodeEntity queryNodeByTask(Task task);

    /**
     * 根据activiti的任务插入本项目的任务
     *
     * @param delegateTask   activiti任务信息
     * @param nodeEntity     节点定义信息
     * @param candidateUsers 候选人
     * @param definition     流程定义
     * @param instance       流程实例
     * @return 任务信息
     * @throws Exception 抛出Exception
     */
    TaskEntity insertTask(DelegateTask delegateTask, NodeEntity nodeEntity, List<String> candidateUsers, WorkflowDefinitionEntity definition, WorkflowInstanceVO instance) throws Exception;

    /**
     * 转办任务
     *
     * @param taskId  任务ID
     * @param userIds 要转办的目标用户
     * @param userId  操作人ID
     * @throws Exception 抛出Exception
     */
    void transfer(String taskId, List<Integer> userIds, Integer userId) throws Exception;

    /**
     * 保存忽略空值字段
     *
     * @param entity 要保存的任务实体数据
     * @param userId 操作人ID
     * @throws Exception 任务不存在时抛出Exception
     */
    void saveIgnoreNull(TaskEntity entity, Long userId) throws Exception;

    /**
     * 获取所有未完成且在 正常工作时间段（早九晚八，且是工作日中）
     *
     * @return List<TaskVO>
     */
    List<TaskVO> findUnfinishedTask();

    /**
     * 获取某个人所有的任务
     *
     * @param myTaskRequest 我的任务请求参数
     * @return Page<TaskVO>
     * @throws Exception e
     */
    Page<TaskVO> getAllTaskByCandidateId(MyTaskRequest myTaskRequest) throws Exception;

    /**
     * 获取某流程实例当前任务信息
     *
     * @param businessKey   业务主键
     * @param definitionKey 流程定义key
     * @return 当前任务信息
     */
    TaskEntity getCurrentTaskByDefinitionKeyAndBusinessKey(String definitionKey, String businessKey);

    /**
     * 开始操作
     *
     * @param taskId 任务id
     * @return ServerResponse
     * @throws Exception taskId不存在
     */
    void updateBeginStatus(String taskId) throws Exception;

    /**
     * 开始操作
     *
     * @param definitionKey 流程定义key
     * @param businessKey   业务key
     * @return ServerResponse
     * @throws Exception taskId不存在
     */
    void updateBeginStatusByBusinessKey(String definitionKey, String businessKey) throws Exception;

    /**
     * 获取实例任务
     *
     * @param instanceId 实例id
     * @return 任务列表
     */
    List<TaskVO> getAllTaskByInstanceId(String instanceId);

    /**
     * 获取全部任务通过实例ID
     *
     * @param instanceId 实例Id
     * @return 任务列表
     */
    List<TaskEntity> getAllTaskEntityByInstanceId(String instanceId);

    /**
     * 获取任务通过任务ID
     *
     * @param taskId 任务id
     * @return 任务实体
     */
    TaskEntity getTaskEntityById(String taskId);

    /**
     * 通过流程定义key,获取该某个流程下所有未完成的任务信息
     *
     * @param instanceIds 流程实例列表
     * @return 未完成任务列表
     */
    List<TaskEntity> getIncompleteTaskByInstanceIds(List<String> instanceIds);

    /**
     * 通过流程Id.获取为完成任务
     *
     * @param instanceId 流程实例列表
     * @return 未完成任务列表
     */
    TaskEntity getCurrentTaskByInstanceId(String instanceId);

    /**
     * 闪电链 - 我受理查询
     *
     * @param definitionKey 流程定义key
     * @param userId        候选人id
     * @param businessKeys  业务主键列表
     * @return 当前任务id
     */
    List<InstanceTaskInfoDTO> getCurrentTaskIdByBusinessKey(String definitionKey, List<String> businessKeys, Integer userId);

    /**
     * 修改任务状态
     *
     * @param task 任务实体
     */
    void updateTaskBeginStatus(TaskEntity task);
}
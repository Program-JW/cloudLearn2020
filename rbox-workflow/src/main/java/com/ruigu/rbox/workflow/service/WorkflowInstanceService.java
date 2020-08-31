package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.entity.TaskEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowHistoryEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowInstanceEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowInstanceWithTaskEntity;
import com.ruigu.rbox.workflow.model.request.*;
import com.ruigu.rbox.workflow.model.vo.*;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author alan.zhao
 */
public interface WorkflowInstanceService {

    /**
     * 将实例移动到历史表
     *
     * @param id     实例ID
     * @param userId 操作人ID
     * @return 被移动的历史数据
     * @throws Exception e
     */
    WorkflowHistoryEntity moveToHistory(String id, Long userId) throws Exception;

    /**
     * 将实例移动到历史表
     *
     * @param instance 实例
     * @param userId   操作人ID
     * @return 被移动的历史数据
     * @throws Exception e
     */
    WorkflowHistoryEntity moveToHistory(WorkflowInstanceEntity instance, Long userId);

    /**
     * 启动流程实例 （原则上系统内部调用使用）
     *
     * @param request 参数体
     * @param userId  操作人ID
     * @return 返回实例ID
     * @throws Exception 异常
     */
    String start(StartInstanceRequest request, Long userId) throws Exception;

    /**
     * 外部调用 启动流程实例
     */
    DefinitionAndInstanceIdVO startExternalCall(StartInstanceRequest request, Integer userId);

    /**
     * 流程实例启动
     *
     * @param request 参数体
     * @param userId  操作人ID
     * @return 返回定义ID和实例ID
     * @throws Exception 异常
     */
    DefinitionAndInstanceIdVO lightningStart(StartLightningInstanceRequest request, Long userId) throws Exception;

    /**
     * 流程实例启动
     *
     * @param request 参数体
     * @return 返回定义ID和实例ID
     * @throws Exception 异常
     */
    List<DefinitionAndInstanceIdVO> lightningBatchStart(BatchStartLightningInstanceRequest request) throws Exception;

    /**
     * 暂停流程实例
     *
     * @param instanceId 实例
     * @param userId     操作人ID
     * @return 返回实例ID
     * @throws Exception e
     */
    public String suspend(String instanceId, Long userId) throws Exception;

    /**
     * 激活流程实例
     *
     * @param request 参数体
     * @param userId  操作人ID
     * @return 返回实例ID
     * @throws Exception e
     */
    String activate(WorkflowInstanceEntity request, Long userId) throws Exception;

    /**
     * 删除流程实例
     *
     * @param request 参数体
     * @param userId  操作人ID
     * @throws Exception e
     */
    void delete(DeleteInstanceRequest request, Long userId) throws Exception;

    /**
     * 获取所有实例列表
     *
     * @param request 搜索实例请求参数
     * @return Page<WorkflowInstanceVO>
     * @throws Exception e
     */
    Page<WorkflowInstanceVO> search(SearchInstanceRequest request) throws Exception;

    /**
     * 获取实例详情entity
     *
     * @param instanceId 实例id
     * @return WorkflowInstanceEntity
     */
    WorkflowInstanceEntity findInstanceById(String instanceId);

    /**
     * 获取候选人所有的流程实例
     *
     * @return List<TaskVO>
     */
    List<TaskVO> getInstanceListByCandidateId();

    /**
     * 获取当前登陆人所负责实例列表
     *
     * @param request 搜索实例参数
     * @return Page<WorkflowInstanceVO>
     * @throws Exception e
     */
    Page<WorkflowInstanceVO> searchOwn(SearchInstanceRequest request) throws Exception;

    /**
     * 获取当前登陆人所创建的实例列表
     *
     * @param request 搜索实例参数
     * @return Page<WorkflowInstanceVO>
     * @throws Exception e
     */
    Page<WorkflowInstanceVO> searchMySubmit(SearchInstanceRequest request) throws Exception;

    /**
     * 获取为解决问题
     *
     * @param key            流程定义key
     * @param taskStatusList 任务状态列表
     * @return 未完成流程
     */
    List<WorkflowInstanceEntity> getIncompleteInstanceByDefinitionKeyAndTaskStatusIn(String key, List<Integer> taskStatusList);

    /**
     * 获取为解决流程及相应任务信息
     *
     * @param key            流程定义key
     * @param taskStatusList 任务状态列表
     * @return 为完成流程及任务详情
     */
    List<WorkflowInstanceWithTaskEntity> getIncompleteInstanceWithTaskByDefinitionKeyAndTaskStatusIn(String key, List<Integer> taskStatusList);

    /**
     * 获取实例详细信息（不带评论）
     *
     * @param instanceId 实例ID
     * @return ServerResponse<WorkflowInstanceVO>
     */
    WorkflowInstanceVO getInstanceById(String instanceId);

    /**
     * 获取实例详细信息（不带评论）
     *
     * @param taskId 任务ID
     * @return WorkflowInstanceVO
     */
    WorkflowInstanceVO getInstanceByTaskId(String taskId);

    /**
     * 获取实例详细信息（带评论，表单数据）
     *
     * @param instanceId 实例ID
     * @return ServerResponse<WorkflowInstanceVO>
     * @throws Exception e
     */
    InstanceDetailVO getInstanceDetailInfo(String instanceId) throws Exception;

    /**
     * 查询采购单状态
     *
     * @param businessKey 业务key
     * @return ：该实例的流程状态，也就是处理过程
     * @throws Exception e
     * @author ：jianghuilin
     * @date ：Created in {2019/9/16} {11:40}
     */
    List<PurchaseOrderStatusVO> getPurchaseProcess(String businessKey) throws Exception;

    /**
     * 通过businessKey 查询有无作废流程
     *
     * @param businessKey 业务参数
     * @return 有效的流程
     */
    List<String> getEffectiveInstanceByBusinessKey(List<String> businessKey);

    /**
     * 撤销审核 （作废流程）
     *
     * @param instanceId 流程id列表
     */
    @Transactional(rollbackFor = Exception.class)
    void revokeInstanceById(String instanceId);

    /**
     * 撤销审核 （作废流程）
     *
     * @param revokeRequestList 撤销请求参数列表
     * @return 不能撤销的id列表
     */
    List<String> revokeLightningInstanceById(List<RevokeRequest> revokeRequestList);

    /**
     * 自己创建的单子才可以撤回
     *
     * @param businessKey 业务参数
     * @param userId      创建人ID
     * @return 有效的流程
     */
    List<WorkflowInstanceEntity> getSelfEffectiveInstanceByBusinessKey(List<String> businessKey, Long userId);

    /**
     * 自己创建的单子才可以撤回
     *
     * @param instanceIds 流程id
     * @param userId      创建人ID
     * @return 有效的流程
     */
    List<WorkflowInstanceEntity> getSelfEffectiveInstanceById(List<String> instanceIds, Long userId);

    /**
     * 问题撤销  在非已解决情况下
     *
     * @param executionId 执行单元的id
     * @param signalName  信号名称
     */
    @Transactional(rollbackFor = Exception.class)
    void revokeIssueInstanceById(String signalName, String executionId);

    /**
     * 发送确认信号
     *
     * @param confirmSolveSignalRequest 请求实体
     */
    void sendConfirmIsSolveSignal(ConfirmSolveSignalRequest confirmSolveSignalRequest);

    /**
     * 公用方法 发送信号
     *
     * @param signalName  信号名
     * @param executionId 流程id
     */
    void sendSignal(String signalName, String executionId);

    /**
     * 催办
     *
     * @param definitionKey 定义key
     * @param businessKey   业务主键key
     */
    void urgeCurrentTaskByBusinessKey(String definitionKey, String businessKey);

    /**
     * 催办当前任务
     *
     * @param currentTask 当前任务id
     * @param type        类型 （ 超时催办 和 超时未受理催办 ）
     */
    void urgeTask(TaskEntity currentTask, Integer type);
}

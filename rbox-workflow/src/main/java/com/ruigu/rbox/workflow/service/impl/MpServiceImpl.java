//package com.ruigu.rbox.workflow.service.impl;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.ruigu.rbox.cloud.kanai.util.TimeUtil;
//import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
//import com.ruigu.rbox.workflow.feign.MpClient;
//import com.ruigu.rbox.workflow.feign.PassportFeignClient;
//import com.ruigu.rbox.workflow.model.ServerResponse;
//import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
//import com.ruigu.rbox.workflow.model.entity.OperationLogEntity;
//import com.ruigu.rbox.workflow.model.entity.TaskEntity;
//import com.ruigu.rbox.workflow.model.entity.WorkflowDefinitionEntity;
//import com.ruigu.rbox.workflow.model.enums.ResponseCode;
//import com.ruigu.rbox.workflow.model.vo.PurchaseReqVO;
//import com.ruigu.rbox.workflow.model.vo.TaskVO;
//import com.ruigu.rbox.workflow.model.vo.WorkflowInstanceVO;
//import com.ruigu.rbox.workflow.repository.OperationLogRepository;
//import com.ruigu.rbox.workflow.repository.TaskRepository;
//import com.ruigu.rbox.workflow.repository.WorkflowDefinitionRepository;
//import com.ruigu.rbox.workflow.service.MpService;
//import com.ruigu.rbox.workflow.service.WorkflowInstanceService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
///**
// * @author liqingtian
// * @date 2019/09/23 14:52
// */
//@Slf4j
//@Service
//public class MpServiceImpl implements MpService {
//
//    @Autowired
//    private MpClient mp;
//
//    @Autowired
//    private TaskRepository taskRepository;
//
//    @Autowired
//    private WorkflowInstanceService workflowInstanceService;
//
//    @Autowired
//    private WorkflowDefinitionRepository workflowDefinitionRepository;
//
//    @Autowired
//    private OperationLogRepository operationLogRepository;
//
//    @Autowired
//    private PassportFeignClient passportFeignClient;
//
//    @Override
//    public ServerResponse<Map<String, Object>> detail(String orderNumber, String taskId) {
//        try {
//            Map<String, Object> data = new HashMap<>(16);
//            // 获取详情
//            PurchaseReqVO vo = new PurchaseReqVO();
//            vo.setOrderNumber(orderNumber);
//            ServerResponse<Object> detailInfo = mp.getPurchaseOrderDetailInfo(vo);
//            if (detailInfo.getCode() != ResponseCode.SUCCESS.getCode()) {
//                log.error("MP获取采购单详情失败。" + JSONObject.toJSONString(detailInfo));
//                return ServerResponse.fail(500, "获取MP系统采购单获取详细数据失败。");
//            }
//            data.put("detail", detailInfo.getData());
//            // 获取按钮
//            TaskEntity task = taskRepository.selectNoticeContentByTaskId(taskId);
//            if (task == null) {
//                return ServerResponse.fail("获取任务详情异常，获取详细数据失败。");
//            }
//            if (StringUtils.isNotBlank(task.getNoticeContent())) {
//                data.put("button", JSONArray.parseArray(task.getNoticeContent()));
//            }
//            // 获取流程信息
//            Map<String, Object> info = new HashMap<>(16);
//            WorkflowInstanceVO instanceById = workflowInstanceService.getInstanceById(task.getInstanceId());
//            if (instanceById == null) {
//                throw new VerificationFailedException(400, "流程实例信息查询失败。");
//            }
//            WorkflowDefinitionEntity definition = workflowDefinitionRepository.findById(instanceById.getDefinitionId())
//                    .orElseThrow(() -> new VerificationFailedException(400, "流程定义信息查询失败。"));
//            Integer creatorId = instanceById.getCreatedBy().intValue();
//            Integer submitId = null;
//            Set<Integer> userSet = new HashSet<>();
//            if (task.getSubmitBy() != null) {
//                submitId = task.getSubmitBy().intValue();
//                userSet.add(submitId);
//            }
//            userSet.add(creatorId);
//            ServerResponse<List<PassportUserInfoDTO>> userMsgByIds = passportFeignClient.getUserMsgByIds(userSet);
//            if (userMsgByIds.getCode() == ResponseCode.SUCCESS.getCode()) {
//                List<PassportUserInfoDTO> userInfoList = userMsgByIds.getData();
//                Integer finalSubmitId = submitId;
//                userInfoList.forEach(userInfo -> {
//                    Integer userId = userInfo.getId();
//                    if (creatorId.equals(userId)) {
//                        info.put("creator", userInfo.getNickname());
//                    }
//                    if (finalSubmitId != null) {
//                        if (finalSubmitId.equals(userId)) {
//                            info.put("submit", userInfo.getNickname());
//                        }
//                    }
//                });
//            } else {
//                log.error("失败。远程调用权限中心异常。--> ");
//                log.error("失败返回信息：" + JSONObject.toJSONString(userMsgByIds));
//            }
//            info.put("status", task.getStatus());
//            info.put("name", definition.getName());
//            info.put("createdOn", TimeUtil.format(instanceById.getCreatedOn(), TimeUtil.FORMAT_DATE_TIME));
//            info.put("submitOn", TimeUtil.format(instanceById.getStartTime(), TimeUtil.FORMAT_DATE_TIME));
//            data.put("task", info);
//            return ServerResponse.ok(data);
//        } catch (Exception e) {
//            log.error("获取mp信息异常。 --> ", e);
//            return ServerResponse.fail(500, "异常采购单详情异常。");
//        }
//    }
//
//    @Override
//    public ServerResponse instance(String orderNumber, String instanceId) {
//        Map<String, Object> data = new HashMap<>(16);
//        // 获取详情
//        PurchaseReqVO vo = new PurchaseReqVO();
//        vo.setOrderNumber(orderNumber);
//        ServerResponse<Object> detailInfo = mp.getPurchaseOrderDetailInfo(vo);
//        if (detailInfo.getCode() != ResponseCode.SUCCESS.getCode()) {
//            log.error("MP获取采购单详情失败。" + JSONObject.toJSONString(detailInfo));
//            return ServerResponse.fail(500, "获取MP系统采购单获取详细数据失败。");
//
//        }
//        data.put("detail", detailInfo.getData());
//        // 获取流程信息
//        Set<Integer> userIdSet = new HashSet<>();
//        WorkflowInstanceVO instance = workflowInstanceService.getInstanceById(instanceId);
//        userIdSet.add(instance.getCreatedBy().intValue());
//        List<TaskVO> tasks = taskRepository.selectAllTaskByInstanceId(instanceId);
//        List<String> taskIds = new ArrayList<>();
//        tasks.forEach(task -> {
//            if (task.getSubmitBy() != null) {
//                userIdSet.add(task.getSubmitBy().intValue());
//            }
//            taskIds.add(task.getId());
//        });
//        // 流程结束任务id
//        taskIds.add("0");
//        List<OperationLogEntity> operationLogs = operationLogRepository.findAllByInstanceIdAndStatusAndTaskIdNotIn(instance.getId(), 1, taskIds);
//        operationLogs.forEach(log -> {
//            TaskVO task = new TaskVO();
//            task.setId("-1");
//            task.setSubmitBy(log.getCreatedBy().longValue());
//            task.setCreatedOn(log.getCreatedOn());
//            task.setSubmitTime(log.getCreatedOn());
//            task.setName(log.getContent());
//            task.setStatus(log.getStatus());
//            userIdSet.add(log.getCreatedBy());
//            tasks.add(task);
//        });
//        // 调用feign获取姓名
//        ServerResponse<List<PassportUserInfoDTO>> userInfoResponse = passportFeignClient.getUserMsgByIds(userIdSet);
//        if (userInfoResponse.getCode() == ResponseCode.SUCCESS.getCode()) {
//            List<PassportUserInfoDTO> userInfoList = userInfoResponse.getData();
//            List<PassportUserInfoDTO> finalUserInfoList = userInfoList;
//            tasks.forEach(task -> {
//                if (task.getSubmitBy() == null) {
//                    return;
//                }
//                PassportUserInfoDTO info = finalUserInfoList.stream()
//                        .filter(u -> task.getSubmitBy() == u.getId().longValue())
//                        .findFirst().orElse(null);
//                if (info != null) {
//                    // 实际含义是任务的提交人（处理任务的人）
//                    task.setCreator(info.getNickname());
//                }
//            });
//
//            PassportUserInfoDTO info = finalUserInfoList.stream()
//                    .filter(u -> instance.getCreatedBy() == u.getId().longValue())
//                    .findFirst().orElse(null);
//            if (info != null) {
//                instance.setCreator(info.getNickname());
//            }
//        } else {
//            log.error("消息中心：获取用户信息失败。 --> ");
//            log.error("失败信息： " + JSONObject.toJSONString(userInfoResponse));
//        }
//        Collections.sort(tasks, new Comparator<TaskVO>() {
//            @Override
//            public int compare(TaskVO o1, TaskVO o2) {
//                return o1.getCreatedOn().compareTo(o2.getCreatedOn());
//            }
//        });
//        data.put("task", tasks);
//        data.put("instance", instance);
//        return ServerResponse.ok(data);
//    }
//}

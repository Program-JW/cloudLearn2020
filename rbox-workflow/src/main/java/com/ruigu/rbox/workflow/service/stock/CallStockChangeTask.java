package com.ruigu.rbox.workflow.service.stock;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.workflow.feign.ScmFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.*;
import com.ruigu.rbox.workflow.repository.TaskRepository;
import com.ruigu.rbox.workflow.service.NoticeLogService;
import com.ruigu.rbox.workflow.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author alan.zhao
 * @date 2019/11/02 23:42
 */
@Slf4j
@Service("callStockChangeServiceTask")
public class CallStockChangeTask implements JavaDelegate {

    @Resource
    private ScmFeignClient scmFeignClient;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private NoticeLogService noticeLogService;

    @Resource
    private OperationLogService operationLogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(DelegateExecution delegateExecution) {

        // 是否执行成功
//        int complete = 0;
//
//        try {
//            distributedLocker.lock(RBOX_WORKFLOW_CHANGE_STOCK);
//            log.info("=============================== [ 变更库存 ] [ 加锁 ] ===========================");
//            Map<String, Object> variables = delegateExecution.getVariables();
//            Integer applyId = Integer.valueOf(String.valueOf(variables.get("applyId")));
//
//            ServerResponse<StockChangeApplyEntity> response = scmFeignClient.getStockChangeApplyById(applyId);
//
//            StockChangeApplyEntity apply;
//            if (response.getCode() == ResponseCode.SUCCESS.getCode()) {
//                apply = response.getData();
//            } else {
//                throw new GlobalRuntimeException(response.getCode(), response.getMessage());
//            }
//
//            if (apply == null) {
//                log.error("|-执行库存变更 异常。无法查询到该申请单。申请ID : " + applyId);
//                return;
//            }
//            if (apply.getActualTime() != null) {
//                log.error("执行库存变更 异常。该申请已经调用过stock接口。");
//                return;
//            }
//            // 组装请求参数
//            Map<String, Object> params = new HashMap<>(16);
//            Integer storageId = Integer.valueOf(String.valueOf(variables.get(StockChangeApplyParam.STORAGE_ID.getDesc())));
//            params.put(StockChangeApplyParam.PHP_STORAGE_ID.getDesc(), storageId);
//            params.put(StockChangeApplyParam.PHP_STOCK_TYPE.getDesc(), 1);
//            params.put(StockChangeApplyParam.PHP_FROM_TTB.getDesc(), 1);
//            params.put(StockChangeApplyParam.PHP_RECORD_CODE.getDesc(), "");
//            params.put(StockChangeApplyParam.PHP_PUBLISH_BY.getDesc(), variables.get("instanceCreatorId"));
//            List<Map<String, Object>> skuMap = (List<Map<String, Object>>) JSONArray.parse(String.valueOf(variables.get(StockChangeApplyParam.ITEM.getDesc())));
//            List<Map<String, Object>> phpSkuMap = new ArrayList<>();
//            skuMap.forEach(sku -> {
//                Map<String, Object> data = new HashMap<>(16);
//                data.put(StockChangeApplyParam.PHP_SKU_CODE.getDesc(), sku.get(StockChangeApplyParam.SKU_CODE.getDesc()));
//                Integer count = Integer.valueOf(String.valueOf(sku.get(StockChangeApplyParam.COUNT.getDesc())));
//                if (apply.getIncreased() == 0) {
//                    data.put(StockChangeApplyParam.COUNT.getDesc(), 0 - count);
//                } else {
//                    data.put(StockChangeApplyParam.COUNT.getDesc(), count);
//                }
//                phpSkuMap.add(data);
//            });
//            params.put(StockChangeApplyParam.ITEM.getDesc(), phpSkuMap);
//
//            StringBuilder content = new StringBuilder("商品编码：" + String.valueOf(phpSkuMap.get(0).get(StockChangeApplyParam.PHP_SKU_CODE.getDesc())) + "\n");
//            content.append("所属仓库：" + StorageEnum.getValueByCode(storageId) + "\n");
//            content.append("变更数量：" + String.valueOf(phpSkuMap.get(0).get(StockChangeApplyParam.COUNT.getDesc())) + "\n");
//            // 请求stock
//            ServerResponse publishStockResponse = null;
//            try {
//                publishStockResponse = stockFeignClient.publishStockCount(params);
//                if (publishStockResponse.getCode() == ResponseCode.SUCCESS.getCode()) {
//                    content.append("系统可售库存更新成功");
//                    complete = 1;
//                } else {
//                    content.append("系统可售库存更新失败。失败返回信息：" + publishStockResponse.getMessage());
//                }
//            } catch (Exception e) {
//                log.error("|-执行库存变更 异常。调用异常。失败原因：", e);
//                content.append("系统异常，系统可售库存更新失败");
//            } finally {
//
//                // 保存本地变量
//                Map<String, Object> executionInfo = new HashMap<>(4);
//                executionInfo.put();
//                executionInfo.put();
//                delegateExecution.setVariables(executionInfo);
//
////                ServerResponse<Integer> integerServerResponse = scmFeignClient.saveChangeApply(apply);
////                if (integerServerResponse.getCode() != ResponseCode.SUCCESS.getCode()) {
////                    throw new GlobalRuntimeException(integerServerResponse.getCode(), integerServerResponse.getMessage());
////                }
//                ApiLogEntity apiLogEntity = new ApiLogEntity();
//                apiLogEntity.setObjectId(applyId);
//                apiLogEntity.setRequestData(JSONObject.toJSONString(params));
//                apiLogEntity.setResponseData(JSONObject.toJSONString(publishStockResponse));
//                apiLogEntity.setMethodName(this.getClass().toString() + ": execute");
//                apiLogEntity.setRequestUrl("/sys/stock/publishStockCount");
//                apiLogEntity.setCreateOn(new Date());
//                apiLogEntity.setStatus(1);
//
//                apiLogRepository.save(apiLogEntity);
//
//            }
//            List<TaskEntity> tasks = taskRepository.findAllByInstanceId(apply.getInstanceId());
//            List<List<String>> taskUserList = tasks.stream()
//                    .map(t -> Arrays.asList(t.getCandidateUsers().split(",")))
//                    .collect(Collectors.toList());
//            int[] users = taskUserList.stream()
//                    .flatMapToInt(childList -> childList.stream().mapToInt(Integer::new)).toArray();
//            Set<Integer> targets = new HashSet<>(Arrays.asList(ArrayUtils.toObject(users)));
//            targets.add(apply.getCreatedBy());
//            // 记录日志
//            recordLog(content.toString(), apply, targets);
//            //刷新拉平边界的状态
//            final Integer yes = 1;
//            if (yes.equals(apply.getCompleted())) {
//                Integer skuCode = Integer.valueOf(phpSkuMap.get(0).get(StockChangeApplyParam.PHP_SKU_CODE.getDesc()).toString());
//                ServerResponse serverResponse = scmFeignClient.refreshSkuUnbalancedFlag(storageId, skuCode);
//                if (serverResponse.getCode() != ResponseCode.SUCCESS.getCode()) {
//                    throw new GlobalRuntimeException(serverResponse.getCode(), serverResponse.getMessage());
//                }
//            }
//        } finally {
//            distributedLocker.unlock(RBOX_WORKFLOW_CHANGE_STOCK);
//            log.info("=============================== [ 变更库存 ] [ 释放锁 ] ===========================");
//        }

        log.info(" ----- notice scm execute ---- ");

        final String APPLY_ID = "applyId";
        final String NOTICE_EXECUTE_STATUS = "noticeExecuteStatus";

        // 获取申请id
        Map<String, Object> variables = delegateExecution.getVariables();
        Integer applyId = Integer.valueOf(String.valueOf(variables.get(APPLY_ID)));

        // 通知状态
        int noticeExecuteStatus = YesOrNoEnum.NO.getCode();

        // 通知业务系统执行
        try {
            ServerResponse<StockChangeApplyEntity> serverResponse = scmFeignClient.noticeExecute(applyId);
            if (serverResponse.getCode() == ResponseCode.SUCCESS.getCode()) {
                // 成功的话 必定会有apply
                StockChangeApplyEntity apply = serverResponse.getData();
                // 本地变量中有备份数据，因此不再调用接口查询 而是自己拼装
                StockChangeDetailEntity detail = new StockChangeDetailEntity();
                detail.setApplyId(applyId);
                Integer skuCode = (Integer) delegateExecution.getVariable(StockChangeApplyParam.SKU_CODE.getDesc());
                detail.setSkuCode(skuCode);
                Integer count = (Integer) delegateExecution.getVariable(StockChangeApplyParam.COUNT.getDesc());
                detail.setCount(count);
                // 记录本次动作
                recordLog(apply, detail);
                // 打成功标志位
                noticeExecuteStatus = YesOrNoEnum.YES.getCode();
            }
        } finally {
            log.info(" ------ notice execute status : {} ------ ", noticeExecuteStatus);
            delegateExecution.setVariable(NOTICE_EXECUTE_STATUS, noticeExecuteStatus);
        }
    }

    private void recordLog(StockChangeApplyEntity apply, StockChangeDetailEntity detail) {

        // 用于标识日志
        final String EVENT = "noticeExecute";

        String instanceId = apply.getInstanceId();

        // 防止重复打日志
        List<OperationLogEntity> noticeExecuteLog = operationLogService.getLogByInstanceIdAndEvent(instanceId, EVENT);
        if (CollectionUtils.isNotEmpty(noticeExecuteLog)) {
            return;
        }

        // 兼容老流程（老流程因为没有 noticeExecute 的事件日志）
        // 因此用serviceTask的通知日志来进行防重
        List<NoticeEntity> noticeLog = noticeLogService.getListNoticeByInstanceIdAndType(instanceId, InstanceEvent.SERVER_TASK.getCode());
        if (CollectionUtils.isNotEmpty(noticeLog)) {
            return;
        }

        // 1. 组装 content
        String result = null;
        Integer completed = apply.getCompleted();
        if (completed == YesOrNoEnum.YES.getCode()) {
            // 调用成功
            result = "系统可售库存更新成功";
        } else {
            // 因为各种原因调用失败
            result = "系统可售库存更新失败";
        }
        StringBuilder noticeContent = new StringBuilder();
        noticeContent.append("商品编码：").append(detail.getSkuCode()).append(Symbol.BR.getValue());
        noticeContent.append("所属仓库：").append(StorageEnum.getValueByCode(apply.getStorageId())).append(Symbol.BR.getValue());
        noticeContent.append("变更数量：").append(detail.getCount()).append(Symbol.BR.getValue());
        noticeContent.append(result);

        // 2. 查询涉及到的人
        List<TaskEntity> allTask = taskRepository.findAllByInstanceId(instanceId);
        List<String> userStringList = new ArrayList<>();
        allTask.forEach(t -> userStringList.add(t.getCandidateUsers()));
        String targets = StringUtils.join(userStringList, Symbol.BR.getValue());

        // 3. 记录日志
        Date now = new Date();
        String definitionId = apply.getDefinitionId();
        Integer createdBy = apply.getCreatedBy();

        // 通知日志
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setCreatedOn(now);
        noticeEntity.setContent(noticeContent.toString());
        noticeEntity.setTargets(targets);
        noticeEntity.setDefinitionId(definitionId);
        noticeEntity.setInstanceId(instanceId);
        noticeEntity.setType(InstanceEvent.SERVER_TASK.getCode());
        noticeEntity.setStatus((byte) -1);
        noticeLogService.insertNotice(noticeEntity);

        // 操作日志
        OperationLogEntity operationLogEntity = new OperationLogEntity();
        operationLogEntity.setContent(result);
        operationLogEntity.setDefinitionId(definitionId);
        operationLogEntity.setInstanceId(instanceId);
        operationLogEntity.setEvent(EVENT);
        operationLogEntity.setCreatedBy(createdBy);
        operationLogEntity.setLastUpdatedBy(createdBy);
        operationLogEntity.setCreatedOn(now);
        operationLogEntity.setLastUpdatedOn(now);
        operationLogService.log(operationLogEntity);
    }
}

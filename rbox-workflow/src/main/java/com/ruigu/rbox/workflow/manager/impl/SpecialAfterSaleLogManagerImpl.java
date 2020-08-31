package com.ruigu.rbox.workflow.manager.impl;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.manager.SpecialAfterSaleLogManager;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleLogEntity;
import com.ruigu.rbox.workflow.model.enums.SpecialAfterSaleLogActionEnum;
import com.ruigu.rbox.workflow.model.vo.SpecialAfterSaleLogVO;
import com.ruigu.rbox.workflow.repository.SpecialAfterSaleLogRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liqingtian
 * @date 2020/08/11 17:51
 */
@Service
public class SpecialAfterSaleLogManagerImpl implements SpecialAfterSaleLogManager {

    @Resource
    private SpecialAfterSaleLogRepository specialAfterSaleLogRepository;
    @Resource
    private PassportFeignManager passportFeignManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SpecialAfterSaleLogEntity createActionLog(Long applyId, String taskId, Integer reviewNodeId, Integer action, String remark, Integer isShow, Integer createdBy) {
        return specialAfterSaleLogRepository.save(buildLog(applyId, taskId, reviewNodeId, action, remark, isShow, createdBy));
    }

    @Override
    public void createActionLog(Long applyId, String taskId, Integer reviewNodeId, Integer action, String remark, Integer isShow, List<Integer> createdBy) {
        List<SpecialAfterSaleLogEntity> logs = new ArrayList<>();
        createdBy.forEach(id -> {
            logs.add(buildLog(applyId, taskId, reviewNodeId, action, remark, isShow, id));
        });
        specialAfterSaleLogRepository.saveAll(logs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SpecialAfterSaleLogEntity hideOrShowLog(Long applyId, String taskId, Integer action, Integer isShow, Integer createdBy) {
        SpecialAfterSaleLogEntity log = specialAfterSaleLogRepository.findByApplyIdAndTaskIdAndActionAndCreatedBy(applyId, taskId, action, createdBy);
        log.setShow(isShow);
        return specialAfterSaleLogRepository.save(log);
    }

    @Override
    public List<SpecialAfterSaleLogEntity> hideOrShowLog(Long applyId, String taskId, Integer action, Integer isShow) {
        List<SpecialAfterSaleLogEntity> actionLog = specialAfterSaleLogRepository.findByApplyIdAndTaskIdAndAction(applyId, taskId, action);
        actionLog.forEach(l -> l.setShow(isShow));
        return specialAfterSaleLogRepository.saveAll(actionLog);
    }

    @Override
    public void createTransferLog(Long applyId, String taskId, List<Integer> transferUserIdList, Integer operatorId) {
        // 隐藏操作人待审批日志
        SpecialAfterSaleLogEntity pendingApprovalOperatorLog = specialAfterSaleLogRepository.findByApplyIdAndTaskIdAndActionAndCreatedBy(applyId, taskId, SpecialAfterSaleLogActionEnum.PENDING_APPROVAL.getCode(), operatorId);
        pendingApprovalOperatorLog.setShow(YesOrNoEnum.NO.getCode());
        specialAfterSaleLogRepository.save(pendingApprovalOperatorLog);

        Integer reviewNodeId = pendingApprovalOperatorLog.getReviewNodeId();
        // 增加操作人转审日志
        specialAfterSaleLogRepository.save(buildLog(applyId, taskId, reviewNodeId, SpecialAfterSaleLogActionEnum.TRANSFER.getCode(), null, YesOrNoEnum.NO.getCode(), operatorId));
        // 增加转审人待审批日志
        List<SpecialAfterSaleLogEntity> transferPendingLogList = new ArrayList<>();
        transferUserIdList.forEach(id -> {
            transferPendingLogList.add(buildLog(applyId, taskId, reviewNodeId, SpecialAfterSaleLogActionEnum.PENDING_APPROVAL.getCode(), null, YesOrNoEnum.YES.getCode(), id));
        });
        specialAfterSaleLogRepository.saveAll(transferPendingLogList);
    }

    @Override
    public List<SpecialAfterSaleLogEntity> queryListActionLog(Long applyId, Integer action) {
        return null;
    }

    @Override
    public List<SpecialAfterSaleLogVO> queryListLog(Long applyId) {
        List<SpecialAfterSaleLogVO> results = new ArrayList<>();
        // 查询日志 （因为一个节点可能有多个人的日志）
        List<SpecialAfterSaleLogEntity> logList = specialAfterSaleLogRepository.findAllByApplyIdAndShowOrderById(applyId, YesOrNoEnum.YES.getCode());
        if (CollectionUtils.isEmpty(logList)) {
            return results;
        }
        // 先通过taskId进行聚合
        Map<String, List<SpecialAfterSaleLogEntity>> mapByTaskId = logList.parallelStream().collect(Collectors.groupingBy(SpecialAfterSaleLogEntity::getTaskId));
        mapByTaskId.forEach((taskId, logs) -> {
            SpecialAfterSaleLogEntity source = logs.get(0);
            // 基本信息
            SpecialAfterSaleLogVO vo = new SpecialAfterSaleLogVO();
            vo.setId(source.getId());
            vo.setReviewNodeId(source.getReviewNodeId());
            vo.setAction(source.getAction());
            vo.setRemarks(source.getRemarks());
            vo.setCreatedAt(source.getCreatedAt());
            vo.setShow(source.getShow());
            // 日志操作人
            final Integer systemId = -1;
            List<Integer> operatorIds = logs.stream().map(SpecialAfterSaleLogEntity::getCreatedBy)
                    .filter(id -> !systemId.equals(id)).collect(Collectors.toList());
            vo.setOperatorIdList(operatorIds);
            Map<Integer, PassportUserInfoDTO> userInfo = passportFeignManager.getUserInfoMapFromRedis(operatorIds);
            List<String> operationNameList = userInfo.values().stream().map(PassportUserInfoDTO::getNickname).collect(Collectors.toList());
            vo.setOperatorNameList(operationNameList);
            results.add(vo);
        });
        // 按id升序
        results.sort(Comparator.comparing(SpecialAfterSaleLogVO::getId));
        return results;
    }

    private SpecialAfterSaleLogEntity buildLog(Long applyId, String taskId, Integer reviewNodeId, Integer action, String remark, Integer isShow, Integer createdBy) {
        SpecialAfterSaleLogEntity entity = new SpecialAfterSaleLogEntity();
        entity.setApplyId(applyId);
        entity.setReviewNodeId(reviewNodeId);
        entity.setTaskId(taskId);
        entity.setRemarks(remark);
        entity.setAction(action);
        entity.setShow(isShow);
        entity.setStatus(YesOrNoEnum.YES.getCode());
        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setCreatedBy(createdBy);
        entity.setLastUpdatedAt(now);
        entity.setLastUpdatedBy(createdBy);
        return entity;
    }
}

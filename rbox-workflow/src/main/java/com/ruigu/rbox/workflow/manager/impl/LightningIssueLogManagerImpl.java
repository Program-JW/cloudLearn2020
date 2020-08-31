package com.ruigu.rbox.workflow.manager.impl;

import com.ruigu.rbox.workflow.manager.LightningIssueLogManager;
import com.ruigu.rbox.workflow.model.entity.LightningIssueLogEntity;
import com.ruigu.rbox.workflow.model.enums.LightningIssueLogStatusEnum;
import com.ruigu.rbox.workflow.model.request.lightning.LightningOverTimeReq;
import com.ruigu.rbox.workflow.repository.LightningIssueLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author caojinghong
 * @date 2019/12/31 16:11
 */
@Component
public class LightningIssueLogManagerImpl implements LightningIssueLogManager {
    @Autowired
    private LightningIssueLogRepository lightningIssueLogRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveIssueLogAction(Integer issueId, Integer action, Integer createdBy) {
        LightningIssueLogEntity logEntity = saveIssueLog(issueId, action, createdBy);
        return lightningIssueLogRepository.save(logEntity).getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveIssueLogAction(Integer issueId, Integer action, Integer createdBy, String remarks) {
        LightningIssueLogEntity logEntity = saveIssueLog(issueId, action, createdBy, remarks);
        return lightningIssueLogRepository.save(logEntity).getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveIssueLogAction(List<Integer> issueIds, Integer action, Integer createdBy) {
        List<LightningIssueLogEntity> logEntities = new ArrayList<>();
        for (Integer issueId : issueIds) {
            LightningIssueLogEntity logEntity = saveIssueLog(issueId, action, createdBy);
            logEntities.add(logEntity);
        }
        lightningIssueLogRepository.saveAll(logEntities);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveIssueLogOverTime(List<LightningOverTimeReq> req) {
        List<LightningIssueLogEntity> logEntities = new ArrayList<>();
        for (LightningOverTimeReq everyOverTime : req) {
            LightningIssueLogEntity logEntity = saveIssueLog(everyOverTime.getIssueId(), everyOverTime.getAction(), everyOverTime.getCreatedBy());
            logEntities.add(logEntity);
        }
        lightningIssueLogRepository.saveAll(logEntities);
    }

    @Override
    public LightningIssueLogEntity saveIssueLog(Integer issueId, Integer action, Integer createdBy) {
        LightningIssueLogEntity logEntity = new LightningIssueLogEntity();
        logEntity.setIssueId(issueId);
        logEntity.setStatus(LightningIssueLogStatusEnum.OPEN.getCode());
        logEntity.setAction(action);
        logEntity.setCreatedOn(new Date());
        logEntity.setCreatedBy(createdBy);
        logEntity.setLastUpdatedOn(new Date());
        logEntity.setLastUpdatedBy(createdBy);
        return logEntity;
    }
    @Override
    public LightningIssueLogEntity saveIssueLog(Integer issueId, Integer action, Integer createdBy, String remarks) {
        LightningIssueLogEntity logEntity = new LightningIssueLogEntity();
        logEntity.setIssueId(issueId);
        logEntity.setStatus(LightningIssueLogStatusEnum.OPEN.getCode());
        logEntity.setAction(action);
        logEntity.setCreatedOn(new Date());
        logEntity.setCreatedBy(createdBy);
        logEntity.setRemarks(remarks);
        logEntity.setLastUpdatedOn(new Date());
        logEntity.setLastUpdatedBy(createdBy);
        return logEntity;
    }
}

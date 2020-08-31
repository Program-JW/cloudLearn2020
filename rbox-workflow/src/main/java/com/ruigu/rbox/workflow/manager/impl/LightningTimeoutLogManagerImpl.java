package com.ruigu.rbox.workflow.manager.impl;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.workflow.manager.LightningTimeoutLogManager;
import com.ruigu.rbox.workflow.model.entity.LightningTimeoutLogEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author liqingtian
 * @date 2020/03/03 16:10
 */
@Component
public class LightningTimeoutLogManagerImpl implements LightningTimeoutLogManager {
    @Override
    public LightningTimeoutLogEntity returnTimeoutLog(Integer issueId, Integer type, Integer createdBy) {
        LightningTimeoutLogEntity timeoutLogEntity = new LightningTimeoutLogEntity();
        timeoutLogEntity.setIssueId(issueId);
        timeoutLogEntity.setType(type);
        timeoutLogEntity.setCreatedBy(createdBy);
        timeoutLogEntity.setStatus(YesOrNoEnum.YES.getCode());
        LocalDateTime now = LocalDateTime.now();
        timeoutLogEntity.setCreatedOn(now);
        timeoutLogEntity.setLastUpdatedBy(createdBy);
        timeoutLogEntity.setLastUpdatedOn(now);
        return timeoutLogEntity;
    }
}

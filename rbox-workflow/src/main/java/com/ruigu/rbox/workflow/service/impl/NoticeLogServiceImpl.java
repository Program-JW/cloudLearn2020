package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.model.entity.NoticeEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowDefinitionEntity;
import com.ruigu.rbox.workflow.model.enums.InstanceEvent;
import com.ruigu.rbox.workflow.repository.NoticeRepository;
import com.ruigu.rbox.workflow.repository.WorkflowDefinitionRepository;
import com.ruigu.rbox.workflow.service.NoticeLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author liqingtian
 * @date 2019/08/21 16:58
 */
@Service
@Slf4j
public class NoticeLogServiceImpl implements NoticeLogService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private WorkflowDefinitionRepository workflowDefinitionRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long insertNotice(NoticeEntity notice) {
        try {
            NoticeEntity save = noticeRepository.save(notice);
            return save.getId();
        } catch (Exception e) {
            log.error("异常，通知保存失败。", e);
            log.debug("--> notice : taskId : " + notice.getTaskId());
            log.debug("--> notice : title : " + notice.getTitle());
            log.debug("--> notice : content : " + notice.getContent());
            log.debug("--> notice : createdOn : " + notice.getCreatedOn());
            log.debug("--> notice : status : " + notice.getStatus());
            log.debug("--> notice : taskIdWeixin : " + notice.getTaskIdWeixin());
        }
        return -1;
    }

    @Override
    public NoticeEntity getNoticeByTaskIdWeixin(String taskIdWeixin) {
        return noticeRepository.findTopByTaskIdWeixinOrderByCreatedOn(taskIdWeixin);
    }

    @Override
    public NoticeEntity getNoticeTaskId(String taskId) {
        return noticeRepository.findTopByTaskIdOrderByCreatedOn(taskId);
    }

    @Override
    public void updateNotice(String taskId) {
        try {
            noticeRepository.updateStatus(taskId);
        } catch (Exception e) {
            log.error("状态更新异常。 任务id:" + taskId);
            log.error("异常信息：" + e);
        }
    }

    @Override
    public NoticeEntity getLastTimeoutNoticeByTaskId(String taskId) {
        return noticeRepository.findByTaskIdAndTypeAndStatusOrderByCreatedOnDesc(taskId,
                InstanceEvent.TIME_OUT.getCode(), Byte.valueOf("1"));
    }

    @Override
    public List<NoticeEntity> getLastTimeoutNoticeByInstanceId(String instanceId) {
        return noticeRepository.findByInstanceIdAndTypeAndStatusOrderByCreatedOnDesc(instanceId,
                InstanceEvent.TIME_OUT.getCode(), Byte.valueOf("1"));
    }

    @Override
    public List<NoticeEntity> getTimeoutNoticeByInstanceIds(List<String> instanceIds) {
        return noticeRepository.findAllByTypeAndStatusAndInstanceIdIn(InstanceEvent.TIME_OUT.getCode(),
                Byte.valueOf("1"), instanceIds);
    }

    @Override
    public List<NoticeEntity> getListNoticeByInstanceIdAndType(String instanceId, Integer type) {
        return noticeRepository.findAllByInstanceIdAndType(instanceId, type);
    }

    @Override
    public List<NoticeEntity> getNoticesByInstanceIdInAndTypeIn(List<String> instanceIds, List<Integer> typeList) {
        return noticeRepository.findByInstanceIdInAndTypeIn(instanceIds, typeList);
    }

    @Override
    public long updateNoticeCount(Long id) {
        try {
            return noticeRepository.updateNoticeCount(id);
        } catch (Exception e) {
            log.error("通知次数更新失败。 通知id:" + id);
            log.error("异常信息：" + e);
        }
        return -1;
    }

    @Override
    public List<NoticeEntity> getUnsentNotice(String definitionKey, Integer type) {
        // 先通过definition code 获取所有版本的流程定义Id
        List<WorkflowDefinitionEntity> definitionList = workflowDefinitionRepository.findAllByInitialCodeAndIsReleasedAndStatus(definitionKey, 1, 1);
        if (CollectionUtils.isEmpty(definitionList)) {
            return null;
        }
        List<String> definitionIds = definitionList.stream().map(d -> d.getId()).collect(Collectors.toList());
        return noticeRepository.findAllByDefinitionIdInAndTypeAndStatus(definitionIds, type, (byte) -1);
    }

    @Override
    public void batchUpdate(List<NoticeEntity> noticeList) {
        try {
            noticeRepository.saveAll(noticeList);
        } catch (Exception e) {
            log.error("批量更新notice失败，失败信息：", e);
        }
    }

    @Override
    public List<NoticeEntity> getStockChangeTimeoutLeaderNotice(String definitionCode) {
        return noticeRepository.findStockLeaderTimeoutNotice(definitionCode);
    }

    @Override
    public NoticeEntity getNoticeByInstanceIdAndTaskIdAndType(String instanceId, String taskId, Integer type, String targets) {
        return noticeRepository.findByInstanceIdAndTaskIdAndTypeAndTargets(instanceId, taskId, type, targets);
    }
}

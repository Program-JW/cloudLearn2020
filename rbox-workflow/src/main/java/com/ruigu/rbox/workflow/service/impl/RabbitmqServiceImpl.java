package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.model.entity.RabbitmqMsgLogEntity;
import com.ruigu.rbox.workflow.repository.RabbitmqMsgRepository;
import com.ruigu.rbox.workflow.service.RabbitmqMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author liqingtian
 * @date 2019/09/10 18:28
 */
@Slf4j
@Service
public class RabbitmqServiceImpl implements RabbitmqMsgService {

    @Autowired
    private RabbitmqMsgRepository rabbitmqMsgRepository;

    @Override
    public int saveMsg(RabbitmqMsgLogEntity msgEntity) {
        try {
            msgEntity.setStatus(1);
            msgEntity.setCreatedOn(new Date());
            RabbitmqMsgLogEntity save = rabbitmqMsgRepository.save(msgEntity);
            return save.getId();
        } catch (Exception e) {
            log.error("Rabbit MQ 信息保存失败");
            log.error("异常信息 --> ", e);
            log.error("message:{}", msgEntity);
        }
        return -1;
    }

    @Override
    public int updateStatus(Integer id) {
        try {
            rabbitmqMsgRepository.updateStatus(id);
        } catch (Exception e) {
            log.error("状态更新失败 --> ", e);
        }
        return 0;
    }

    @Override
    public RabbitmqMsgLogEntity getMsgByTaskId(String taskId) {
        return rabbitmqMsgRepository.findByTaskIdWeixin(taskId);
    }
}

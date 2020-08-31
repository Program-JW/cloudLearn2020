package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.entity.RabbitmqMsgLogEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liqingtian
 * @date 2019/09/10 18:27
 */
public interface RabbitmqMsgService {

    /**
     * 保存rabbitmq消息
     *
     * @param msgEntity 消息实体
     * @return int
     */
    @Transactional(rollbackFor = Exception.class)
    int saveMsg(RabbitmqMsgLogEntity msgEntity);

    /**
     * 修改消息状态
     *
     * @param id 消息实体id
     * @return int
     */
    @Transactional(rollbackFor = Exception.class)
    int updateStatus(Integer id);

    /**
     * 通过微信任务id查找消息
     *
     * @param taskId 微信任务id
     * @return RabbitmqMsgEntity
     */
    RabbitmqMsgLogEntity getMsgByTaskId(String taskId);
}

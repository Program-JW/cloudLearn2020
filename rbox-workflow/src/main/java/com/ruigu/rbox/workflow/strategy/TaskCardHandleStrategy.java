package com.ruigu.rbox.workflow.strategy;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.TaskCardReturnMessageDTO;
import com.ruigu.rbox.workflow.model.entity.NoticeEntity;
import com.ruigu.rbox.workflow.model.vo.MessageInfoVO;

import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/12/31 16:53
 */
public interface TaskCardHandleStrategy {

    /**
     * 任务卡片事件处理
     *
     * @param message rabbitmq消息
     * @param notice  卡片通知记录
     * @return 处理结果
     */
    ServerResponse handle(TaskCardReturnMessageDTO message, NoticeEntity notice);
}

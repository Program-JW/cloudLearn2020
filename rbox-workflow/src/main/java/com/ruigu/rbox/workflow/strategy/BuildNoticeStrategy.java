package com.ruigu.rbox.workflow.strategy;

import com.ruigu.rbox.workflow.model.request.EnvelopeReq;
import com.ruigu.rbox.workflow.model.vo.MessageInfoVO;

/**
 * @author liqingtian
 * @date 2019/10/14 17:50
 */
public interface BuildNoticeStrategy {

    /**
     * 发送通知
     *
     * @param message 通知消息信息实体
     * @return 发送消息体
     */
    EnvelopeReq build(MessageInfoVO message);
}

package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.enums.EnvelopeChannelEnum;
import com.ruigu.rbox.workflow.model.enums.EnvelopeTypeEnum;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2020/08/17 0:45
 */
public interface MultipleAppQuestNoticeService {

    /**
     * 发送文本卡片
     *
     * @param channel 渠道
     * @param title   标题
     * @param content 文本
     * @param url     链接
     * @param target  目标
     */
    void sendTextCardMultipleApp(EnvelopeChannelEnum channel, String title, String content, String url, Collection<Integer> target);

    /**
     * 发送文本卡片
     *
     * @param channel 渠道
     * @param body    消息体
     * @param target  目标
     */
    void sendTextCardMultipleApp(EnvelopeChannelEnum channel, Map<String, Object> body, Collection<Integer> target);

    /**
     * 发送文本卡片
     *
     * @param channel 渠道
     * @param content 消息内容
     * @param target  目标
     */
    void sendTextMultipleApp(EnvelopeChannelEnum channel, String content, List<Integer> target);
}

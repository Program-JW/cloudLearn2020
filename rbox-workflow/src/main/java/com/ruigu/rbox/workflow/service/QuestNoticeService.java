package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.LightningUnreadMessageDTO;
import com.ruigu.rbox.workflow.model.request.EnvelopeReq;
import com.ruigu.rbox.workflow.model.vo.EmailAttachment;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/08/20 13:43
 */
public interface QuestNoticeService extends MultipleAppQuestNoticeService {

    /**
     * 发送邮件通知
     *
     * @param url      链接
     * @param title    标题
     * @param content  内容
     * @param target   到达
     * @param ccTarget 抄送
     * @return ServerResponse
     * @throws Exception e
     */
    ServerResponse sendEmailNotice(String url, String title, String content, Collection<Integer> target, Collection<Integer> ccTarget) throws Exception;

    /**
     * 发送邮件通知
     *
     * @param url         链接
     * @param title       标题
     * @param content     内容
     * @param target      到达
     * @param ccTarget    抄送
     * @param attachments 附件清单
     * @return ServerResponse
     * @throws Exception e
     */
    ServerResponse sendEmailNotice(String url, String title, String content, Collection<Integer> target, Collection<Integer> ccTarget, Collection<EmailAttachment> attachments) throws Exception;

    /**
     * 发送微信任务卡片通知
     *
     * @param body   内容
     * @param target 到达
     * @return ServerResponse
     */
    ServerResponse sendWeiXinTaskCardNotice(Map<String, Object> body, Collection<Integer> target);

    /**
     * 发送微信文本卡片通知
     *
     * @param body   内容
     * @param target 到达
     * @return ServerResponse
     */
    ServerResponse sendWeiXinTextCardNotice(Map<String, Object> body, Collection<Integer> target);

    /**
     * 发送微信文本通知
     *
     * @param url     标题
     * @param content 内容
     * @param target  到达
     * @return ServerResponse
     */
    ServerResponse sendWeiXinTextNotice(String url, String content, Collection<Integer> target);

    /**
     * 闪电链 - 发送未读消息
     *
     * @param unreadMessage 未读消息信息
     * @return 发送结果
     */
    ServerResponse sendUnreadMessageNotice(LightningUnreadMessageDTO unreadMessage);

    /**
     * 发送预警
     *
     * @param content 内容
     */
    void sendWarnNotice(String content);

    /**
     * 发送通知根据事件 （单任务节点使用 ！！！！！，多任务节点禁止使用，自己重新封装方法）
     *
     * @param instanceId 实例id
     * @param event      事件
     * @param targets    送达人（不传，则默认）
     * @return 发送结果
     */
    ServerResponse sendNoticeByEventAndId(String instanceId, Integer event, Collection<Integer> targets);

    /**
     * 发送
     *
     * @param envelopeReqs 消息体
     * @return 相应
     */
    ServerResponse sendNoticeByMq(List<EnvelopeReq> envelopeReqs);


}

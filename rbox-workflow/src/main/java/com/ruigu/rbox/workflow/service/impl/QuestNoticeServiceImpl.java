package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.cloud.kanai.util.TimeUtil;
import com.ruigu.rbox.workflow.config.RabbitMqConfig;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.factory.EnvelopeFactory;
import com.ruigu.rbox.workflow.feign.HedwigFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.LightningUnreadMessageDTO;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.*;
import com.ruigu.rbox.workflow.model.request.EnvelopeReq;
import com.ruigu.rbox.workflow.model.request.UserReq;
import com.ruigu.rbox.workflow.model.vo.EmailAttachment;
import com.ruigu.rbox.workflow.model.vo.MessageInfoVO;
import com.ruigu.rbox.workflow.repository.WorkflowDefinitionRepository;
import com.ruigu.rbox.workflow.repository.WorkflowInstanceRepository;
import com.ruigu.rbox.workflow.service.MultipleAppQuestNoticeService;
import com.ruigu.rbox.workflow.service.NoticeConfigService;
import com.ruigu.rbox.workflow.service.QuestNoticeService;
import com.ruigu.rbox.workflow.strategy.context.SendNoticeContext;
import com.ruigu.rbox.workflow.supports.ElUtil;
import com.ruigu.rbox.workflow.supports.NoticeContentUtil;
import com.ruigu.rbox.workflow.supports.NoticeUtil;
import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author liqingtian
 * @date 2019/08/20 13:44
 */
@Slf4j
@org.springframework.stereotype.Service
public class QuestNoticeServiceImpl implements QuestNoticeService {
    @Resource
    private NoticeConfigService noticeConfigService;

    @Resource
    private HedwigFeignClient hedwigFeignClient;

    @Resource
    private SendNoticeContext sendNoticeContext;

    @Resource
    private WorkflowInstanceRepository workflowInstanceRepository;

    @Resource
    private WorkflowDefinitionRepository workflowDefinitionRepository;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private NoticeUtil noticeUtil;

    @Resource
    private NoticeContentUtil noticeContentUtil;

    @Value("${rbox.workflow.definition.lightning}")
    private String lightningKey;

    @Value("${rbox.workflow.warnning.notice.target}")
    private List<Integer> warnTarget;

    @Resource(name = "rabbitTemplate")
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMqConfig.SendProperties sendProperties;

    @Resource
    private EnvelopeFactory envelopeFactory;

    @Override
    public ServerResponse sendEmailNotice(String url, String title, String content, Collection<Integer> target, Collection<Integer> ccTarget) throws Exception {
        log.debug("=================================== 邮件发送 ===================================");
        return sendEmail(url, title, content, target, ccTarget);
    }

    @Override
    public ServerResponse sendEmailNotice(String url, String title, String content, Collection<Integer> target, Collection<Integer> ccTarget, Collection<EmailAttachment> attachments) throws Exception {
        log.debug("=================================== 邮件发送 ===================================");
        return sendEmail(url, title, content, target, ccTarget, attachments);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponse sendWeiXinTaskCardNotice(Map<String, Object> body, Collection<Integer> target) {
        log.debug("=================================== 任务卡片发送 ===================================");
        return sendCard(body, target, NoticeType.TASK_CARD);
    }

    @Override
    public ServerResponse sendWeiXinTextCardNotice(Map<String, Object> body, Collection<Integer> target) {
        log.debug("=================================== 文本卡片发送 ===================================");
        return sendCard(body, target, NoticeType.TEXT_CARD);
    }

    @Override
    public ServerResponse sendWeiXinTextNotice(String url, String content, Collection<Integer> target) {
        log.debug("=================================== 文本通知发送 ===================================");
        return sendText(content, target, url);
    }

    @Override
    public ServerResponse sendUnreadMessageNotice(LightningUnreadMessageDTO unreadMessage) {
        LightningIssueApplyEntity issueInfo = unreadMessage.getIssueInfo();
        // 获取姓名
        // 获取notice模板
        String definitionId = issueInfo.getDefinitionId();
        List<NoticeTemplateEntity> noticeTemplates = noticeConfigService.getNoticeTemplate(NoticeConfigState.DEFINITION,
                definitionId, InstanceEvent.REMIND_UNREAD.getCode());
        if (CollectionUtils.isEmpty(noticeTemplates)) {
            return ServerResponse.ok();
        }
        NoticeTemplateEntity template = noticeTemplates.get(0);
        Map<String, Object> variable = new HashMap<>(4);
        variable.put(InstanceVariableParam.BUSINESS_KEY.getText(), issueInfo.getId());
        variable.put(InstanceVariableParam.UNREAD_CHAT_MESSAGE.getText(), unreadMessage.getContent());
        variable.put(InstanceVariableParam.LEADER_NAME.getText(), unreadMessage.getFromUserName());
        variable.put(InstanceVariableParam.INSTANCE_CREATE_TIME.getText(), TimeUtil.format(issueInfo.getCreatedOn(), TimeUtil.FORMAT_DATE_TIME));
        String desc = issueInfo.getDescription().length() <= 10 ? issueInfo.getDescription() : issueInfo.getDescription().substring(0, 10);
        variable.put(InstanceVariableParam.DESCRIPTION.getText(), desc);
        variable.put(InstanceVariableParam.RECEIVER_NAME.getText(), unreadMessage.getCurrentUserName());
        variable.put(InstanceVariableParam.LINE_FEED.getText(), "\n");
        variable.put(InstanceVariableParam.UNREAD_CHAT_SEND_TIME.getText(),
                TimeUtil.format(TimeUtil.localDateTime2Date(unreadMessage.getSendTime()), TimeUtil.FORMAT_DATE_TIME));
        MessageInfoVO messageInfo = new MessageInfoVO();
        messageInfo.setNoticeEventType(InstanceEvent.REMIND_UNREAD.getCode());
        messageInfo.setTargets(unreadMessage.getToUserList());
        messageInfo.setInstanceId(issueInfo.getInstanceId());
        messageInfo.setDefinitionId(definitionId);
        messageInfo.setTitle(ElUtil.fill(variable, template.getTitle()));
        messageInfo.setDescription(ElUtil.fill(variable, template.getContent()));
        messageInfo.setUrl(ElUtil.fill(variable, template.getDetailUrl()));
        return sendNoticeContext.send(template, messageInfo);
    }

    @Override
    public void sendWarnNotice(String content) {
        sendText(content, warnTarget, null);
    }

    @Override
    public ServerResponse sendNoticeByEventAndId(String instanceId, Integer event, Collection<Integer> targets) {
        // 获取流程实例信息
        WorkflowInstanceEntity instance = workflowInstanceRepository.findById(instanceId).orElseThrow(() -> new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "异常，流程信息缺失"));
        // 获取该流程所对应通知信息
        String definitionId = instance.getDefinitionId();
        List<NoticeTemplateEntity> templateList = noticeConfigService.getNoticeTemplate(instance.getDefinitionId(), event);
        if (CollectionUtils.isEmpty(templateList)) {
            throw new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "没有查找到相应通知模板");
        }
        // 获取流程变量
        Map<String, Object> variables = runtimeService.getVariables(instanceId);
        // 获取流程定义信息
        WorkflowDefinitionEntity definition = workflowDefinitionRepository.findById(definitionId).orElseThrow(() -> new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "异常，流程定义信息丢失"));
        for (NoticeTemplateEntity template : templateList) {
            MessageInfoVO messageInfo = noticeContentUtil.translateDefinitionTemplate(template, definition, instanceId, variables);
            messageInfo.setTargets(targets);
            messageInfo.setNoticeEventType(event);
            ServerResponse send = sendNoticeContext.send(template, messageInfo);
            if (send.getCode() != ResponseCode.SUCCESS.getCode()) {
                return send;
            }
        }
        return ServerResponse.ok();
    }

    @Override
    public ServerResponse sendNoticeByMq(List<EnvelopeReq> envelopeReqs) {
        String routeKey = sendProperties.getRouting();
        String exchange = sendProperties.getExchange();
        envelopeReqs.forEach(e ->
                rabbitTemplate.convertSendAndReceive(exchange, routeKey, JsonUtil.toJsonString(e)));
        return ServerResponse.ok();
    }

    private ServerResponse sendEmail(String url, String title, String content, Collection<Integer> target, Collection<Integer> ccTarget) {
        return sendEmail(url, title, content, target, ccTarget, null);
    }

    private ServerResponse sendEmail(String url, String title, String content, Collection<Integer> target, Collection<Integer> ccTarget, Collection<EmailAttachment> attachments) {
        EnvelopeReq envelope = noticeUtil.getEnvelopReqEmail(ccTarget, target, attachments);
        // 内容
        Map<String, Object> contentMap = new HashMap<>(8);
        contentMap.put("channel", envelope.getChannel().get(0));
        content = content.replace("\n", " ");
        Map<String, Object> bodyMap = new HashMap<>(8);
        bodyMap.put("content", content);
        bodyMap.put("title", title);
        contentMap.put("body", bodyMap);
        envelope.setContent(Collections.singletonList(contentMap));
        rabbitTemplate.convertAndSend(sendProperties.getExchange(), sendProperties.getRouting(), JsonUtil.toJsonString(envelope));
        return ServerResponse.ok();
    }

    private ServerResponse sendText(String content, Collection<Integer> target, String url) {

        // 发送微信文本通知
        EnvelopeReq envelope = noticeUtil.getEnvelopReqWeixin(NoticeType.TEXT, target);

        // 设置内容
        Map<String, Object> contentMap = new HashMap<>(8);
        contentMap.put("channel", envelope.getChannel().get(0));
        if (url != null) {
            String noticeUrl = "<a href=\"" + url + "\"> 详情</a>";
            contentMap.put("body", content + noticeUrl);
        } else {
            contentMap.put("body", content);
        }
        envelope.setContent(Collections.singletonList(contentMap));

        return send(envelope);
    }

    private ServerResponse sendCard(Map<String, Object> body, Collection<Integer> target, NoticeType noticeType) {

        // 发送微信卡片通知
        EnvelopeReq envelope = noticeUtil.getEnvelopReqWeixin(noticeType, target);

        // 设置内容
        Map<String, Object> contentMap = new HashMap<>(8);
        contentMap.put("channel", envelope.getChannel().get(0));
        contentMap.put("body", body);
        envelope.setContent(Collections.singletonList(contentMap));

        return send(envelope);
    }

    private ServerResponse send(EnvelopeReq envelope) {
        try {
            ServerResponse cardResponse = hedwigFeignClient.sendMsg(envelope);
            if (cardResponse.getCode() == ResponseCode.SUCCESS.getCode()) {
                log.debug("消息中心：发送 - 返回信息：{} ", cardResponse);
            } else {
                log.error("消息中心：通知发送失败 - 失败消息：{}", JsonUtil.toJsonString(cardResponse));
            }
            return cardResponse;
        } catch (Exception e) {
            log.error("消息中心：任务卡片通知发送异常 - 异常消息：{} ", e);
            return ServerResponse.fail("Exception：{}" + e);
        }
    }

    @Override
    public void sendTextCardMultipleApp(EnvelopeChannelEnum channel, String title, String content, String url, Collection<Integer> target) {
        if (StringUtils.isBlank(url)) {
            throw new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "URL缺失，发送失败");
        }
        // 组装内容
        Map<String, Object> body = new HashMap<>(8);
        body.put(NoticeParam.TITLE.getDesc(), title);
        body.put(NoticeParam.DESCRIPTION.getDesc(), content);
        body.put(NoticeParam.URL.getDesc(), url);
        // 发送
        sendTextCardMultipleApp(channel, body, target);
    }

    @Override
    public void sendTextCardMultipleApp(EnvelopeChannelEnum channel, Map<String, Object> body, Collection<Integer> target) {
        if (CollectionUtils.isEmpty(target)) {
            return;
        }
        // 获取模板
        EnvelopeReq envelopeReq = envelopeFactory.create(channel, EnvelopeTypeEnum.WEIXIN_TEXT_CARD);
        // 默认按钮配置
        body.put(NoticeParam.BTN_TXT.getDesc(), "详情");
        // 拼装req
        setContent(body, null, envelopeReq);
        // 发送人
        setTarget(target, envelopeReq);
        // 发送
        send(envelopeReq);
    }

    @Override
    public void sendTextMultipleApp(EnvelopeChannelEnum channel, String content, List<Integer> target) {
        if (CollectionUtils.isEmpty(target)) {
            return;
        }
        // 获取模板
        EnvelopeReq envelopeReq = envelopeFactory.create(channel, EnvelopeTypeEnum.WEIXIN_TEXT);
        setContent(null, content, envelopeReq);
        setTarget(target, envelopeReq);
    }

    private void setTarget(Collection<Integer> targetId, EnvelopeReq envelopeReq) {
        List<UserReq> targets = new ArrayList<>();
        targetId.forEach(id -> {
            UserReq u = new UserReq();
            u.setId(id);
            targets.add(u);
        });
        envelopeReq.setUsers(targets);
    }

    private void setContent(Map<String, Object> body, String content, EnvelopeReq envelopeReq) {
        Map<String, Object> contentMap = new HashMap<>(4);
        contentMap.put("channel", envelopeReq.getChannel().get(0));
        if (StringUtils.isNotBlank(content)) {
            contentMap.put("body", content);
        } else {
            contentMap.put("body", body);
        }
        envelopeReq.setContent(Collections.singletonList(contentMap));
    }
}

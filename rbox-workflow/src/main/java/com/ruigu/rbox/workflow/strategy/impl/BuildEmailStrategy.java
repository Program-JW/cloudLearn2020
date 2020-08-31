package com.ruigu.rbox.workflow.strategy.impl;

import com.ruigu.rbox.workflow.model.entity.NoticeEntity;
import com.ruigu.rbox.workflow.model.enums.NoticeParam;
import com.ruigu.rbox.workflow.model.enums.Symbol;
import com.ruigu.rbox.workflow.model.request.EnvelopeReq;
import com.ruigu.rbox.workflow.model.vo.MessageInfoVO;
import com.ruigu.rbox.workflow.service.NoticeLogService;
import com.ruigu.rbox.workflow.strategy.BuildNoticeStrategy;
import com.ruigu.rbox.workflow.supports.NoticeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author liqingtian
 * @date 2019/10/14 20:04
 */
@Slf4j
@Component("email")
public class BuildEmailStrategy implements BuildNoticeStrategy {

    @Resource
    private NoticeUtil noticeUtil;

    @Resource
    private NoticeLogService noticeLogService;

    @Override
    public EnvelopeReq build(MessageInfoVO message) {

        Collection<Integer> leaders = message.getLeaders();
        Collection<Integer> targets = message.getTargets();
        EnvelopeReq envelope = noticeUtil.getEnvelopReqEmail(leaders, targets);

        // 内容
        Map<String, Object> contentMap = new HashMap<>(16);
        contentMap.put("channel", envelope.getChannel().get(0));
        Map<String, Object> body = new HashMap<>(8);
        String title = message.getTitle();
        body.put(NoticeParam.TITLE.getDesc(), title);
        String content = message.getDescription();
        body.put(NoticeParam.CONTENT.getDesc(), content);
        contentMap.put("body", body);
        envelope.setContent(Collections.singletonList(contentMap));

        // 通知记录
        NoticeEntity notice = new NoticeEntity();
        notice.setTitle(title);
        notice.setContent(content);
        String targetString = StringUtils.join(targets, Symbol.COMMA.getValue());
        if (CollectionUtils.isNotEmpty(leaders)) {
            targetString += Symbol.COMMA.getValue() + StringUtils.join(leaders, Symbol.COMMA.getValue());
        }
        notice.setTargets(targetString);
        String taskId = message.getTaskId();
        if (StringUtils.isNotBlank(message.getTaskId())) {
            notice.setTaskId(taskId);
        }
        notice.setInstanceId(message.getInstanceId());
        notice.setDefinitionId(message.getDefinitionId());
        notice.setNoticeUrl(message.getUrl());
        notice.setCreatedOn(new Date());
        notice.setType(message.getNoticeEventType());
        notice.setStatus((byte) 1);
        noticeLogService.insertNotice(notice);

        return envelope;
    }
}

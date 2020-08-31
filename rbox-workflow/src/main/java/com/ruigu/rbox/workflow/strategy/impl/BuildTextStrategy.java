package com.ruigu.rbox.workflow.strategy.impl;

import com.ruigu.rbox.workflow.feign.HedwigFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.NoticeEntity;
import com.ruigu.rbox.workflow.model.enums.NoticeType;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.Symbol;
import com.ruigu.rbox.workflow.model.request.EnvelopeReq;
import com.ruigu.rbox.workflow.model.vo.MessageInfoVO;
import com.ruigu.rbox.workflow.service.NoticeLogService;
import com.ruigu.rbox.workflow.strategy.BuildNoticeStrategy;
import com.ruigu.rbox.workflow.supports.NoticeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author liqingtian
 * @date 2019/10/14 20:04
 */
@Slf4j
@Component("text")
public class BuildTextStrategy implements BuildNoticeStrategy {

    @Resource
    private NoticeUtil noticeUtil;

    @Resource
    private NoticeLogService noticeLogService;

    @Override
    public EnvelopeReq build(MessageInfoVO message) {

        Collection<Integer> targets = message.getTargets();
        EnvelopeReq envelope = noticeUtil.getEnvelopReqWeixin(NoticeType.TEXT, targets);

        // 设置内容
        Map<String, Object> contentMap = new HashMap<>(16);
        contentMap.put("channel", envelope.getChannel().get(0));
        String content = message.getDescription();
        contentMap.put("body", content);
        envelope.setContent(Collections.singletonList(contentMap));

        // 保存通知日志
        NoticeEntity notice = new NoticeEntity();
        notice.setContent(content);
        notice.setTargets(StringUtils.join(targets, Symbol.COMMA.getValue()));
        if (StringUtils.isNotBlank(message.getTaskId())) {
            notice.setTaskId(message.getTaskId());
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

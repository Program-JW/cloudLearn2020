package com.ruigu.rbox.workflow.strategy.impl;

import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.feign.HedwigFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.NoticeEntity;
import com.ruigu.rbox.workflow.model.enums.NoticeParam;
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
@Component("textCard")
public class BuildTextCardStrategy implements BuildNoticeStrategy {

    @Resource
    private NoticeUtil noticeUtil;

    @Resource
    private NoticeLogService noticeLogService;

    @Override
    public EnvelopeReq build(MessageInfoVO message) {

        // 获取相应类型消息发送实体
        Collection<Integer> targets = message.getTargets();
        EnvelopeReq envelope = noticeUtil.getEnvelopReqWeixin(NoticeType.TEXT_CARD, targets);

        // 设置内容
        Map<String, Object> contentMap = new HashMap<>(16);
        contentMap.put("channel", envelope.getChannel().get(0));
        Map<String, Object> body = new HashMap<>(8);
        String title = message.getTitle();
        body.put(NoticeParam.TITLE.getDesc(), title);
        String content = message.getDescription();
        body.put(NoticeParam.DESCRIPTION.getDesc(), content);
        String url = message.getUrl();
        if (StringUtils.isBlank(url)) {
            throw new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "URL缺失，发送失败");
        }
        body.put(NoticeParam.URL.getDesc(), url);
        body.put(NoticeParam.BTN_TXT.getDesc(), "详情");
        contentMap.put("body", body);
        envelope.setContent(Collections.singletonList(contentMap));

        // 保存
        NoticeEntity notice = new NoticeEntity();
        notice.setTargets(StringUtils.join(targets.toArray(new Integer[0]), Symbol.COMMA.getValue()));
        String taskId = message.getTaskId();
        if (StringUtils.isNotBlank(taskId)) {
            notice.setTaskId(taskId);
        }
        notice.setInstanceId(message.getInstanceId());
        notice.setDefinitionId(message.getDefinitionId());
        notice.setTitle(title);
        notice.setContent(content);
        notice.setNoticeUrl(url);
        notice.setType(message.getNoticeEventType());
        notice.setCreatedOn(new Date());
        notice.setStatus((byte) 1);
        noticeLogService.insertNotice(notice);

        return envelope;
    }
}

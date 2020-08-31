package com.ruigu.rbox.workflow.strategy.impl;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.model.bpmn.Button;
import com.ruigu.rbox.workflow.model.entity.NoticeEntity;
import com.ruigu.rbox.workflow.model.enums.*;
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
 * @date 2019/10/14 20:03
 */
@Slf4j
@Component("taskCard")
public class BuildTaskCardStrategy implements BuildNoticeStrategy {

    @Resource
    private NoticeUtil noticeUtil;

    @Resource
    private NoticeLogService noticeLogService;

    @Override
    public EnvelopeReq build(MessageInfoVO message) {

        // 检验数据正确性
        String taskId = message.getTaskId();
        String instanceId = message.getInstanceId();
        if (StringUtils.isBlank(taskId) && StringUtils.isBlank(instanceId)) {
            log.error("| - 任务卡片通知： [ 报错信息： 缺少任务ID或实例ID，无法发送任务卡片通知。]");
            throw new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "异常，缺少任务ID或实例ID");
        }
        String buttonConfig = message.getButtonConfig();
        if (StringUtils.isBlank(buttonConfig)) {
            log.error("| - 任务卡片通知： [ 报错信息： 缺少卡片按钮配置信息，无法发送任务卡片通知。]" +
                    " [ ID: {} - {} ] ", instanceId, taskId);
            throw new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "异常，缺少卡片按钮配置");
        }

        // 获取相应类型消息发送实体
        Collection<Integer> targets = message.getTargets();
        EnvelopeReq envelope = noticeUtil.getEnvelopReqWeixin(NoticeType.TASK_CARD, targets);

        // 设置内容
        Map<String, Object> contentMap = new HashMap<>(16);
        contentMap.put("channel", envelope.getChannel().get(0));
        Map<String, Object> body = new HashMap<>(8);
        String title = message.getTitle();
        body.put(NoticeParam.TITLE.getDesc(), title);
        String content = message.getDescription();
        body.put(NoticeParam.DESCRIPTION.getDesc(), content);
        String url = message.getUrl();
        body.put(NoticeParam.URL.getDesc(), url);
        List<Button> buttons = JsonUtil.parseArray(buttonConfig, Button.class);
        List<Map<String, Object>> buttonMapList = new ArrayList<>();
        buttons.forEach(button -> {
            Map<String, Object> btn = new HashMap<>(8);
            btn.put(NoticeParam.BTN_KEY.getDesc(), button.getKey());
            btn.put(NoticeParam.BTN_NAME.getDesc(), button.getName());
            btn.put(NoticeParam.BTN_REPLACE_NAME.getDesc(), button.getReplaceName());
            buttonMapList.add(btn);
        });
        body.put(NoticeParam.BTN.getDesc(), buttonMapList);

        // button配置中冗余了按钮所对应事件
        String eventCode = buttons.get(0).getButtonEvent();
        String taskIdWeiXin = UUID.randomUUID().toString().replace("-", "");
        // 任务id后缀为按钮所触发的service
        taskIdWeiXin = WeixinMsgElement.TASK_ID_PREFIX.getDesc() + taskIdWeiXin + Symbol.UNDERLINE.getValue() + eventCode;
        body.put(NoticeParam.TASK_ID.getDesc(), taskIdWeiXin);
        contentMap.put("body", body);
        envelope.setContent(Collections.singletonList(contentMap));

        // 任务卡片需要先进行记录保存，再发送，因为记录中保存了加了假的任务id
        // 通知记录
        NoticeEntity notice = new NoticeEntity();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setNoticeUrl(url);
        notice.setTaskIdWeixin(taskIdWeiXin);
        notice.setTargets(StringUtils.join(targets.toArray(new Integer[0]), Symbol.COMMA.getValue()));
        notice.setTaskId(taskId);
        notice.setInstanceId(instanceId);
        notice.setDefinitionId(message.getDefinitionId());
        notice.setType(message.getNoticeEventType());
        notice.setStatus((byte) 1);
        notice.setCreatedOn(new Date());

        if (noticeLogService.insertNotice(notice) == -1) {
            log.error("| - 任务卡片通知： [ 报错信息： Notice 保存失败，将不再进行微信卡片通知。] ");
            throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(),
                    "通知实体保存失败，任务卡片通知禁止发送。");
        } else {
            return envelope;
        }
    }
}

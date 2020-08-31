package com.ruigu.rbox.workflow.service.script;

import com.ruigu.rbox.workflow.model.enums.NoticeType;
import com.ruigu.rbox.workflow.service.QuestNoticeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/12/30 20:40
 */
@Component
public class BaseScriptTaskService {

    @Resource
    private QuestNoticeService questNoticeService;

    public void send(int type, Map<String, Object> data, Map<String, Object> variables) {



        if (type == NoticeType.TASK_CARD.getState()) {

        } else if (type == NoticeType.TEXT_CARD.getState()) {

        } else if (type == NoticeType.TEXT.getState()) {

        } else {

        }
    }
}

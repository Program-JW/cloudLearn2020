package com.ruigu.rbox.workflow.strategy.context;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.TaskCardReturnMessageDTO;
import com.ruigu.rbox.workflow.model.entity.NoticeEntity;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.Symbol;
import com.ruigu.rbox.workflow.model.enums.TaskCardHandleEvent;
import com.ruigu.rbox.workflow.strategy.TaskCardHandleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/12/31 16:56
 */
@Slf4j
@Component
public class TaskCardHandleContext {

    @Resource
    private Map<String, TaskCardHandleStrategy> map;

    public ServerResponse handle(TaskCardReturnMessageDTO message, NoticeEntity notice) {
        String errorLogHead = "| - > [ 任务卡片事件处理 ] [ TaskCardHandleContext ] - ";
        int splitCount = 2;
        String[] group = message.getTaskId().trim().split(Symbol.UNDERLINE.getValue());
        if (group.length != splitCount) {
            String errMsg = "异常，配置错误或没有找到相应处理类";
            log.error(errorLogHead + errMsg);
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), errMsg);
        }
        String event = TaskCardHandleEvent.getValue(Integer.valueOf(group[1]));
        if (StringUtils.isNotBlank(event) && map.containsKey(event) && map.get(event) != null) {
            return map.get(event).handle(message, notice);
        } else {
            String errMsg = "异常，没有找到相应处理类";
            log.error(errorLogHead + errMsg);
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), errMsg);
        }
    }
}

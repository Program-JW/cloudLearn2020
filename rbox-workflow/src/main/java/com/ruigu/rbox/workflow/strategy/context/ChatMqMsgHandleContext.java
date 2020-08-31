package com.ruigu.rbox.workflow.strategy.context;

import com.ruigu.rbox.workflow.model.dto.MsgNotifyDTO;
import com.ruigu.rbox.workflow.model.enums.ChatMqMsgHandleEnum;
import com.ruigu.rbox.workflow.strategy.ChatMqMsgHandleStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2020/02/04 9:50
 */
@Service
public class ChatMqMsgHandleContext {

    @Resource
    private Map<String, ChatMqMsgHandleStrategy> map;

    public void handle(Integer action, MsgNotifyDTO msgNotifyDTO) {
        if (action != null) {
            String strategy = ChatMqMsgHandleEnum.getHandle(action);
            if (StringUtils.isNotBlank(strategy)) {
                map.get(strategy).handle(msgNotifyDTO);
            }
        }
    }
}

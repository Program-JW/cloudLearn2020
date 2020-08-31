package com.ruigu.rbox.workflow.strategy.impl;

import com.ruigu.rbox.workflow.model.dto.LightningUnreadMessageDTO;
import com.ruigu.rbox.workflow.model.dto.MsgNotifyDTO;
import com.ruigu.rbox.workflow.model.enums.ChatContentTypeEnum;
import com.ruigu.rbox.workflow.model.enums.Symbol;
import com.ruigu.rbox.workflow.service.LightningIssueService;
import com.ruigu.rbox.workflow.strategy.ChatMqMsgHandleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/02/04 10:03
 */
@Slf4j
@Service
public class UnreadMsgHandleStrategy implements ChatMqMsgHandleStrategy {

    @Resource
    private LightningIssueService lightningIssueService;

    @Override
    public void handle(MsgNotifyDTO msgNotifyDTO) {
        // 处理
        LightningUnreadMessageDTO unreadMessageDTO = new LightningUnreadMessageDTO();
        unreadMessageDTO.setGroupId(msgNotifyDTO.getGroupId());
        unreadMessageDTO.setSendTime(msgNotifyDTO.getSendTime());
        // 如果有默认值，则设置未默认值
        String defaultContent = ChatContentTypeEnum.queryValue(msgNotifyDTO.getContentType());
        if (StringUtils.isNotBlank(defaultContent)) {
            unreadMessageDTO.setContent(defaultContent);
        } else {
            unreadMessageDTO.setContent(msgNotifyDTO.getContent());
        }
        // 解析发送人id
        try {
            // 解析发送人
            unreadMessageDTO.setFromUser(getUserId(msgNotifyDTO.getFromConnName()));
            // 解析送达人id
            List<String> offUserList = msgNotifyDTO.getOffUserList();
            if (CollectionUtils.isEmpty(offUserList)) {
                return;
            }
            List<Integer> toUserList = new ArrayList<>();
            try {
                for (String user : offUserList) {
                    toUserList.add(getUserId(user));
                }
            } catch (Exception e) {
                log.error("| - > [ 闪电链 ] [ 未读消息通知 ] 数据解析异常：{}", e);
                return;
            }
            unreadMessageDTO.setToUserList(toUserList);
            lightningIssueService.unreadMessageNotice(unreadMessageDTO);
        } catch (Exception e) {
            log.error("| - > [ 闪电链 ] [ 未读消息通知 ] 数据解析异常：{}", e);
        }
    }

    private Integer getUserId(String userName) throws Exception {
        String[] split = userName.split(Symbol.HYPHEN.getValue());
        return Integer.valueOf(split[1]);
    }
}

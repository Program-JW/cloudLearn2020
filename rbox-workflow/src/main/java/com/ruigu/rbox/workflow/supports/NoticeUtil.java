package com.ruigu.rbox.workflow.supports;

import com.ruigu.rbox.workflow.model.enums.NoticeType;
import com.ruigu.rbox.workflow.model.request.EnvelopeReq;
import com.ruigu.rbox.workflow.model.request.UserReq;
import com.ruigu.rbox.workflow.model.vo.EmailAttachment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author liqingtian
 * @date 2019/12/30 17:31
 */
@Component
public class NoticeUtil {

    @Value("${rbox.msg.source}")
    private String admin;

    @Value("${rbox.msg.account}")
    private String msgAccount;

    @Value("${rbox.msg.weixin.channal}")
    private String msgChannel;

    @Value("${rbox.msg.email.channal}")
    private String msgEmailChannel;

    public EnvelopeReq getEnvelopReqEmail(Collection<Integer> cc, Collection<Integer> targets) {
        EnvelopeReq envelopeReq = getEnvelopReq(targets);
        // 渠道
        envelopeReq.setChannel(Collections.singletonList(msgEmailChannel));
        // 扩展
        Map<String, Object> extEmailMap = new HashMap<>(8);
        extEmailMap.put("cc", cc);
        extEmailMap.put("mailType", "0");
        Map<String, Object> extMap = new HashMap<>(8);
        extMap.put("email", extEmailMap);
        envelopeReq.setExt(extMap);
        return envelopeReq;
    }

    public EnvelopeReq getEnvelopReqEmail(Collection<Integer> cc, Collection<Integer> targets, Collection<EmailAttachment> attachments) {
        EnvelopeReq envelopeReq = getEnvelopReq(targets);
        // 渠道
        envelopeReq.setChannel(Collections.singletonList(msgEmailChannel));
        // 扩展
        Map<String, Object> extEmailMap = new HashMap<>(8);
        extEmailMap.put("cc", cc);
        extEmailMap.put("mailType", "0");
        boolean hasAttachments = attachments != null && !attachments.isEmpty();
        if (hasAttachments) {
            extEmailMap.put("attachments", attachments);
        }
        Map<String, Object> extMap = new HashMap<>(8);
        extMap.put("email", extEmailMap);
        envelopeReq.setExt(extMap);
        return envelopeReq;
    }

    public EnvelopeReq getEnvelopReqWeixin(NoticeType noticeType, Collection<Integer> targets) {
        EnvelopeReq envelopeReq = getEnvelopReq(targets);
        // 渠道
        envelopeReq.setChannel(Collections.singletonList(msgChannel));
        // 扩展
        Map<String, Object> extMap = new HashMap<>(4);
        Map<String, Object> weixinMap = new HashMap<>(8);
        weixinMap.put("enableIdTrans", 0);
        weixinMap.put("safe", 0);
        weixinMap.put("msgType", noticeType.getDesc());
        extMap.put("work-weixin", weixinMap);
        envelopeReq.setExt(extMap);
        return envelopeReq;
    }

    private EnvelopeReq getEnvelopReq(Collection<Integer> targets) {
        EnvelopeReq envelope = new EnvelopeReq();
        // 设置送达目标
        List<UserReq> userList = new ArrayList<>();
        targets.stream().distinct().forEach(id -> {
            UserReq user = new UserReq();
            user.setId(id);
            userList.add(user);
        });
        envelope.setUsers(userList);
        // 设置通用属性
        envelope.setSource(admin);
        envelope.setGroup(new ArrayList<>());
        envelope.setScope(0);
        envelope.setMsgType(1);
        // 生成msgId
        long timeMillis = System.currentTimeMillis();
        long random = ThreadLocalRandom.current().nextLong(99L);
        String msgId = timeMillis + "" + random;
        envelope.setMsgId(Long.valueOf(msgId));
        envelope.setAccount(msgAccount);
        return envelope;
    }
}

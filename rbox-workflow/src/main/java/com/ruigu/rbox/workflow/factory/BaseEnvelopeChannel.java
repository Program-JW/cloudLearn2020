package com.ruigu.rbox.workflow.factory;

import com.ruigu.rbox.workflow.config.HedwigConfigProperties;
import com.ruigu.rbox.workflow.model.enums.EnvelopeTypeEnum;
import com.ruigu.rbox.workflow.model.request.EnvelopeReq;
import com.ruigu.rbox.workflow.model.request.UserReq;
import com.ruigu.rbox.workflow.model.vo.EmailAttachment;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author liqingtian
 * @date 2020/08/14 17:53
 */
public abstract class BaseEnvelopeChannel implements EnvelopeChannel {

    @Resource
    private HedwigConfigProperties hedwigConfigProperties;

    protected EnvelopeReq buildWxEnvelope(EnvelopeTypeEnum type) {

        EnvelopeReq envelopeReq = buildEnvelope();
        // 扩展
        Map<String, Object> extMap = new HashMap<>(4);
        Map<String, Object> weixinMap = new HashMap<>(8);
        weixinMap.put("enableIdTrans", 0);
        weixinMap.put("safe", 0);
        weixinMap.put("msgType", type.getValue());
        extMap.put("work-weixin", weixinMap);
        envelopeReq.setExt(extMap);

        return envelopeReq;
    }

    protected EnvelopeReq buildEmailEnvelope() {
        EnvelopeReq envelopeReq = buildEnvelope();
        // 扩展
//        Map<String, Object> extEmailMap = new HashMap<>(8);
//        extEmailMap.put("cc", cc);
//        extEmailMap.put("mailType", "0");
//        boolean hasAttachments = attachments != null && !attachments.isEmpty();
//        if (hasAttachments) {
//            extEmailMap.put("attachments", attachments);
//        }
//        Map<String, Object> extMap = new HashMap<>(8);
//        extMap.put("email", extEmailMap);
//        envelopeReq.setExt(extMap);
        return envelopeReq;
    }

    private EnvelopeReq buildEnvelope() {
        EnvelopeReq envelope = hedwigConfigProperties.getReq();
        // 生成msgId
        String id = System.currentTimeMillis() + "" + ThreadLocalRandom.current().nextLong(99L);
        envelope.setMsgId(Long.valueOf(id));
        return envelope;
    }
}

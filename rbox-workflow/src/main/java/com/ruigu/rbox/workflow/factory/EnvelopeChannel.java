package com.ruigu.rbox.workflow.factory;

import com.ruigu.rbox.workflow.model.enums.EnvelopeChannelEnum;
import com.ruigu.rbox.workflow.model.enums.EnvelopeTypeEnum;
import com.ruigu.rbox.workflow.model.request.EnvelopeReq;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/14 17:01
 */
public interface EnvelopeChannel {

    /**
     * 是否匹配
     *
     * @param channel 渠道
     * @return 是否
     */
    Boolean match(EnvelopeChannelEnum channel);

    /**
     * 选择生产者
     *
     * @return 生产者
     */
    EnvelopeReq product(EnvelopeTypeEnum type);
}

package com.ruigu.rbox.workflow.factory;

import com.ruigu.rbox.workflow.model.enums.EnvelopeChannelEnum;
import com.ruigu.rbox.workflow.model.enums.EnvelopeTypeEnum;
import com.ruigu.rbox.workflow.model.request.EnvelopeReq;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/14 17:17
 */
@Component
public final class EnvelopeFactory {

    @Resource
    private List<EnvelopeChannel> channels;

    public final EnvelopeReq create(EnvelopeChannelEnum channel, EnvelopeTypeEnum type) {

        return channels.stream()
                .filter(c -> c.match(channel))
                .findFirst()
                .orElseThrow(NullPointerException::new)
                .product(type);

    }
}

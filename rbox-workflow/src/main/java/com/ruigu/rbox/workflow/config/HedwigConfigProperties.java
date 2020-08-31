package com.ruigu.rbox.workflow.config;

import com.ruigu.rbox.workflow.model.request.EnvelopeReq;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 消息中心发送消息的入参配置
 *
 * @author GongXincheng
 * @since 2019-10-23 17:19
 */
@Data
@Component
@ConfigurationProperties(prefix = "hedwig")
public class HedwigConfigProperties {

    private EnvelopeReq req;

    private ChannelInfo ttbChannel;

    private ChannelInfo sasChannel;

    @Data
    public static class ChannelInfo {
        private String source;
        private String account;
        private String wxChannel;
        private String emailChannel;
    }
}

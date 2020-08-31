package com.ruigu.rbox.workflow.factory;

import com.ruigu.rbox.workflow.config.HedwigConfigProperties;
import com.ruigu.rbox.workflow.model.enums.EnvelopeChannelEnum;
import com.ruigu.rbox.workflow.model.enums.EnvelopeTypeEnum;
import com.ruigu.rbox.workflow.model.request.EnvelopeReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/14 17:05
 */
@Slf4j
@Component
public class SasEnvelopeChannel extends BaseEnvelopeChannel {

    // 放入创建者
    private List<EnvelopeProduct> products = new ArrayList<>();

    {
        try {
            Class[] productClasses = SasEnvelopeChannel.class.getDeclaredClasses();
            // 逐个创建并放入list
            for (Class p : productClasses) {
                // 根据构造器实例化内部类
                Object inner = p.getDeclaredConstructors()[0].newInstance(this);
                // 判断内部类是否继承生产者接口
                if (inner instanceof EnvelopeProduct) {
                    products.add((EnvelopeProduct) inner);
                }
            }
        } catch (Exception e) {
            log.error("初始化 workflow envelope product 失败。e:{}", e);
        }
    }

    @Resource
    private HedwigConfigProperties hedwigConfigProperties;

    @Override
    public Boolean match(EnvelopeChannelEnum channel) {
        return EnvelopeChannelEnum.SPECIAL_AFTER_SALE == channel;
    }

    @Override
    public EnvelopeReq product(EnvelopeTypeEnum type) {
        return products.stream()
                .filter(p -> p.match(type))
                .findFirst()
                .orElseThrow(NullPointerException::new)
                .product();
    }

    class WeixinTextEnvelop implements EnvelopeProduct {

        @Override
        public Boolean match(EnvelopeTypeEnum type) {
            return EnvelopeTypeEnum.WEIXIN_TEXT == type;
        }

        @Override
        public EnvelopeReq product() {
            // 获取消息模板
            EnvelopeReq envelopeReq = buildWxEnvelope(EnvelopeTypeEnum.WEIXIN_TEXT);
            // 设置特殊参数
            envelopeReq.setSource(hedwigConfigProperties.getSasChannel().getSource());
            envelopeReq.setAccount(hedwigConfigProperties.getSasChannel().getAccount());
            envelopeReq.setChannel(Collections.singletonList(hedwigConfigProperties.getSasChannel().getWxChannel()));
            return envelopeReq;
        }
    }

    class WeixinTaskCardEnvelop implements EnvelopeProduct {

        @Override
        public Boolean match(EnvelopeTypeEnum type) {
            return EnvelopeTypeEnum.WEIXIN_TASK_CARD == type;
        }

        @Override
        public EnvelopeReq product() {
            // 获取消息模板
            EnvelopeReq envelopeReq = buildWxEnvelope(EnvelopeTypeEnum.WEIXIN_TASK_CARD);
            // 设置特殊参数
            envelopeReq.setSource(hedwigConfigProperties.getSasChannel().getSource());
            envelopeReq.setAccount(hedwigConfigProperties.getSasChannel().getAccount());
            envelopeReq.setChannel(Collections.singletonList(hedwigConfigProperties.getSasChannel().getWxChannel()));
            return envelopeReq;
        }
    }

    class WeixinTextCardEnvelop implements EnvelopeProduct {

        @Override
        public Boolean match(EnvelopeTypeEnum type) {
            return EnvelopeTypeEnum.WEIXIN_TEXT_CARD == type;
        }

        @Override
        public EnvelopeReq product() {
            // 获取消息模板
            EnvelopeReq envelopeReq = buildWxEnvelope(EnvelopeTypeEnum.WEIXIN_TEXT_CARD);
            // 设置特殊参数
            envelopeReq.setSource(hedwigConfigProperties.getSasChannel().getSource());
            envelopeReq.setAccount(hedwigConfigProperties.getSasChannel().getAccount());
            envelopeReq.setChannel(Collections.singletonList(hedwigConfigProperties.getSasChannel().getWxChannel()));
            return envelopeReq;
        }
    }

    class EmailEnvelop implements EnvelopeProduct {

        @Override
        public Boolean match(EnvelopeTypeEnum type) {
            return EnvelopeTypeEnum.EMAIL == type;
        }

        @Override
        public EnvelopeReq product() {
            // 获取消息模板
            EnvelopeReq envelopeReq = buildEmailEnvelope();
            // 设置特殊参数
            envelopeReq.setSource(hedwigConfigProperties.getSasChannel().getSource());
            envelopeReq.setAccount(hedwigConfigProperties.getSasChannel().getAccount());
            envelopeReq.setChannel(Collections.singletonList(hedwigConfigProperties.getSasChannel().getEmailChannel()));
            return envelopeReq;
        }
    }
}

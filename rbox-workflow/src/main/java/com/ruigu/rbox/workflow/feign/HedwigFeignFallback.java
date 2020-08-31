package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.model.ServerResponse;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author liqingtian
 * @date 2019/09/30 16:45
 */
@Component
public class HedwigFeignFallback implements FallbackFactory<HedwigFeignClient> {
    @Override
    public HedwigFeignClient create(Throwable throwable) {
        String errMsgHead = "远程调用 - 消息中心 - 异常 - ";
        return new HedwigFeignClient() {
            @Override
            public ServerResponse sendMsg(Object req) {
                return ServerResponse.fail(500, errMsgHead + "发送消息失败。" + throwable.toString());
            }
        };
    }
}

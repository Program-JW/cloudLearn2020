package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.UpdateStatusMsg;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author liqingtian
 * @date 2019/09/30 16:54
 */
@Component
public class WeixinFeignFallback implements FallbackFactory<WeixinFeignClient> {
    @Override
    public WeixinFeignClient create(Throwable throwable) {

        return new WeixinFeignClient() {
            String errMsgHead = "远程调用 - 微信 - 异常 - ";

            @Override
            public ServerResponse sendUpdateStatusMsg(UpdateStatusMsg msg) {
                return ServerResponse.fail(500, errMsgHead + "更新微信卡片状态失败。" + throwable.toString());
            }
        };
    }
}

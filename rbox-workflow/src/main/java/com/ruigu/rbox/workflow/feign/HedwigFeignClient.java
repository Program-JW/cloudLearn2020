package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.model.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author liqingtian
 * @date 2019/09/06 18:26
 */
@Component
@FeignClient(value = "hedwig", url = "${rbox.hedwig.feign.url:}", fallbackFactory = HedwigFeignFallback.class)
public interface HedwigFeignClient {

    /**
     * 发送微信卡片消息
     *
     * @param req 消息实体
     * @return 相应的用户信息
     */
    @RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
    ServerResponse sendMsg(@RequestBody Object req);
}

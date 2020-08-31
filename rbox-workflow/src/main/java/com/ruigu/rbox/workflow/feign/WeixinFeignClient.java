package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.UpdateStatusMsg;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author liqingtian
 * @date 2019/09/17 14:39
 */
@Component
@FeignClient(value = "weixin", url = "${rbox.weixin.feign.url:}")
public interface WeixinFeignClient {

    /**
     * 更新任务卡片消息状态
     *
     * @param msg 更新任务卡片状态实体
     * @return ServerResponse
     */
    @RequestMapping(value = "/message/updateTaskCard", method = RequestMethod.POST)
    ServerResponse sendUpdateStatusMsg(@RequestBody UpdateStatusMsg msg);
}

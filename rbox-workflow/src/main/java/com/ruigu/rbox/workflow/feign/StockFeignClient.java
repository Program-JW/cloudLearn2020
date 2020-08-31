package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.model.ServerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/11/02 21:08
 */
@Component
@FeignClient(name = "stock", url = "${rbox.stock.feign.url:}", fallbackFactory = StockFeignFallback.class)
public interface StockFeignClient {

    /**
     * 发布增减库存数量
     *
     * @param data 请求参数
     * @return 调用返回结果 -1 错误 200 成功
     */
    @RequestMapping(value = "/sys/stock/publishStockCount", method = RequestMethod.POST)
    ServerResponse publishStockCount(@RequestBody Map<String, Object> data);

    /**
     * 导入锁定的库存列表
     *
     * @param data
     * @return
     */
    @PostMapping(value = "/operate/importLockStock",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ServerResponse importLockStock(MultiValueMap<String, Object> data);

    /**
     * 删除锁定的库存列表
     *
     * @param data
     * @return
     */
    @PostMapping("/operate/delLockStock")
    ServerResponse delLockStock(@RequestBody List<Map<String, Object>> data);

    /**
     * 获取锁定的库存列表
     *
     * @param data
     * @return
     */
    @GetMapping("/operate/listLockStock")
    ServerResponse listLockStock(Map<String, Object> data);
}

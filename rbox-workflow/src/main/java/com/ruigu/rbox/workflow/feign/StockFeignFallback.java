package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.model.ServerResponse;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhenya
 * @date 2019/11/18 20:47
 */

@Slf4j
@Component
public class StockFeignFallback implements FallbackFactory<StockFeignClient> {
    @Override
    public StockFeignClient create(Throwable throwable) {

        String errMsgHead = "远程调用 - 库存 - 异常 - ";

        return new StockFeignClient() {
            @Override
            public ServerResponse publishStockCount(Map<String, Object> data) {
                return ServerResponse.fail(500, errMsgHead + "发布增减库存数量。" + throwable.toString());
            }

            @Override
            public ServerResponse importLockStock(MultiValueMap<String, Object> data) {
                return ServerResponse.fail(500, errMsgHead + "导入锁定的库存列表。" + throwable.toString());
            }

            @Override
            public ServerResponse delLockStock(List<Map<String, Object>> data) {
                return ServerResponse.fail(500, errMsgHead + "删除锁定的库存列表。" + throwable.toString());
            }

            @Override
            public ServerResponse listLockStock(Map<String, Object> data) {
                return ServerResponse.fail(500, errMsgHead + "获取锁定的库存列表。" + throwable.toString());
            }
        };
    }
}

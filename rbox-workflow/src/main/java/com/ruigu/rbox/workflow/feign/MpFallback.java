package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.request.QuerySkuDetailRequest;
import com.ruigu.rbox.workflow.model.request.UpdatePurchaseOrderStatusRequest;
import com.ruigu.rbox.workflow.model.vo.PurchaseReqVO;
import com.ruigu.rbox.workflow.model.vo.SkuDetailVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author liqingtian
 * @date 2019/09/30 16:50
 */
@Component
public class MpFallback implements FallbackFactory<MpClient> {
    @Override
    public MpClient create(Throwable throwable) {
        String errMsgHead = "远程调用 - MP系统 - 异常 - ";
        return new MpClient() {
            @Override
            public ServerResponse<Object> getPurchaseOrderDetailInfo(PurchaseReqVO vo) {
                return ServerResponse.fail(500, errMsgHead + "获取订单详情失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<Object> updatePurchaseOrderStatus(UpdatePurchaseOrderStatusRequest vo) {
                return ServerResponse.fail(500, errMsgHead + "更改订单状态失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<SkuDetailVo> getSkuDetailInfo(QuerySkuDetailRequest request) {
                return ServerResponse.fail(500, errMsgHead + "获取SKU信息失败。" + throwable.toString());
            }
        };
    }
}

package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.config.FeignAuthConfiguration;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.request.QuerySkuDetailRequest;
import com.ruigu.rbox.workflow.model.request.UpdatePurchaseOrderStatusRequest;
import com.ruigu.rbox.workflow.model.vo.PurchaseReqVO;
import com.ruigu.rbox.workflow.model.vo.SkuDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author ：jianghuilin
 * @date ：Created in {2019/9/17} {18:03}
 */
@Component
@FeignClient(name = "mp", url = "${server.mp.api.url}", configuration = FeignAuthConfiguration.class, fallbackFactory = MpFallback.class)
public interface MpClient {

    /**
     * 获取mp采购单详情
     *
     * @param vo 采购单编号
     * @return ServerResponse
     */
    @PostMapping("/outsideinterface/get_order_detail_look")
    ServerResponse<Object> getPurchaseOrderDetailInfo(PurchaseReqVO vo);

    /**
     * 更改mp采购单状态
     *
     * @param vo 采购单编号,是否通过
     * @return ServerResponse
     */
    @PostMapping("/outsideinterface/update_order_status")
    ServerResponse<Object> updatePurchaseOrderStatus(UpdatePurchaseOrderStatusRequest vo);

    /**
     * 封装从MP查询sku
     * @param  request 参数对象
     * @return 商品详情
     */
    @GetMapping("/outsideinterface/get_product_info")
    ServerResponse<SkuDetailVo> getSkuDetailInfo(QuerySkuDetailRequest request);

}



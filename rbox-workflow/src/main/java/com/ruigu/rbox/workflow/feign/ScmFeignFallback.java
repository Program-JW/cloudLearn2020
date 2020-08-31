package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.StockChangeSkuNotEmptyDTO;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.request.CheckSubSkuCountMapRequest;
import com.ruigu.rbox.workflow.model.request.StockChangeLastApplyRequest;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhenya
 * @date 2019/11/19 17:15
 */

@Slf4j
@Component
public class ScmFeignFallback implements FallbackFactory<ScmFeignClient> {
    @Override
    public ScmFeignClient create(Throwable throwable) {

        String errMsgHead = "远程调用 - scm - 异常 - ";

        return new ScmFeignClient() {
            @Override
            public ServerResponse listLockStock(Map<String, Object> data) {
                return ServerResponse.fail(500, errMsgHead + "分页查询锁库数据。" + throwable.toString());
            }

            @Override
            public ServerResponse saveApiLog(List<ApiLogEntity> apiLogEntity) {
                return ServerResponse.fail(500, errMsgHead + "日志记录失败" + throwable.toString());
            }

            @Override
            public ServerResponse recordAll(List<StockLockApplyEntity> stockLockApplyReq) {
                return ServerResponse.fail(500, errMsgHead + "记录锁库数据。" + throwable.toString());
            }

            @Override
            public ServerResponse<List<StockLockApplyEntity>> getStockLockApplyListByIds(List<Integer> applyIds) {
                return ServerResponse.fail(500, errMsgHead + "通过id查询锁库数据。" + throwable.toString());
            }

            @Override
            public ServerResponse<StockChangeApplyEntity> getStockChangeApplyById(Integer applyId) {
                return ServerResponse.fail(500, errMsgHead + "通过id查询变更库存数据。" + throwable.toString());
            }

            @Override
            public ServerResponse saveChangeApply(StockChangeApplyEntity stockChangeApplyEntity) {
                return ServerResponse.fail(500, errMsgHead + "保存库存变更申请。" + throwable.toString());
            }

            @Override
            public ServerResponse saveChangeLog(StockChangeLogEntity stockChangeLogEntity) {
                return ServerResponse.fail(500, errMsgHead + "保存库存变更日志。" + throwable.toString());
            }

            @Override
            public ServerResponse refreshSkuUnbalancedFlag(Integer storageId, Integer skuCode) {
                return ServerResponse.fail(500, errMsgHead + "更新拉平字段。" + throwable.toString());
            }

            @Override
            public ServerResponse checkSubSkuCountMap(CheckSubSkuCountMapRequest checkSubSkuCountMapRequest) {
                return ServerResponse.fail(500, errMsgHead + "去库存检验。" + throwable.toString());
            }

            @Override
            public ServerResponse saveChangeDetailAll(List<StockChangeDetailEntity> detailList) {
                return ServerResponse.fail(500, errMsgHead + "保存库存变更详细。" + throwable.toString());
            }

            @Override
            public ServerResponse<List<StockChangeSkuNotEmptyDTO>> getThisMonthSkuNotEmpty() {
                return ServerResponse.fail(500, errMsgHead + "获取本月剩余sku统计失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<List<StockChangeApplyEntity>> batchGetLastRecords(List<StockChangeLastApplyRequest> request) {
                return ServerResponse.fail(500, errMsgHead + "批量获取最后一条申请记录失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<StockChangeApplyEntity> noticeExecute(Integer applyId) {
                return ServerResponse.fail(500, errMsgHead + "通知执行失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<StockLockApplyEntity> noticeLockExecute(Integer applyId) {
                return ServerResponse.fail(500, errMsgHead + "通知执行锁定库存失败。" + throwable.toString());
            }
        };
    }
}

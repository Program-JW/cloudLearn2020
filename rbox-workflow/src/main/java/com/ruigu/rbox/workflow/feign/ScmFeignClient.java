package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.StockChangeSkuNotEmptyDTO;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.request.CheckSubSkuCountMapRequest;
import com.ruigu.rbox.workflow.model.request.StockChangeLastApplyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author heyi
 * @date 2019/11/19 10:29
 */
@Component
@FeignClient(name = "scm", fallbackFactory = ScmFeignFallback.class)
public interface ScmFeignClient {

    /**
     * 获取锁定的库存列表
     *
     * @param data 参数信息
     * @return 分页查询结果
     */
    @GetMapping("/stock-lock-apply/search")
    ServerResponse listLockStock(@RequestParam Map<String, Object> data);

    /**
     * 保存库存变更日志
     *
     * @param apiLogEntity 库存变更日志
     * @return
     */
    @PostMapping("/scm-api/log")
    ServerResponse saveApiLog(@RequestBody List<ApiLogEntity> apiLogEntity);

    /**
     * 记录锁库数据
     *
     * @param stockLockApplyReq 需要保存的实体
     * @return 保存后的id数组
     */
    @PostMapping("/stock-lock-apply/stock-lock-data")
    ServerResponse<List<Integer>> recordAll(@RequestBody List<StockLockApplyEntity> stockLockApplyReq);

    /**
     * 通过id获取实体
     *
     * @param applyIds 申请id
     * @return 申请实体
     */
    @GetMapping("/stock-lock-apply/stock-lock-data")
    ServerResponse<List<StockLockApplyEntity>> getStockLockApplyListByIds(@RequestParam("applyIds") List<Integer> applyIds);

    /**
     * 通过id查询请求
     *
     * @param applyId 请求id
     * @return 请求实体
     */
    @GetMapping("/stock-change-apply/apply/{applyId}")
    ServerResponse<StockChangeApplyEntity> getStockChangeApplyById(@PathVariable("applyId") Integer applyId);

    /**
     * 保存申请
     *
     * @param stockChangeApplyEntity 申请实体
     * @return 申请ID
     */
    @PostMapping("/stock-change-apply/apply")
    ServerResponse<Integer> saveChangeApply(@RequestBody StockChangeApplyEntity stockChangeApplyEntity);

    /**
     * 保存库存变更日志
     *
     * @param stockChangeLogEntity 库存变更日志
     * @return
     */
    @PostMapping("/stock-change-apply/apply-log")
    ServerResponse saveChangeLog(@RequestBody StockChangeLogEntity stockChangeLogEntity);

    /**
     * 更新拉平字段
     *
     * @param storageId 仓库id
     * @param skuCode   sku编码
     * @return
     */
    @PostMapping("/stock-change-apply/sku/refresh-unbalanced-flag")
    ServerResponse refreshSkuUnbalancedFlag(@RequestParam("storageId") Integer storageId, @RequestParam("skuCode") Integer skuCode);

    /**
     * 去库存检验
     *
     * @param checkSubSkuCountMapRequest 去库存检验请求
     * @return
     */
    @PostMapping("/stock-change-apply/check-sku")
    ServerResponse checkSubSkuCountMap(@RequestBody CheckSubSkuCountMapRequest checkSubSkuCountMapRequest);

    /**
     * 保存库存变更详细
     *
     * @param detailList 库存变更详细列表
     * @return
     */
    @PostMapping("/stock-change-apply/apply-detail")
    ServerResponse saveChangeDetailAll(@RequestBody List<StockChangeDetailEntity> detailList);

    /**
     * 获取本月未用完的sku
     *
     * @return 返回结果
     */
    @GetMapping("/stock-change-apply/not-empty-sku")
    ServerResponse<List<StockChangeSkuNotEmptyDTO>> getThisMonthSkuNotEmpty();

    /**
     * 批量获取最后一条增加库存的记录
     *
     * @param request 请求参数
     * @return 最后一条增加的记录
     */
    @PostMapping("/stock-change-apply/last-apply-list")
    ServerResponse<List<StockChangeApplyEntity>> batchGetLastRecords(@RequestBody List<StockChangeLastApplyRequest> request);

    /**
     * 提醒执行
     *
     * @param applyId 申请id
     * @return 响应
     */
    @PutMapping("/stock-change-apply/apply-change/{applyId}")
    ServerResponse<StockChangeApplyEntity> noticeExecute(@PathVariable("applyId") Integer applyId);

    /**
     *提醒 锁库
     *
     * @param applyId
     * @return
     */
    @PutMapping("/stock-lock-apply/stock-lock-data-save/{applyId}")
    ServerResponse<StockLockApplyEntity> noticeLockExecute(@PathVariable("applyId") Integer applyId);
}

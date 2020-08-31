package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.cloud.kanai.web.page.PageImpl;
import com.ruigu.rbox.workflow.model.dto.RsGroupGmvDataDTO;
import com.ruigu.rbox.workflow.model.dto.SpecialAfterSaleGroupQuotaDTO;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleQuotaTransactionEntity;
import com.ruigu.rbox.workflow.model.request.PageableRequest;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleQuotaRuleEnableRequest;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleQuotaRuleHistoryRequest;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleQuotaRuleRequest;
import com.ruigu.rbox.workflow.model.vo.SpecialAfterSaleQuotaRuleVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2020/08/12 19:08
 */
public interface SpecialAfterSaleQuotaService {

    /**
     * init 初始化
     */
    void initQuota(List<RsGroupGmvDataDTO> groupGmvDataDTO);

    /**
     * 撤销流水
     *
     * @param applyId 申请id
     */
    void undoTransaction(Long applyId);

    /**
     * 查询某人额度
     *
     * @param userIds 用户
     * @param type    类型
     * @return 额度
     */
    Map<Integer, List<SpecialAfterSaleGroupQuotaDTO>> queryQuotaByUserId(List<Integer> userIds, Integer type);

    /**
     * 扣减流水
     *
     * @param applyId     申请id
     * @param userId      用户id
     * @param quotaId     使用的额度id
     * @param applyAmount 申请金额
     * @return 如果额度充足则返回流水记录，如果不足则返回null
     */
    SpecialAfterSaleQuotaTransactionEntity deductionTransaction(Long applyId, Integer userId, Integer quotaId, BigDecimal applyAmount);

    /**
     * 标记成功
     *
     * @param applyId 申请id
     * @param status  状态
     */
    void markSuccessOrFail(Long applyId, Integer status);

    /**
     * 查询所有审批记录
     *
     * @param req 请求参数
     * @return 审批记录列表
     */
    PageImpl<SpecialAfterSaleQuotaRuleVO> queryAllQuotaRule(PageableRequest req);


    /**
     * 查询审批额度历史
     *
     * @param req 请求参数
     * @return 审批历史列表
     */
    PageImpl<SpecialAfterSaleQuotaRuleVO> queryQuotaRuleHistory(SpecialAfterSaleQuotaRuleHistoryRequest req);


    /**
     * 添加审批额度配置
     *
     * @param req 请求参数
     */
    void addQuotaRule(SpecialAfterSaleQuotaRuleRequest req);


    /**
     * 启用禁用审批额度配置
     *
     * @param req 请求参数
     */
    void enableQuotaRule(SpecialAfterSaleQuotaRuleEnableRequest req);

}

package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.dto.SpecialAfterSaleApprovalRulesDTO;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleApplyConfigRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/11 17:02
 */
public interface SpecialAfterSaleApplyConfigService {

    /**
     * 新建/更新审批配置
     *
     * @param req 请求参数
     */
    void saveOrUpdateApproverConfig(SpecialAfterSaleApplyConfigRequest req);

    /**
     * 查询审批配置列表状态启用或禁用
     * @param status
     * @param id
     */
    void enableOrDisable(Integer status, Integer id);

    /**
     * 查询审批配置列表
     * @param status
     * @param pageable
     * @return
     */
    List<SpecialAfterSaleApprovalRulesDTO> getSpecialAfterSaleApprovalRulesDetails(Integer status, Pageable pageable);
}


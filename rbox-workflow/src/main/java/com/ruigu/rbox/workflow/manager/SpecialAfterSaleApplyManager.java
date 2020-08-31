package com.ruigu.rbox.workflow.manager;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleApplyEntity;
import com.ruigu.rbox.workflow.model.request.lightning.AddSpecialAfterSaleApplyRequest;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/11 18:09
 */
public interface SpecialAfterSaleApplyManager {

    /**
     * 保存申请
     *
     * @param request  请求
     * @param configId 配置id
     * @param userId   用户id
     * @param userName 用户名
     * @return 保存后的申请数据
     */
    SpecialAfterSaleApplyEntity saveApply(AddSpecialAfterSaleApplyRequest request, Integer configId, Integer userId, String userName);

    /**
     * 保存申请
     *
     * @param apply 申请
     * @return 保存后的申请数据
     */
    SpecialAfterSaleApplyEntity saveApply(SpecialAfterSaleApplyEntity apply);

    /**
     * 保存当前审批人
     *
     * @param applyId        申请id
     * @param approverIdList 审批人id列表
     */
    void saveCurrentApprover(Long applyId, List<Integer> approverIdList);

    /**
     * 更新当前审批人
     *
     * @param applyId        申请id
     * @param approverIdList 审批人id列表
     */
    void updateCurrentApprover(Long applyId, List<Integer> approverIdList);

    /**
     * 查询当前审批人id
     *
     * @param applyId 申请id
     * @return 当前审批人
     */
    List<Integer> queryCurrentApprover(Long applyId);

    /**
     * 校验是否审批人
     *
     * @param applyId 申请id
     * @param userId  用户id
     * @return 是否
     */
    boolean checkIsApprover(Long applyId, Integer userId);
}

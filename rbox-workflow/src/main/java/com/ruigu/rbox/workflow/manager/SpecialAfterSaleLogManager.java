package com.ruigu.rbox.workflow.manager;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleLogEntity;
import com.ruigu.rbox.workflow.model.vo.SpecialAfterSaleLogVO;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/11 17:46
 */
public interface SpecialAfterSaleLogManager {

    /**
     * 生成动作日志
     *
     * @param applyId      申请id
     * @param taskId       任务id
     * @param reviewNodeId 配置节点id
     * @param action       动作
     * @param remark       备注
     * @param isShow       是否显示
     * @param createdBy    创建人
     * @return 日志
     */
    SpecialAfterSaleLogEntity createActionLog(Long applyId, String taskId, Integer reviewNodeId, Integer action, String remark, Integer isShow, Integer createdBy);

    /**
     * 生成动作日志
     *
     * @param applyId      申请id
     * @param taskId       任务id
     * @param reviewNodeId 配置节点id
     * @param action       动作
     * @param remark       备注
     * @param isShow       是否显示
     * @param createdBy    创建人
     */
    void createActionLog(Long applyId, String taskId, Integer reviewNodeId, Integer action, String remark, Integer isShow, List<Integer> createdBy);

    /**
     * 隐藏或展示日志
     *
     * @param applyId   申请id
     * @param taskId    任务id
     * @param action    动作
     * @param isShow    是否显示
     * @param createdBy 创建人
     * @return 日志
     */
    SpecialAfterSaleLogEntity hideOrShowLog(Long applyId, String taskId, Integer action, Integer isShow, Integer createdBy);

    /**
     * 隐藏或展示日志
     *
     * @param applyId 申请id
     * @param taskId  任务id
     * @param action  动作
     * @param isShow  是否显示
     * @return 日志
     */
    List<SpecialAfterSaleLogEntity> hideOrShowLog(Long applyId, String taskId, Integer action, Integer isShow);

    /**
     * 创建转审日志
     *
     * @param applyId            申请id
     * @param taskId             任务id
     * @param operatorId         操作人
     * @param transferUserIdList 转审人员
     */
    void createTransferLog(Long applyId, String taskId, List<Integer> transferUserIdList, Integer operatorId);

    /**
     * 查询日志记录
     *
     * @param applyId 申请id
     * @return 日志
     */
    List<SpecialAfterSaleLogVO> queryListLog(Long applyId);

    /**
     * 查询某申请某动作日志
     *
     * @param applyId 申请id
     * @param action  动作
     * @return 日志
     */
    List<SpecialAfterSaleLogEntity> queryListActionLog(Long applyId, Integer action);
}

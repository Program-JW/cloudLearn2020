package com.ruigu.rbox.workflow.manager;

import com.ruigu.rbox.workflow.model.entity.LightningIssueLogEntity;
import com.ruigu.rbox.workflow.model.request.lightning.LightningOverTimeReq;

import java.util.List;

/**
 * @author caojinghong
 * @date 2019/12/31 16:13
 */
public interface LightningIssueLogManager {
    /**
     * 保存问题操作日志记录/保存超时问题日志
     * @param issueId 问题id
     * @param action 日志操作类型
     * @param createdBy 该条日志记录的创建人，即动作的行为人
     * @return 该条日志记录的id
     */
    Integer saveIssueLogAction(Integer issueId, Integer action, Integer createdBy);

    /**
     * 保存问题操作日志记录（主要带备注的）
     * @param issueId 问题id
     * @param action 日志操作类型
     * @param createdBy 该条日志记录的创建人，即动作的行为人
     * @param remarks 备注
     * @return 该条日志记录的id
     */
    Integer saveIssueLogAction(Integer issueId, Integer action, Integer createdBy, String remarks);

    /**
     * 批量保存申请人发起的申请日志记录
     * @param issueId 问题id
     * @param action 日志操作类型
     * @param createdBy 该条日志记录的创建人，即动作的行为人
     */
    void saveIssueLogAction(List<Integer> issueId, Integer action, Integer createdBy);

    /**
     * 批量保存超时问题日志
     * @param req 超时问题入参列表
     */
    void saveIssueLogOverTime(List<LightningOverTimeReq> req);

    /**
     * 封装通用的保存日志的实体的方法
     * @param issueId 问题id
     * @param action 日志操作类型
     * @param createdBy 该条日志记录的创建人，即动作的行为人
     * @return 该条日志记录的实体
     */
    LightningIssueLogEntity saveIssueLog(Integer issueId, Integer action, Integer createdBy);

    /**
     * 封装通用的保存日志的实体的方法
     * @param issueId 问题id
     * @param action 日志操作类型
     * @param createdBy 该条日志记录的创建人，即动作的行为人
     * @param remarks 备注
     * @return 该条日志记录的实体
     */
    LightningIssueLogEntity saveIssueLog(Integer issueId, Integer action, Integer createdBy, String remarks);
}

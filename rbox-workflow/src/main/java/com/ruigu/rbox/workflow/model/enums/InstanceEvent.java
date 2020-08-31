package com.ruigu.rbox.workflow.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liqingtian
 * @date 2019/10/14 14:56
 */
@Getter
@AllArgsConstructor
public enum InstanceEvent {

    /**
     * 流程启动事件
     */
    INSTANCE_START(1),

    /**
     * 任务创建
     */
    TASK_CREATE(2),

    /**
     * 任务提交
     */
    TASK_COMPLETE(3),

    /**
     * 任务转办
     */
    TASK_TRANSFER(4),

    /**
     * 任务删除
     */
    TASK_DELETE(5),

    /**
     * 流程结束事件
     */
    INSTANCE_END(6),

    /**
     * 任务超时
     */
    TIME_OUT(7),

    /**
     * 流程撤销
     */
    INSTANCE_REVOKE(8),

    /**
     * 系统任务
     */
    SERVER_TASK(9),

    /**
     * 任务开始
     */
    TASK_BEGIN(10),

    /**
     * 催办
     */
    URGE(11),

    /**
     * 超时未开始 (超时)
     */
    TIME_OUT_BEGIN(12),

    /**
     * 超时未开始催办
     */
    TIME_OUT_BEGIN_URGE(13),

    /**
     * 闪电链 - 未读提醒
     */
    REMIND_UNREAD(14);

    private Integer code;
}

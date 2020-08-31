package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;

import java.util.Collection;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/11/12 13:43
 */
@Data
public class MessageInfoVO {
    private String title;
    private String description;
    private String url;
    private String definitionId;
    private String instanceId;
    private String taskId;

    /**
     * 按钮配置（任务卡片必有）
     */
    private String buttonConfig;

    /**
     * 送达人
     */
    private Collection<Integer> targets;
    private Collection<Integer> leaders;

    /**
     * 通知事件类型
     */
    private Integer noticeEventType;

    /**
     * 扩展属性
     */
    private Map<String, Object> extend;
}

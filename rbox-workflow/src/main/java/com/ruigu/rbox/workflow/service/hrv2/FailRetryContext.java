package com.ruigu.rbox.workflow.service.hrv2;

import lombok.Data;

/**
 * 失败重试上下文
 * @author alan.zhao
 */
@Data
public class FailRetryContext {
    private String name;
    private int count;
}

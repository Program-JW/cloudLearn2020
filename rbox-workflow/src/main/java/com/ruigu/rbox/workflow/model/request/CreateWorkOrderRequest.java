package com.ruigu.rbox.workflow.model.request;

import lombok.Data;

/**
 * 创建工单请求
 * @author alan.zhao
 */
@Data
public class CreateWorkOrderRequest {
    private String name;
    private Long ownerId;
}

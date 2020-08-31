package com.ruigu.rbox.workflow.model.request.lightning;

import lombok.Data;

/**
 * @author caojinghong
 * @date 2020/01/11 17:41
 */
@Data
public class LightningOverTimeReq {
    private Integer issueId;
    /**
     * 7-超时4小时 8-超时24小时 9-超时48小时
     */
    private Integer action;
    /**
     * 超时的受理人
     */
    private Integer createdBy;
}

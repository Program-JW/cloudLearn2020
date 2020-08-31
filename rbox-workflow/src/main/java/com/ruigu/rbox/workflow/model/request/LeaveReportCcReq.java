package com.ruigu.rbox.workflow.model.request;

import lombok.Data;

import java.util.List;

/**
 * @Author wuyimin
 * @Date 2020/5/24 1:26
 */
@Data
public class LeaveReportCcReq {

    /**
     * 申请记录ID
     */
    private Integer applyId;

    /**
     * 抄送人ID
     */
    private List<Integer> userIds;

}

package com.ruigu.rbox.workflow.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liqingtian
 * @date 2020/02/17 15:41
 */
public interface LightningIssueCountDTO {
    /**
     * 用户id
     *
     * @return 用户id
     */
    Integer getUserId();

    /**
     * 问题数
     *
     * @return 问题数
     */
    Long getCount();
}

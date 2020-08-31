package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author liqingtian
 * @date 2020/02/17 12:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GroupAndUserAndCountVO extends GroupAndUserVO {
    private Long issueCount;
}

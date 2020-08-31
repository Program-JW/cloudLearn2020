package com.ruigu.rbox.workflow.model.dto;

import lombok.Data;

/**
 * @author caojinghong
 * @date 2020/01/10 14:53
 */
@Data
public class DepartmentIssueDTO {
    private String firstLevelDepartmentName;
    private String secondLevelDepartmentName;
    private Integer secondLevelDepartmentId;
    private Integer secondLevelIssueCount;
}

package com.ruigu.rbox.workflow.model.dto;

import lombok.Data;

/**
 * 分人
 * @author caojinghong
 * @date 2020/01/09 15:30
 */
@Data
public class LightningIssueReportDTO {

    private String firstLevelDepartmentName;
    private String secondLevelDepartmentName;
    private String personName;
    private Integer userId;
    private Integer applyIssueCount;
    private Integer revockIssueCount;
    private Integer acceptIssueCount;
    private Integer consideredBestCount;
    private Integer finishAndConfirmCount;
    private Integer transferIssueCount;
    private Integer firstLevelPromotedCount;
    private Integer secondLevelPromotedCount;
    private Integer thirdLevelPromotedCount;
}

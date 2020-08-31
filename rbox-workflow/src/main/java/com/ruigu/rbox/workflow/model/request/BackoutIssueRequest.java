package com.ruigu.rbox.workflow.model.request;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
/**
 * @author xuhaoxaing
 */
@Data
public class BackoutIssueRequest {

    private List<String> businessKeys;

    private String instanceId;

    @NotNull(message = "操作人ID不能未空")
    private Integer operationUserId;

    private  String signalName;
}
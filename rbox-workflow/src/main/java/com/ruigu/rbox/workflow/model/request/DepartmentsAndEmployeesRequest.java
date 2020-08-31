package com.ruigu.rbox.workflow.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/02/17 11:43
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentsAndEmployeesRequest {
    private Integer dutyId;
    private Integer groupId;
    private Boolean isFilterTheGeneralManager;
    private List<String> positionList;
    private String roleCode;
    private String searchKey;
}

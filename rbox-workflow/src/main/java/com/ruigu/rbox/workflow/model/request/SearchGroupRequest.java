package com.ruigu.rbox.workflow.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/13 20:31
 */
@Data
@Builder
public class SearchGroupRequest {

    @JsonIgnore
    private List<Integer> groupIds;

    private String groupIdList;
    private String nameSearch;
    private Integer nameFuzzy;
    private String descriptionSearch;
    private Integer descriptionFuzzy;
    private Integer parentId;
    private Integer groupStatus;
    private Integer groupDeleted;
}


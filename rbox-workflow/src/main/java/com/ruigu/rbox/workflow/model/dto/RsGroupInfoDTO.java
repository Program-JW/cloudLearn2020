package com.ruigu.rbox.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

/**
 * @author liqingtian
 * @date 2020/08/13 10:09
 */
@Data
public class RsGroupInfoDTO {
    @JsonSetter("group_id")
    private Integer groupId;
    @JsonSetter("group_name")
    private String groupName;
    private Integer type;
}

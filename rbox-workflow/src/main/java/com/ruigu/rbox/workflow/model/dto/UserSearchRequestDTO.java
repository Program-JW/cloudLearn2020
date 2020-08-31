package com.ruigu.rbox.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/05 22:10
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSearchRequestDTO {

    private List<Integer> userIdList;
    private Integer userStatus;
    private Integer userDeleted;
    private Integer groupStatus;
    private Integer groupDeleted;

}

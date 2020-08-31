package com.ruigu.rbox.workflow.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruigu.rbox.workflow.model.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author cg1
 * @date 2019-08-29 15:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserReq extends User {
    private Integer id;
}

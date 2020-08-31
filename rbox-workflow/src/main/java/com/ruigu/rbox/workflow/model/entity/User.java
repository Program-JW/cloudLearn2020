package com.ruigu.rbox.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用于区分req与Vo
 *
 * @author cg1
 * @date 2019-09-21 16:05
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {
    private String phone;
    private String email;
    private String workWx;
    private String clientId;
}

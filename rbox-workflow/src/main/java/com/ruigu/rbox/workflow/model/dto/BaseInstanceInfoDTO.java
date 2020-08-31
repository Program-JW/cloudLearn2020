package com.ruigu.rbox.workflow.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author liqingtian
 * @date 2019/12/24 17:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseInstanceInfoDTO {

    private String id;
    private String name;
    private Date createdOn;
}

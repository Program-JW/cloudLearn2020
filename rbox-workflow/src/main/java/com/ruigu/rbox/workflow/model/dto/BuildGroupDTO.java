package com.ruigu.rbox.workflow.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/01/08 17:56
 */
@Data
public class BuildGroupDTO {

    /**
     * 问题id
     */
    private Integer issueId;

    /**
     *
     */
    private Integer masterId;

    /**
     * 成员id列表
     */
    private List<Integer> memberIds;
}

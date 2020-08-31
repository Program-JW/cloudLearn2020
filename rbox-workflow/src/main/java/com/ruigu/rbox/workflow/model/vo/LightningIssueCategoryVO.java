package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 闪电链问题分类实体
 *
 * @author alan.zhao
 * @date 2019-12-26 16:29:31
 */
@Data
@NoArgsConstructor
public class LightningIssueCategoryVO implements Serializable {

    /**
     * ID
     */
    private Integer id;

    /**
     * 问题分类名称
     */
    private String name;

    /**
     * 处理人ID
     */
    private Integer userId;

    /**
     * 处理人名称
     */
    private String userName;

    /**
     * 处理人名称
     */
    private String avatar;

    /**
     * 类型 （1-按天值班 2-轮询）
     */
    private Integer type;
}

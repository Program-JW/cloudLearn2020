package com.ruigu.rbox.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组管理
 *
 * @author lijiajia
 * @date 2019/6/27 18:02
 */
@Data
public class PassportGroupInfoDTO {

    /**
     * 部门id导入时需要手动设置
     */
    private Integer id;
    /**
     * 部门名称
     */
    private String name;

    private Integer status;

    private Integer type;

    /**
     * 1-来自企业微信，0-默认
     */
    private Integer source;

    /**
     * 部门领导userId
     */
    private String leader;

    private String description;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 创建时间
     */
    private String createdOn;
    /**
     * 修改时间
     */
    private String lastUpdatedOn;

    private Integer createdBy;
    private Integer lastUpdatedBy;

    private Integer parentId;

    /**
     * 是否删除
     *
     * @see com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum
     */
    private Integer deleted;

}

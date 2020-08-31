package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author liqingtian
 * @date 2019-10-14 14:47:37
 */
@Data
@Entity
@Table(name = "definition_notice_config")
public class DefinitionNoticeConfigEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;

    /**
     * 节点ID
     */
    @Column(name = "definition_id", columnDefinition = "varchar")
    private String definitionId;

    /**
     * 是否启用 1 启用 0 关闭
     */
    @Column(name = "enabled", columnDefinition = "int")
    private Integer enabled;

    /**
     * 适用事件类型
     */
    @Column(name = "event", columnDefinition = "int")
    private Integer event;

    /**
     * 通知模板的ID
     */
    @Column(name = "template_id", columnDefinition = "bigint")
    private Long templateId;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", columnDefinition = "timestamp")
    private Date createdOn;

    /**
     * 创建者
     */
    @Column(name = "created_by", columnDefinition = "bigint")
    private Long createdBy;

    /**
     * 最后修改日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated_on", columnDefinition = "timestamp")
    private Date lastUpdatedOn;

    /**
     * 最后修改者
     */
    @Column(name = "last_updated_by", columnDefinition = "bigint")
    private Long lastUpdatedBy;

    /**
     * 状态：-1废弃，0 禁用,1 启用
     */
    @Column(name = "status", columnDefinition = "int")
    private Integer status;

}

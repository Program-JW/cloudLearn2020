package com.ruigu.rbox.workflow.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liqingtian
 * @date 2019-10-14 14:47:38
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notice_template")
public class NoticeTemplateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    private Long id;

    /**
     * 适用事件类型 1 流程启动 2 任务创建 3 任务完成 4 任务转办 5 任务删除 6 流程完成
     */
    @Column(name = "event", columnDefinition = "int")
    private Integer event;

    /**
     * 渠道类型 1 企业维系 2 邮件
     */
    @Column(name = "channel", columnDefinition = "int")
    private Integer channel;

    /**
     * 子类型
     */
    @Column(name = "type", columnDefinition = "int")
    private Integer type;

    /**
     * 标题模板
     */
    @Column(name = "title", columnDefinition = "text")
    private String title;

    /**
     * 内容模板
     */
    @Column(name = "content", columnDefinition = "text")
    private String content;

    /**
     * 内容模板
     */
    @Column(name = "detail_url", columnDefinition = "text")
    private String detailUrl;

    /**
     * 按钮配置
     */
    @Column(name = "button_config", columnDefinition = "text")
    private String buttonConfig;

    /**
     * 创建时间
     */
    @Column(name = "created_on", columnDefinition = "timestamp")
    private LocalDateTime createdOn;

    /**
     * 创建者
     */
    @Column(name = "created_by", columnDefinition = "bigint")
    private Long createdBy;

    /**
     * 最后修改日期
     */
    @Column(name = "last_updated_on", columnDefinition = "timestamp")
    private LocalDateTime lastUpdatedOn;

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

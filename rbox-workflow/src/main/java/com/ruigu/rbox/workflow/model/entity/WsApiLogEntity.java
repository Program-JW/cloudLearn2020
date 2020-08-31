package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liqingtian
 * @date 2020-01-08 16:47:44
 */
@Data
@Entity
@Table(name = "ws_api_log")
public class WsApiLogEntity implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int")
    private Integer id;

    /**
     * 请求唯一标识
     */
    @Column(name = "message_id", columnDefinition = "varchar")
    private String messageId;

    /**
     * 问题id
     */
    @Column(name = "issue_id", columnDefinition = "int")
    private Integer issueId;

    /**
     * 请求参数
     */
    @Column(name = "send_message", columnDefinition = "text")
    private String sendMessage;

    /**
     * 响应结果
     */
    @Column(name = "return_message", columnDefinition = "text")
    private String returnMessage;

    /**
     * 创建时间
     */
    @Column(name = "created_on", columnDefinition = "timestamp")
    private LocalDateTime createdOn;

    /**
     * 创建人ID
     */
    @Column(name = "created_by", columnDefinition = "int")
    private Integer createdBy;

}

package com.ruigu.rbox.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: heyi
 * @date: 2019/11/20 11:35
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "api_log")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiLogEntity {

    /**
     * 主键
     */
    @Id
    @Column(name = "id", columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 操作对象表id
     */
    @Column(name = "object_id", columnDefinition = "int")
    private Integer objectId;

    /**
     * 请求参数（json）
     */
    @Column(name = "request_data", columnDefinition = "text")
    private String requestData;

    /**
     * 返回参数（json）
     */
    @Column(name = "response_data", columnDefinition = "text")
    private String responseData;

    /**
     * 调用记录日志的方法名
     */
    @Column(name = "method_name", columnDefinition = "varchar")
    private String methodName;

    /**
     * 请求的url
     */
    @Column(name = "request_url", columnDefinition = "varchar")
    private String requestUrl;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_on", columnDefinition = "timestamp")
    private Date createOn;

    /**
     * 日志状态 0无效 1有效
     */
    @Column(name = "status", columnDefinition = "int")
    private Integer status;
}
package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author liqingtian
 * @date 2020-01-15 19:49:21
 */
@Data
@Entity
@Table(name = "lightning_issue_group")
public class LightningIssueGroupEntity implements Serializable {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int")
    private Integer id;

    /**
     * 问题id
     */
    @Column(name = "issue_id", columnDefinition = "int")
    private Integer issueId;

    /**
     * 群组id
     */
    @Column(name = "group_id", columnDefinition = "varchar")
    private String groupId;

    /**
     * 群组名
     */
    @Column(name = "group_name", columnDefinition = "varchar")
    private String groupName;

    /**
     * 创建时间
     */
    @Column(name = "created_on", columnDefinition = "timestamp")
    private Date createdOn;
}

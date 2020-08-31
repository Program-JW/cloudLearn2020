package com.ruigu.rbox.workflow.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author caojinghong
 * @date 2020/01/08 10:26
 */
@Data
@Entity
@Table(name = "duty_roster")
public class DutyRosterEntity {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int")
    @ApiModelProperty(value = "主键id",name = "id")
    private Integer id;

    @Column(name = "person_id",columnDefinition = "int")
    @ApiModelProperty(value = "值班人id",name = "personId")
    private Integer personId;

    @Column(name = "person_name",columnDefinition = "varchar")
    @ApiModelProperty(value = "值班人姓名",name = "personName")
    private String personName;

    @Column(name = "duty_date",columnDefinition = "timestamp")
    @ApiModelProperty(value = "值班时间",name = "dutyDate")
    private Date dutyDate;
}

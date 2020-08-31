package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
/**
 * @author liqingtian
 * @date 2019/08/22 15:51
 */
@Data
@Entity
@Table(name = "work_mode")
public class WorkModeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private Integer day;
    private Time startTime;
    private Time endTime;
    private String code;
}

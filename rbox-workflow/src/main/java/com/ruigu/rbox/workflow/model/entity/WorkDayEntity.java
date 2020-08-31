package com.ruigu.rbox.workflow.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

/**
 * @author liqingtian
 * @date 2019/08/22 15:51
 */
@Entity
@Table(name = "work_day")
@Data
public class WorkDayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private Integer year;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfYear;
    private Integer dayOfYear;
    private Integer isWorkingDay;
    private Time startTime;
    private Time endTime;
    private String reason;
}

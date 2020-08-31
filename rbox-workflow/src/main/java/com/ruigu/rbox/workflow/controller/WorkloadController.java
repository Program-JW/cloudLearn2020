package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.enums.WorkloadState;
import com.ruigu.rbox.workflow.strategy.context.WorkloadStatisticsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @author liqingtian
 * @date 2019/10/12 15:54
 */
@RestController
@RequestMapping("/workload")
public class WorkloadController {

    @Autowired
    WorkloadStatisticsContext workloadStatisticsContext;

    @GetMapping("/month")
    public ServerResponse myWorkload(Integer userId, Integer year, Integer month) {
        return ServerResponse.ok(workloadStatisticsContext.statistics(WorkloadState.MONTH.getValue(), year, month, userId));
    }

    @GetMapping("/year")
    public ServerResponse myWorkload(Integer userId, Integer year) {
        return ServerResponse.ok(workloadStatisticsContext.statistics(WorkloadState.YEAR.getValue(), year, null, userId));
    }

    private Integer getYear(Integer year) {
        if (year == null || year < 1) {
            return LocalDate.now().getYear();
        }
        return year;
    }

    private Integer getMonth(Integer month) {
        int minMouthCount = 1, maxMouthCount = 12;
        if (month == null || month < minMouthCount || month > maxMouthCount) {
            return LocalDate.now().getMonthValue();
        }
        return month;
    }
}

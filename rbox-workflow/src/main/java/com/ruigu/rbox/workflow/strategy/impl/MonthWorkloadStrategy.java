package com.ruigu.rbox.workflow.strategy.impl;

import com.google.common.primitives.Ints;
import com.ruigu.rbox.workflow.model.enums.TaskState;
import com.ruigu.rbox.workflow.repository.TaskNativeRepository;
import com.ruigu.rbox.workflow.strategy.WorkloadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/10/12 17:08
 */
@Component("MONTH")
public class MonthWorkloadStrategy implements WorkloadStrategy {

    /**
     * 按月统计工作量
     */
    @Autowired
    private TaskNativeRepository taskNativeRepository;

    @Override
    public Map<String, Object> statistics(Integer userId, Integer year, Integer month) {
        // 数据库中统计数据
        Map<String, Object> data = taskNativeRepository.selectMonthWorkloadData(year, month, userId);
        List<Map<String, Object>> create = (List<Map<String, Object>>) data.get("create");
        List<Map<String, Object>> work = (List<Map<String, Object>>) data.get("work");
        List<Map<String, Object>> timeout = (List<Map<String, Object>>) data.get("timeout");

        // 转换格式，适合前端使用
        Map<String, Object> statisticsData = new HashMap<>(8);
        Map<Integer, List<Integer>> totalWeekData = new HashMap<>(16);
        // 获取查询月信息
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        LocalDateTime monthFirstDate = LocalDateTime.of(year, month, 1, 0, 0, 0);
        int weekOfYear = monthFirstDate.get(weekFields.weekOfYear());
        int monthLength = monthFirstDate.toLocalDate().lengthOfMonth();
        LocalDateTime monthLastDate = LocalDateTime.of(year, month, monthLength, 0, 0, 0);
        int weekCount = monthLastDate.get(weekFields.weekOfMonth());
        // 初始化周统计数据
        for (int i = 0; i < weekCount; i++) {
            int[] weekData = new int[TaskState.values().length + 2];
            totalWeekData.put(weekOfYear++, Ints.asList(weekData));
            // 释放空间
            weekData = null;
        }
        // 工作总数统计
        int workTotal = 0;
        int createTotal = 0;
        int timeoutTotal = 0;
        // 遍历源数据 组装数据
        for (Map<String, Object> week : work) {
            Integer workWeek = Integer.parseInt(String.valueOf(week.get("workWeek")));
            Integer status = Integer.parseInt(String.valueOf(week.get("status")));
            status = status == null ? 0 : status;
            int count = new BigInteger(String.valueOf(week.get("count"))).intValue();
            if (status == TaskState.INVALID.getState()) {
                totalWeekData.get(workWeek).set(TaskState.values().length + 1, count);
            }
            totalWeekData.get(workWeek).set(status, count);
            if (status != TaskState.UNTREATED.getState()) {
                workTotal += count;
            }
        }
        for (Map<String, Object> week : create) {
            int count = new BigInteger(String.valueOf(week.get("count"))).intValue();
            Integer workWeek = Integer.parseInt(String.valueOf(week.get("workWeek")));
            totalWeekData.get(workWeek).set(5, count);
            createTotal = count;
        }
        for (Map<String, Object> week : timeout) {
            int count = new BigInteger(String.valueOf(week.get("count"))).intValue();
            Integer workWeek = Integer.parseInt(String.valueOf(week.get("workWeek")));
            totalWeekData.get(workWeek).set(6, count);
            timeoutTotal += count;
        }
        statisticsData.put("workTotal", workTotal);
        statisticsData.put("timeoutTotal", timeoutTotal);
        statisticsData.put("createTotal", createTotal);
        statisticsData.put("weekData", totalWeekData);

        return statisticsData;
    }
}

package com.ruigu.rbox.workflow.strategy.impl;

import com.google.common.primitives.Ints;
import com.ruigu.rbox.workflow.model.enums.TaskState;
import com.ruigu.rbox.workflow.repository.TaskNativeRepository;
import com.ruigu.rbox.workflow.strategy.WorkloadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/10/12 17:08
 */
@Component("YEAR")
public class YearWorkloadStrategy implements WorkloadStrategy {

    /**
     * 按年统计工作量
     */
    @Autowired
    private TaskNativeRepository taskNativeRepository;

    @Override
    public Map<String, Object> statistics(Integer userId, Integer year, Integer month) {

        // 数据库获取原始数据
        Map<String, Object> data = taskNativeRepository.selectYearWorkloadData(year, userId);
        List<Map<String, Object>> create = (List<Map<String, Object>>) data.get("create");
        List<Map<String, Object>> work = (List<Map<String, Object>>) data.get("work");
        List<Map<String, Object>> timeout = (List<Map<String, Object>>) data.get("timeout");

        Map<String, Object> statisticsData = new HashMap<>(8);
        Map<Integer, List<Integer>> totalMouthData = new HashMap<>(16);
        int monthCount = 12;
        for (int i = 0; i < monthCount; i++) {
            int[] monthData = new int[TaskState.values().length + 2];
            totalMouthData.put(i + 1, Ints.asList(monthData));
            monthData = null;
        }

        // 工作总数统计
        int workTotal = 0;
        int createTotal = 0;
        int timeoutTotal = 0;
        // 遍历源数据 组装数据
        for (Map<String, Object> monthData : work) {
            Integer workMonth = Integer.parseInt(String.valueOf(monthData.get("workMonth")));
            Integer status = Integer.parseInt(String.valueOf(monthData.get("status")));
            status = status == null ? 0 : status;
            int count = new BigInteger(String.valueOf(monthData.get("count"))).intValue();
            if (status == TaskState.INVALID.getState()) {
                totalMouthData.get(workMonth).set(TaskState.values().length + 1, count);
            }
            totalMouthData.get(workMonth).set(status, count);
            if (status != TaskState.UNTREATED.getState()) {
                workTotal += count;
            }
        }
        for (Map<String, Object> monthData : create) {
            int count = new BigInteger(String.valueOf(monthData.get("count"))).intValue();
            Integer workMonth = Integer.parseInt(String.valueOf(monthData.get("workMonth")));
            totalMouthData.get(workMonth).set(5, count);
            createTotal = count;
        }
        for (Map<String, Object> monthData : timeout) {
            int count = new BigInteger(String.valueOf(monthData.get("count"))).intValue();
            Integer workMonth = Integer.parseInt(String.valueOf(monthData.get("workMonth")));
            totalMouthData.get(workMonth).set(6, count);
            timeoutTotal += count;
        }
        statisticsData.put("workTotal", workTotal);
        statisticsData.put("timeoutTotal", timeoutTotal);
        statisticsData.put("createTotal", createTotal);
        statisticsData.put("monthData", totalMouthData);

        return statisticsData;
    }
}

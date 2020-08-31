package com.ruigu.rbox.workflow.strategy.context;

import com.ruigu.rbox.workflow.strategy.WorkloadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/10/12 17:01
 */
@Component
public class WorkloadStatisticsContext {

    @Autowired
    private Map<String, WorkloadStrategy> map;

    public Map<String, Object> statistics(String type, Integer year, Integer month, Integer userId) {
        return map.get(type).statistics(userId, year, month);
    }
}

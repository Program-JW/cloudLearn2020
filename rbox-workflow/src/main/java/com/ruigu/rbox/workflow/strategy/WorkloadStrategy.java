package com.ruigu.rbox.workflow.strategy;

import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/10/12 17:05
 */
public interface WorkloadStrategy {

    /**
     * 统计工作量
     *
     * @param userId 用户id
     * @param year   年
     * @param month  月
     * @return 工作量统计数据
     */
    Map<String, Object> statistics(Integer userId, Integer year, Integer month);
}

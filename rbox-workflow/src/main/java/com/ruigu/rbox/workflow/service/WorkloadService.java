package com.ruigu.rbox.workflow.service;

import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/10/12 15:56
 */
public interface WorkloadService {

    /**
     * 查询某个人的工作量
     *
     * @param userId      用户id
     * @param granularity 粒度
     * @return 工作量统计
     */
    Map<String, Object> getWorkloadByUserId(Integer userId, Integer granularity);
}

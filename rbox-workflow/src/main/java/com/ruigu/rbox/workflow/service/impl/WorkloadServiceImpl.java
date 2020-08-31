package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.service.WorkloadService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/10/12 16:02
 */
@Service
public class WorkloadServiceImpl implements WorkloadService {

    @Override
    public Map<String, Object> getWorkloadByUserId(Integer userId, Integer granularity) {
        return null;
    }
}

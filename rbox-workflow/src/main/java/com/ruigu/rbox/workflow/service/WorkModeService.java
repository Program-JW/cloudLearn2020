package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.entity.WorkModeEntity;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/09/17 20:53
 */
public interface WorkModeService {

    /**
     * 获取各部门工作模式
     *
     * @return 各部门工作模式
     */
    List<WorkModeEntity> getWorkModeToday();
}

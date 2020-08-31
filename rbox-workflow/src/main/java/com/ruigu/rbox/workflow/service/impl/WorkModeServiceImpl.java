package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.model.entity.WorkModeEntity;
import com.ruigu.rbox.workflow.repository.WorkModeRepository;
import com.ruigu.rbox.workflow.service.WorkModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author liqingtian
 * @date 2019/09/17 20:55
 */
@Service
public class WorkModeServiceImpl implements WorkModeService {

    @Autowired
    private WorkModeRepository workModeRepository;

    @Override
    public List<WorkModeEntity> getWorkModeToday() {
        return workModeRepository.selectWorkModeToday(LocalDate.now().getDayOfWeek().getValue());
    }
}

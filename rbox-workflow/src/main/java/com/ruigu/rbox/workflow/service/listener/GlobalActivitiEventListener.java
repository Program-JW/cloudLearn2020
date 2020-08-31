package com.ruigu.rbox.workflow.service.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author alan.zhao
 */
@Service
public class GlobalActivitiEventListener implements ActivitiEventListener {
    private static final Logger logger = LoggerFactory.getLogger(GlobalActivitiEventListener.class);

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public void onEvent(ActivitiEvent event) {
        switch (event.getType()) {
            case TASK_CREATED:
                break;
            default:
                break;
        }
    }

}
package com.ruigu.rbox.workflow.manager;

import com.ruigu.rbox.workflow.model.entity.LightningTimeoutLogEntity;

/**
 * @author liqingtian
 * @date 2020/03/03 16:07
 */
public interface LightningTimeoutLogManager {

    /**
     * 生成超时日志
     *
     * @param issueId   问题id`
     * @param createdBy 创建人
     * @param type      类型
     * @return 超时日志
     */
    LightningTimeoutLogEntity returnTimeoutLog(Integer issueId, Integer type, Integer createdBy);
}

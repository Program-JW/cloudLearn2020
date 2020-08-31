package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.entity.ReliableMqLogEntity;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/07/21 10:26
 */
public interface GuaranteeSuccessMqMessageService {

    /**
     * 保存记录
     *
     * @param record 记录
     */
    void saveRecord(ReliableMqLogEntity record);

    /**
     * 发送
     *
     * @param record 记录
     */
    void send(ReliableMqLogEntity record);

    /**
     * 定时扫描重发
     */
    void scanRecordAndRetry();

    /**
     * 手动补偿 （可无视最大重试次数）
     */
    void failRecordManualRetry(List<Long> recordIds, Boolean maxLimit);
}

package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.entity.NoticeEntity;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/08/21 16:57
 */
public interface NoticeLogService {

    /**
     * 添加通知
     *
     * @param notice 通知实体类
     * @return long id
     */
    long insertNotice(NoticeEntity notice);

    /**
     * 通过微信任务id获取通知
     *
     * @param taskIdWeixin 微信卡片任务id
     * @return NoticeEntity
     */
    NoticeEntity getNoticeByTaskIdWeixin(String taskIdWeixin);

    /**
     * 通过任务id获取通知
     *
     * @param taskId 任务id
     * @return NoticeEntity
     */
    NoticeEntity getNoticeTaskId(String taskId);

    /**
     * 更新任务状态
     *
     * @param taskId 任务id
     * @return long
     */
    void updateNotice(String taskId);

    /**
     * 获取超时通知
     *
     * @param taskId 任务id
     * @return NoticeEntity
     */
    NoticeEntity getLastTimeoutNoticeByTaskId(String taskId);

    /**
     * 获取超时通知
     *
     * @param instanceId 流程id
     * @return NoticeEntity
     */
    List<NoticeEntity> getLastTimeoutNoticeByInstanceId(String instanceId);

    /**
     * 获取超时通知
     *
     * @param instanceIds 流程id列表
     * @return 超时通知
     */
    List<NoticeEntity> getTimeoutNoticeByInstanceIds(List<String> instanceIds);

    /**
     * 获取notice日志
     *
     * @param instanceId 实例id
     * @param type       类型
     * @return 通知日志
     */
    List<NoticeEntity> getListNoticeByInstanceIdAndType(String instanceId, Integer type);

    /**
     * 获取通知数据根据
     *
     * @param instanceIds 流程实例id列表
     * @param typeList    通知类型列表
     * @return 历史通知数据
     */
    List<NoticeEntity> getNoticesByInstanceIdInAndTypeIn(List<String> instanceIds, List<Integer> typeList);

    /**
     * 更新通知次数
     *
     * @param id 通知id
     * @return long 影响行数
     */
    long updateNoticeCount(Long id);

    /**
     * 查询未发送信息
     *
     * @param definitionKey 流程Key
     * @param type          通知类型
     * @return 为发送的通知
     */
    List<NoticeEntity> getUnsentNotice(String definitionKey, Integer type);


    /**
     * 批量更新
     *
     * @param noticeList 批量通知
     */
    void batchUpdate(List<NoticeEntity> noticeList);

    /**
     * 查找库存变更超时通知（领导通知）
     *
     * @param definitionCode 流程key
     * @return 超时通知
     */
    List<NoticeEntity> getStockChangeTimeoutLeaderNotice(String definitionCode);

    /**
     * 查询确认通知
     *
     * @param instanceId 实例id
     * @param taskId     任务id
     * @param type       通知类型
     * @param targets    通知人
     * @return 通知记录
     */
    NoticeEntity getNoticeByInstanceIdAndTaskIdAndType(String instanceId, String taskId, Integer type, String targets);
}

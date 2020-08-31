package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/08/21 17:00
 */
@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer> {

    /**
     * 通过任务卡片id获取获取通知
     *
     * @param taskIdWeixin 任务卡片id
     * @return NoticeEntity
     */
    NoticeEntity findTopByTaskIdWeixinOrderByCreatedOn(String taskIdWeixin);

    /**
     * 通过任务id获取获取通知
     *
     * @param taskId 任务id
     * @return NoticeEntity
     */
    NoticeEntity findTopByTaskIdOrderByCreatedOn(String taskId);

    /**
     * 更新通知状态
     *
     * @param taskId 任务id
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update NoticeEntity n set n.status = 1 where n.taskId = :taskId")
    void updateStatus(@Param("taskId") String taskId);

    /**
     * 通过任务id获取获取超时通知
     *
     * @param taskId 任务id
     * @param type   任务类型
     * @param status 任务状态
     * @return NoticeEntity
     */
    NoticeEntity findByTaskIdAndTypeAndStatusOrderByCreatedOnDesc(String taskId, Integer type, Byte status);

    /**
     * 通过流程id获取获取超时通知
     *
     * @param instanceId 任务id
     * @param type       任务类型
     * @param status     任务状态
     * @return NoticeEntity
     */
    List<NoticeEntity> findByInstanceIdAndTypeAndStatusOrderByCreatedOnDesc(String instanceId, Integer type, Byte status);

    /**
     * 获取通知信息，通过实例id和状态
     *
     * @param instanceIds 实例Id列表
     * @param typeList    类型列表
     * @return 通知信息列表
     */
    List<NoticeEntity> findByInstanceIdInAndTypeIn(List<String> instanceIds, List<Integer> typeList);

    /**
     * 获取通知信息，通过实例id和状态
     *
     * @param instanceId Id
     * @param type       类型
     * @return 通知信息列表
     */
    List<NoticeEntity> findAllByInstanceIdAndType(String instanceId, Integer type);

    /**
     * 批量获取通知通过instanceIds
     *
     * @param type        通知类型
     * @param status      状态
     * @param instanceIds 流程id列表
     * @return 通知列表
     */
    List<NoticeEntity> findAllByTypeAndStatusAndInstanceIdIn(Integer type, Byte status, List<String> instanceIds);

    /**
     * 更改次数
     *
     * @param id 通知id
     * @return int
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query("update NoticeEntity n set n.count = n.count+1 ,n.lastUpdatedOn=current_time where n.id = :id")
    int updateNoticeCount(@Param("id") Long id);

    /**
     * 获取未通知
     *
     * @param definitionIds 流程ids
     * @param type          通知类型
     * @param status        状态
     * @return 未发送通知
     */
    List<NoticeEntity> findAllByDefinitionIdInAndTypeAndStatus(List<String> definitionIds, Integer type, byte status);


    /**
     * 获取库存变更领导通知
     *
     * @param definitionCode code
     * @return 通知
     */
    @Query(" select n " +
            " from NoticeEntity n " +
            " where n.type = 7 and n.status = 1 and n.instanceId is null " +
            " and n.definitionId in " +
            " ( select id from WorkflowDefinitionEntity " +
            " where initialCode = :definitionCode and isReleased = 1 and status = 1 ) " +
            " order by n.createdOn desc ")
    List<NoticeEntity> findStockLeaderTimeoutNotice(@Param("definitionCode") String definitionCode);

    /**
     * 查询通知记录
     *
     * @param instanceId 实例id
     * @param taskId     任务id
     * @param type       通知类型
     * @param targets    通知人
     * @return 通知记录
     */
    NoticeEntity findByInstanceIdAndTaskIdAndTypeAndTargets(String instanceId, String taskId, Integer type, String targets);
}

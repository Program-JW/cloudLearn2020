package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.DefinitionNoticeConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author: heyi
 * @date: 2019/11/25 14:38
 */

public interface DefinitionNoticeConfigRepository extends JpaRepository<DefinitionNoticeConfigEntity, Integer> {

    /**
     * 获取通知配置信息
     *
     * @param id DefinitionId信息
     * @return 返回结果
     */
    List<DefinitionNoticeConfigEntity> findAllByDefinitionId(String id);

    /**
     * 更新通知定义信息的ID
     *
     * @param newDefinitionId 定义ID
     * @param lastUpdatedOn   最后修改时间
     * @param userId          最后修改人
     * @param ids             通知配置定义主键集合
     * @return 受影响记录数
     */
    @Modifying
    @Query("update DefinitionNoticeConfigEntity D set D.definitionId = ?1,D.lastUpdatedOn = ?2,D.lastUpdatedBy = ?3 where D.id in (?4)")
    int updateDefinitionId(String newDefinitionId, Date lastUpdatedOn, Long userId, Collection<Long> ids);

    /**
     * 删除流程通知配置
     *
     * @param definitionId 流程id
     * @param event        事件
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update DefinitionNoticeConfigEntity d set d.status = 0 ,d.enabled = 0 " +
            " where d.definitionId = :definitionId and d.event = :event")
    void removeDefinitionNoticeByIdAndEvent(@Param("definitionId") String definitionId, @Param("event") Integer event);
}

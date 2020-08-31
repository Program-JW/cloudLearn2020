package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.NodeNoticeConfigEntity;
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
 * @date: 2019/11/25 16:24
 */
public interface NodeNoticeConfigRepository extends JpaRepository<NodeNoticeConfigEntity, Integer> {

    /**
     * 获取节点配置信息
     *
     * @param id 节点ID
     * @return 节点配置信息
     */
    List<NodeNoticeConfigEntity> findAllByNodeId(String id);

    /**
     * 更新节点通知配置信息
     *
     * @param nodeId 节点ID
     * @param date   最后更新时间
     * @param userId 最后更新人
     * @param ids    节点通知配置信息主键集合
     * @return 修改记录数
     */
    @Modifying
    @Query("update NodeNoticeConfigEntity N set N.nodeId = ?1,N.lastUpdatedOn= ?2,N.lastUpdatedBy = ?3 where N.id in (?4)")
    int updateNodeId(String nodeId, Date date, Long userId, Collection<Long> ids);

    /**
     * 删除流程通知配置
     *
     * @param nodeId 节点id
     * @param event  事件
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update NodeNoticeConfigEntity n set n.status = 0 ,n.enabled = 0 " +
            " where n.nodeId = :nodeId and n.event = :event")
    void removeDefinitionNoticeByIdAndEvent(@Param("nodeId") String nodeId, @Param("event") Integer event);
}

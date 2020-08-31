package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.NoticeTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/10/14 15:45
 */
public interface NoticeTemplateRepository extends JpaRepository<NoticeTemplateEntity, Long> {

    /**
     * 查找node的通知模板，通过id和事件类型
     *
     * @param id    nodeID
     * @param event 事件
     * @return 返回通知模板
     */
    @Query("select t " +
            "from NoticeTemplateEntity t left join NodeNoticeConfigEntity c on t.id = c.templateId " +
            "where c.nodeId = :id and c.event=:event and c.enabled = 1 and c.status = 1 and t.status = 1")
    List<NoticeTemplateEntity> findNodeNoticeTemplateByIdAndEvent(@Param("id") String id, @Param("event") Integer event);

    /**
     * 查找node的通知模板，通过id和事件类型
     *
     * @param id    nodeID
     * @param event 事件
     * @return 返回通知模板
     */
    @Query("select t " +
            "from NoticeTemplateEntity t left join NodeNoticeConfigEntity c on t.id = c.templateId " +
            "where c.nodeId like :id and c.event=:event and c.enabled = 1 and c.status = 1 and t.status = 1")
    List<NoticeTemplateEntity> findNodeNoticeTemplateByIdLikeAndEvent(@Param("id") String id, @Param("event") Integer event);

    /**
     * 查找definition的通知模板，通过id和事件类型
     *
     * @param definitionId nodeID
     * @param event        事件
     * @return 返回通知模板
     */
    @Query("select t " +
            "from NoticeTemplateEntity t left join DefinitionNoticeConfigEntity c on t.id = c.templateId " +
            "where c.definitionId=:id and c.event=:event and c.enabled = 1 and c.status = 1 and t.status = 1")
    List<NoticeTemplateEntity> findDefinitionTemplateByIdAndEvent(@Param("id") String definitionId, @Param("event") Integer event);

    /**
     * 根据模板逻辑删除
     *
     * @param id 模板id
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update NoticeTemplateEntity t set t.status = 0 where t.id = :id")
    void removeTemplateById(@Param("id") Long id);

    /**
     * 根据模板逻辑删除
     *
     * @param ids 模板id列表
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update NoticeTemplateEntity t set t.status = 0 where t.id in (:ids)")
    void removeTemplateByIds(@Param("ids") List<Long> ids);
}

package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.entity.DefinitionNoticeConfigEntity;
import com.ruigu.rbox.workflow.model.entity.NodeNoticeConfigEntity;
import com.ruigu.rbox.workflow.model.entity.NoticeTemplateEntity;
import com.ruigu.rbox.workflow.model.enums.NoticeConfigState;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/10/14 15:11
 */
public interface NoticeConfigService {

    /**
     * 获取的通知模板
     *
     * @param type   类型
     * @param nodeId nodeID
     * @param event  事件
     * @return 返回通知模板列表
     */
    List<NoticeTemplateEntity> getNoticeTemplate(NoticeConfigState type, String nodeId, Integer event);

    /**
     * 获取的通知模板
     *
     * @param definitionId 流程id
     * @param event        事件
     * @return 返回通知模板列表
     */
    List<NoticeTemplateEntity> getNoticeTemplate(String definitionId, Integer event);

    /**
     * 删除通知模板根据id （逻辑）
     *
     * @param id 模板id
     */
    void deleteNoticeTemplateById(Long id);

    /**
     * 删除流程通知配置 （逻辑）
     *
     * @param definitionId 流程id
     * @param event        事件类型
     */
    void deleteDefinitionNoticeConfig(String definitionId, Integer event);

    /**
     * 删除流程通知下的某一个或某些模板
     *
     * @param definitionId 流程id
     * @param event        事件
     * @param templateIds  模板id
     */
    void deleteDefinitionNoticeConfig(String definitionId, Integer event, List<Long> templateIds);

    /**
     * 删除节点通知
     *
     * @param nodeId 节点id
     * @param event  事件
     */
    void deleteNodeNoticeConfig(String nodeId, Integer event);

    /**
     * 删除节点通知配置的某个模板
     *
     * @param event       事件
     * @param nodeId      节点id
     * @param templateIds 模板id
     */
    void deleteNodeNoticeConfig(String nodeId, Integer event, List<Long> templateIds);

    /**
     * 修改通知模板
     *
     * @param template 通知模板
     * @return 更新后的通知模板配置
     */
    NoticeTemplateEntity updateNoticeTemplate(NoticeTemplateEntity template);

    /**
     * 更新流程通知配置
     *
     * @param definitionNoticeConfig 流程定义配置
     * @return 更新后的流程通知配置
     */
    DefinitionNoticeConfigEntity updateDefinitionNoticeConfig(DefinitionNoticeConfigEntity definitionNoticeConfig);

    /**
     * 更新节点通知配置
     *
     * @param nodeNoticeConfig 节点通知配置
     * @return 更新后的节点通知配置
     */
    NodeNoticeConfigEntity updateNodeNoticeConfig(NodeNoticeConfigEntity nodeNoticeConfig);

}

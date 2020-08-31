package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.model.entity.DefinitionNoticeConfigEntity;
import com.ruigu.rbox.workflow.model.entity.NodeNoticeConfigEntity;
import com.ruigu.rbox.workflow.model.entity.NoticeTemplateEntity;
import com.ruigu.rbox.workflow.model.enums.NoticeConfigState;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.repository.DefinitionNoticeConfigRepository;
import com.ruigu.rbox.workflow.repository.NodeNoticeConfigRepository;
import com.ruigu.rbox.workflow.repository.NoticeTemplateRepository;
import com.ruigu.rbox.workflow.service.NoticeConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liqingtian
 * @date 2019/10/14 15:42
 */
@Service
public class NoticeConfigServiceImpl implements NoticeConfigService {

    @Autowired
    private NoticeTemplateRepository noticeTemplateRepository;

    @Resource
    private DefinitionNoticeConfigRepository definitionNoticeConfigRepository;

    @Resource
    private NodeNoticeConfigRepository nodeNoticeConfigRepository;

    @Override
    public List<NoticeTemplateEntity> getNoticeTemplate(NoticeConfigState type, String id, Integer event) {
        List<NoticeTemplateEntity> templates = new ArrayList<>();
        switch (type) {
            case NODE:
                templates = noticeTemplateRepository.findNodeNoticeTemplateByIdAndEvent(id, event);
                break;
            case DEFINITION:
                templates = noticeTemplateRepository.findDefinitionTemplateByIdAndEvent(id, event);
                break;
            default:
                break;
        }
        return templates;
    }

    @Override
    public List<NoticeTemplateEntity> getNoticeTemplate(String definitionId, Integer event) {
        List<NoticeTemplateEntity> templates = noticeTemplateRepository.findDefinitionTemplateByIdAndEvent(definitionId, event);
        if (CollectionUtils.isEmpty(templates)) {
            // 模糊搜索
            definitionId = definitionId + "%";
            return noticeTemplateRepository.findNodeNoticeTemplateByIdLikeAndEvent(definitionId, event);
        }
        return templates;
    }

    @Override
    public void deleteNoticeTemplateById(Long id) {
        if (id == null) {
            throw new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "模板id为空，不能删除");
        }
        noticeTemplateRepository.removeTemplateById(id);
    }

    @Override
    public void deleteDefinitionNoticeConfig(String definitionId, Integer event) {
        definitionNoticeConfigRepository.removeDefinitionNoticeByIdAndEvent(definitionId, event);
    }

    @Override
    public void deleteDefinitionNoticeConfig(String definitionId, Integer event, List<Long> templateIds) {

    }

    @Override
    public void deleteNodeNoticeConfig(String nodeId, Integer event) {
        nodeNoticeConfigRepository.removeDefinitionNoticeByIdAndEvent(nodeId, event);
    }

    @Override
    public void deleteNodeNoticeConfig(String nodeId, Integer event, List<Long> templateIds) {

    }

    @Override
    public NoticeTemplateEntity updateNoticeTemplate(NoticeTemplateEntity template) {
        return noticeTemplateRepository.save(template);
    }

    @Override
    public DefinitionNoticeConfigEntity updateDefinitionNoticeConfig(DefinitionNoticeConfigEntity definitionNoticeConfig) {
        return definitionNoticeConfigRepository.save(definitionNoticeConfig);
    }

    @Override
    public NodeNoticeConfigEntity updateNodeNoticeConfig(NodeNoticeConfigEntity nodeNoticeConfig) {
        return nodeNoticeConfigRepository.save(nodeNoticeConfig);
    }
}

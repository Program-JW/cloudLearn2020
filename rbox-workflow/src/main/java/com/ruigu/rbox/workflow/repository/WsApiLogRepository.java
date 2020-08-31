package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.WsApiLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author caojinghong
 * @date 2020/01/08 17:35
 */
public interface WsApiLogRepository extends JpaRepository<WsApiLogEntity, Integer>, JpaSpecificationExecutor<WsApiLogEntity> {
    /**
     * 根据请求唯一标识messageId查询该webSocket操作日志记录
     * @param messageId
     * @return
     */
    WsApiLogEntity findFirstByMessageId(String messageId);
}

package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.entity.WorkOrderEntity;
import com.ruigu.rbox.workflow.model.request.CreateWorkOrderRequest;

/**
 * @author alan.zhao
 */
public interface WorkOrderService {

    /**
     * 创建工单
     *
     * @param request 工单数据
     * @param userId 操作人ID
     * @return 工单ID
     * @throws Exception 抛出Exception
     */
    long create(CreateWorkOrderRequest request, Long userId) throws Exception;

    /**
     * 关闭工单
     *
     * @param id 工单ID
     * @param userId 操作人ID
     * @throws Exception 抛出Exception
     */
    void close(Long id, Long userId) throws Exception;

    /**
     * 作废工单
     *
     * @param id 工单ID
     * @param userId 操作人ID
     * @throws Exception 抛出Exception
     */
    void delete(Long id, Long userId) throws Exception;

    /**
     * 保存忽略空值字段
     *
     * @param entity 要保存的实体数据
     * @param userId 操作人ID
     * @throws Exception 可能会抛出Exception
     */
    void saveIgnoreNull(WorkOrderEntity entity, Long userId) throws Exception;
}
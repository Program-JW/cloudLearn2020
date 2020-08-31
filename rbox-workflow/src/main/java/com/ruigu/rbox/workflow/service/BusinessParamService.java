package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.entity.BusinessParamEntity;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/10/10 16:23
 */
public interface BusinessParamService {

    /**
     * 批量保存业务参数
     *
     * @param businessParamList 参数列表
     */
    void saveAllBusinessParam(List<BusinessParamEntity> businessParamList);

    /**
     * 查询业务参数
     *
     * @param instanceId 实例id
     * @return 该实例的业务参数
     */
    List<BusinessParamEntity> getAllBusinessParamByInstanceId(String instanceId);
}

package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.BusinessParamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/10/10 16:26
 */
public interface BusinessParamRepository extends JpaRepository<BusinessParamEntity, Integer> {

    /**
     * 通过实例id获取实例业务参数
     *
     * @param instanceId 实例id
     * @return 业务参数
     */
    List<BusinessParamEntity> findByInstanceId(String instanceId);
}

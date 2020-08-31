package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.model.entity.BusinessParamEntity;
import com.ruigu.rbox.workflow.repository.BusinessParamRepository;
import com.ruigu.rbox.workflow.service.BusinessParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/10/10 16:25
 */
@Service
public class BusinessParamServiceImpl implements BusinessParamService {

    @Autowired
    private BusinessParamRepository businessParamRepository;

    @Override
    public void saveAllBusinessParam(List<BusinessParamEntity> businessParamList) {
        businessParamRepository.saveAll(businessParamList);
    }

    @Override
    public List<BusinessParamEntity> getAllBusinessParamByInstanceId(String instanceId) {
        return businessParamRepository.findByInstanceId(instanceId);
    }
}

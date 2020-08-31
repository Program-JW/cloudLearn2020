package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.model.entity.WsApiLogEntity;
import com.ruigu.rbox.workflow.repository.WsApiLogRepository;
import com.ruigu.rbox.workflow.service.WsApiLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author liqingtian
 * @date 2020/01/11 18:11
 */
@Service
public class WsApiLogServiceImpl implements WsApiLogService {

    @Resource
    private WsApiLogRepository wsApiLogRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void insertLog(WsApiLogEntity wsApiLogEntity) {
        wsApiLogRepository.save(wsApiLogEntity);
    }
}

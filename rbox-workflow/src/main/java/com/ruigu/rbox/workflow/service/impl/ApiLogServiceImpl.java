package com.ruigu.rbox.workflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruigu.rbox.workflow.model.entity.ApiLogEntity;
import com.ruigu.rbox.workflow.repository.ApiLogRepository;
import com.ruigu.rbox.workflow.service.ApiLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 记录请求日志到日志表
 *
 * @author alan.zhao
 */
@Service
public class ApiLogServiceImpl implements ApiLogService {

    @Resource
    private ApiLogRepository apiLogRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiLogEntity logRequest(String method, String url, Object param) {
        String requestJson = param != null ? JSON.toJSONString(param) : "";
        ApiLogEntity apiLogEntity = new ApiLogEntity();
        apiLogEntity.setMethodName(method);
        apiLogEntity.setRequestUrl(url);
        apiLogEntity.setRequestData(requestJson);
        apiLogEntity.setCreateOn(new Date());
        apiLogRepository.save(apiLogEntity);
        return apiLogEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logResponse(ApiLogEntity apiLogEntity, Object response) {
        String responseJson = response != null ? JSON.toJSONString(response) : "";
        apiLogEntity.setResponseData(responseJson);
        apiLogRepository.save(apiLogEntity);
    }
}

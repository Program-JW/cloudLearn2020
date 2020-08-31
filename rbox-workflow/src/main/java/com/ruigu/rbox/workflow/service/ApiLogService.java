package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.entity.ApiLogEntity;

/**
 * 记录请求日志到日志表
 *
 * @author alan.zhao
 */
public interface ApiLogService {

    /**
     * 记录日志请求
     *
     * @param method http method
     * @param url    http url
     * @param param  参数
     * @return 日志记录
     */
    ApiLogEntity logRequest(String method, String url, Object param);

    /**
     * 更新响应到日志记录
     *
     * @param apiLogEntity 日志记录
     * @param response     响应
     */
    void logResponse(ApiLogEntity apiLogEntity, Object response);

}

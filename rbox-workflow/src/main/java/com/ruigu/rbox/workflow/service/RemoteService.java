package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.ServerResponse;

import java.util.Map;

/**
 * 通用的远程调用服务
 *
 * @author alan.zhao
 */
public interface RemoteService {

    /**
     * 事务提交之后发送请求
     * 支持服务发现和负载均衡
     *
     * @param method 接口的method
     * @param url    接口的url
     * @param param  接口的参数
     * @return 响应
     */
    void requestAfterCommit(String method, String url, Map<String, Object> param);

    /**
     * 发送请求
     * 支持服务发现和负载均衡
     *
     * @param method 接口的method
     * @param url    接口的url
     * @param param  接口的参数
     * @return 响应
     */
    ServerResponse<Object> request(String method, String url, Map<String, Object> param);
}

package com.ruigu.rbox.workflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.ApiLogEntity;
import com.ruigu.rbox.workflow.repository.ApiLogRepository;
import com.ruigu.rbox.workflow.service.ApiLogService;
import com.ruigu.rbox.workflow.service.RemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * 通用的远程调用服务
 *
 * @author alan.zhao
 */
@Slf4j
@Service
public class RemoteServiceImpl implements RemoteService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ApiLogService apiLogService;

    /**
     * 事务提交之后发送请求
     * 支持服务发现和负载均衡
     *
     * @param method 接口的method
     * @param url    接口的url
     * @param param  接口的参数
     * @return 响应
     */
    @Override
    public void requestAfterCommit(String method, String url, Map<String, Object> param) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    request(method, url, param);
                }
            });
        } else {
            request(method, url, param);
        }
    }

    /**
     * 发送请求
     * 支持服务发现和负载均衡
     *
     * @param method 接口的method
     * @param url    接口的url
     * @param param  接口的参数
     * @return 响应
     */
    @Override
    public ServerResponse<Object> request(String method, String url, Map<String, Object> param) {
        ApiLogEntity apiLogEntity = apiLogService.logRequest(method, url, param);
        ServerResponse<Object> response = null;
        if (HttpMethod.GET.toString().equalsIgnoreCase(method)) {
            response = restTemplate.getForObject(url, ServerResponse.class, param);
        } else if (HttpMethod.POST.toString().equalsIgnoreCase(method)) {
            response = putOrPost(method, url, param);
        } else if (HttpMethod.PUT.toString().equalsIgnoreCase(method)) {
            response = putOrPost(method, url, param);
        }
        apiLogService.logResponse(apiLogEntity, response);
        log.info("远程调用,{} url={},参数={}", method, url, apiLogEntity.getRequestData(), apiLogEntity.getResponseData());
        return response;
    }


    /**
     * 发送POST/PUT请求
     * 支持服务发现和负载均衡
     *
     * @param method 接口的method
     * @param url    接口的url
     * @param param  接口的参数
     * @return 响应
     */
    public ServerResponse<Object> putOrPost(String method, String url, Map<String, Object> param) {
        HttpMethod method1 = HttpMethod.PUT.toString().equalsIgnoreCase(method) ? HttpMethod.PUT : HttpMethod.POST;
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(param), header);
        ResponseEntity<ServerResponse> resultEntity = restTemplate.exchange(url, method1, entity, ServerResponse.class);
        return resultEntity.getBody();
    }
}

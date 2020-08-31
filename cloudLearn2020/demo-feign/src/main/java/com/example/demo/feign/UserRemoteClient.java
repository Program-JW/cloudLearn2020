package com.example.demo.feign;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/25 15:31
 */
@FeignClient(value = "cloud-eureka-say7002", fallbackFactory = UserRemoteFallBack.class)
public interface UserRemoteClient {

    @LoadBalanced
    @GetMapping("/user/hello")
    String hello();
}

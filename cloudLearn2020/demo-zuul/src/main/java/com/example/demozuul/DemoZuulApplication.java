package com.example.demozuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

//@EnableZuulProxy
@EnableDiscoveryClient
@EnableEurekaClient
@SpringBootApplication
public class DemoZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoZuulApplication.class, args);
    }

}

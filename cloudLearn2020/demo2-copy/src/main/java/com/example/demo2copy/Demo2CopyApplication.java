package com.example.demo2copy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class Demo2CopyApplication {

    public static void main(String[] args) {
        SpringApplication.run(Demo2CopyApplication.class, args);
    }

}

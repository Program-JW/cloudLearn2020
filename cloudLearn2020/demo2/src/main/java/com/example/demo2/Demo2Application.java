package com.example.demo2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class Demo2Application {

    public static void main(String[] args) {
        System.out.println(11123);
        System.out.println(456);
        System.out.println(193);
        SpringApplication.run(Demo2Application.class, args);
    }

}

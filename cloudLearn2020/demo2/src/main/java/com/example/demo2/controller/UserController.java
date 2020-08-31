package com.example.demo2.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/25 15:06
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RabbitTemplate template;

//    @Autowired
//    private Rabbit


    @GetMapping("/hello")
    public String sayHello() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        template.convertAndSend("CalonDirectExchange", "CalonDirectRouting", "message" + Math.random() % 1000);
        return "Hello";
    }

    @GetMapping("/get")
    public String getHelloMsg() {
        return template.receive("CalonDirectQueue").toString();
    }


}

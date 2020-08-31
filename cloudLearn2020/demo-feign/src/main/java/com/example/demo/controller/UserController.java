package com.example.demo.controller;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/25 16:44
 */

import com.example.demo.service.SayHelloServiece;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/")
public class UserController {
    @Resource
    private SayHelloServiece sayHelloServiece;

    @GetMapping("/say-hello")
    public void sayHello(){
        sayHelloServiece.sayHello();
    }

}

package com.example.demo2copy.controller;

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
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello";
    }
}

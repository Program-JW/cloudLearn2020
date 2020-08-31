package com.ruigu.learn.demo;

import com.ruigu.learn.demo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void contextLoads() {
        userService.findByNameAndPassword("name","password");
    }

}

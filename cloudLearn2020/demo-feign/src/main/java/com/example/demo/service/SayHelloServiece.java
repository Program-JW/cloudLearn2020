package com.example.demo.service;

import com.example.demo.feign.UserRemoteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/25 15:56
 */
@Service
public class SayHelloServiece {
    @Autowired
    private UserRemoteClient userRemoteClient;

    public void sayHello(){
        System.out.println(userRemoteClient.hello());
    }

}

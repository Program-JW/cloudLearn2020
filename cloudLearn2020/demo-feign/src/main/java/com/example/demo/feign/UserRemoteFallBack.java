package com.example.demo.feign;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/25 17:41
 */
@Component
public class UserRemoteFallBack implements FallbackFactory<UserRemoteClient> {
    @Override
    public UserRemoteClient create(Throwable throwable) {
        System.out.println("---------------"+"调用出现异常了");
        return null;
    }
}

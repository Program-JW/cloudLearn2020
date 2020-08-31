package com.ruigu.rbox.workflow.feign;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liqingtian
 * @date 2020/02/14 14:47
 */
@Configuration
public class PassportFeignConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        //这里记录所有，根据实际情况选择合适的日志level
        return Logger.Level.FULL;
    }
}

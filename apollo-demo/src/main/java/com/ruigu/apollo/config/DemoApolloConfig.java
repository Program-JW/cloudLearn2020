package com.ruigu.apollo.config;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableApolloConfig
public class DemoApolloConfig {
    @Bean
    public DemoGlobalConfig globalConfig() {
        return new DemoGlobalConfig();
    }
}
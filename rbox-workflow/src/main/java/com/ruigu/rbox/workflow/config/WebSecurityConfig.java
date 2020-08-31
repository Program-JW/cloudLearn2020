package com.ruigu.rbox.workflow.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 认证相关配置
 *
 * @author alan.zhao
 * @date 2019/09/16 18:49
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @SneakyThrows
    protected void configure(HttpSecurity http) {
        http.anonymous();
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger-resources/**", "/v2/api-docs", "/swagger-ui.html");
        web.ignoring().antMatchers("/ajax/**", "/css/**", "/file/**", "/fonts/**", "/img/**", "/js/**", "/ruoyi/**", "/webjars/**");
    }
}

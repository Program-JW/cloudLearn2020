package com.ruigu.rbox.workflow.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruigu.rbox.cloud.kanai.security.interceptor.UserInfoInterceptor;
import com.ruigu.rbox.cloud.kanai.web.converter.StringToLocalDateTimeConverter;
import com.ruigu.rbox.workflow.interceptor.SecureInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * @author alan.zhao
 */
@Configuration
public class WebAppConfigure implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        /*
         * 排除掉原来的MappingJackson2HttpMessageConverter
         */
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                ObjectMapper objectMapper = new ObjectMapper();
                /*
                 * 将long类型的数据转为String类型
                 */
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
                simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
                simpleModule.addSerializer(long.class, ToStringSerializer.instance);
                objectMapper.registerModule(simpleModule);

                SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                objectMapper.setDateFormat(smt);
                objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));

                jackson2HttpMessageConverter.setObjectMapper(objectMapper);
            }
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInfoInterceptor());
        registry.addInterceptor(new SecureInterceptor());
    }

    @Bean
    public Converter convert() {
        return new StringToLocalDateTimeConverter();
    }

}

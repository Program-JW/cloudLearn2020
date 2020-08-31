package com.ruigu.rbox.workflow.config;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author lijiajia
 * @date 2019/6/21 15:55
 */
@Slf4j
public class FeignAuthConfiguration {
    @Value("${server.mp.api.secret}")
    private String secret;
    @Value("${server.mp.api.key}")
    private String key;

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public FeignBasicAuthRequestInterceptor requestInterceptor() {
        return new FeignBasicAuthRequestInterceptor();
    }

    private class FeignBasicAuthRequestInterceptor implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate template) {

            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            String nonce = String.valueOf((Math.random() * 9 + 1) * 100000);
            List<String> list = new ArrayList<>();
            list.add(key);
            list.add(secret);
            list.add(timestamp);
            list.add(nonce);
            Collections.sort(list);
            String toMd5 = String.join("", list);
            String signature = DigestUtils.md5DigestAsHex(toMd5.getBytes());

            Map<String, Object> param = JsonUtil.parseMap(template.requestBody().asString());
            param.put("signature", signature);
            param.put("timestamp", timestamp);
            param.put("key", key);
            param.put("nonce", nonce);
            template.body(Request.Body.encoded(JsonUtil.toJsonString(param).getBytes(), StandardCharsets.UTF_8));
        }
    }
}

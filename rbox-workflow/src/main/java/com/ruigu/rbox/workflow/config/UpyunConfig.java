package com.ruigu.rbox.workflow.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 又拍云java-sdk配置
 * @author alan.zhao
 **/
@Data
@Configuration
@ConfigurationProperties("upyun")
public class UpyunConfig {
   private String bucketName;
   private String username;
   private String password;
   private String storePath;
   private String prefix;
   private String filePathPrefix;
}

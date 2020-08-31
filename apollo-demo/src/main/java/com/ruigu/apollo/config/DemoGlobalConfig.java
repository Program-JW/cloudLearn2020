package com.ruigu.apollo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class DemoGlobalConfig {
    @Value("${current.version}")
    public String currentVersion;
    @Value("${function.switch.open}")
    public boolean switchFlag;
}
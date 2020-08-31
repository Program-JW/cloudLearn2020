package com.ruigu.rbox.workflow.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author caojinghong
 * @date 2020/01/11 16:54
 */
@ConfigurationProperties(prefix = "rbox.workflow.lightning.report.user-id")
@Configuration
@Data
public class ReportEmailTargetParamProperties {
    private List<Integer> targetList;
    private List<Integer> ccTargetList;
}

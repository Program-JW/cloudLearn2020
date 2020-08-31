package com.ruigu.rbox.workflow.supports;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liqingtian
 * @date 2019/09/19 12:05
 */
public class UrlUtil {
    public static String setBusinessAndTaskParam(String url, String businessKey, String taskId) {
        if(StringUtils.isBlank(url)){
            return null;
        }
        return url.replace("#{business_key}", businessKey).replace("#{task_id}", taskId);
    }

    public static String setBusinessAndInstanceParam(String url, String businessKey, String instanceId) {
        if(StringUtils.isBlank(url)){
            return null;
        }
        return url.replace("#{business_key}", businessKey).replace("#{instance_id}", instanceId);
    }
}

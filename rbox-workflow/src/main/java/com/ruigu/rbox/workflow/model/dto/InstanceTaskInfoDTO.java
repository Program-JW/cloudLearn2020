package com.ruigu.rbox.workflow.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liqingtian
 * @date 2020/01/06 17:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstanceTaskInfoDTO {
    private String businessKey;
    private String taskId;
}

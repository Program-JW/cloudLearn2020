package com.ruigu.rbox.workflow.supports;

import com.ruigu.rbox.workflow.model.dto.BaseTaskInfoDTO;
import com.ruigu.rbox.workflow.model.entity.TaskEntity;
import com.ruigu.rbox.workflow.model.vo.TaskVO;

/**
 * @author liqingtian
 * @date 2019/12/24 19:23
 */
public class ConvertClassUtil {
    /**
     * 任务信息 转换 base任务
     */
    public static BaseTaskInfoDTO convertToBaseTaskInfoDTO(TaskEntity task) {
        return new BaseTaskInfoDTO(task.getInstanceId(), task.getId(), task.getName(), task.getCreatedOn());
    }

    public static BaseTaskInfoDTO convertToBaseTaskInfoDTO(TaskVO task) {
        return new BaseTaskInfoDTO(task.getInstanceId(), task.getId(), task.getName(), task.getCreatedOn());
    }
}

package com.ruigu.rbox.workflow.model.vo;

import com.ruigu.rbox.workflow.model.response.Form;
import lombok.Data;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/09/05 13:59
 */
@Data
public class InstanceDetailVO {
    private WorkflowInstanceVO instance;
    private List<TaskVO> tasks;
    private Form form;
    private List<TaskCommentVO> comments;
}

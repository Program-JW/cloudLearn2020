package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.entity.WorkflowFormEntity;
import com.ruigu.rbox.workflow.model.request.SubmitFormRequest;
import com.ruigu.rbox.workflow.model.vo.WorkflowCategoryNode;

import java.util.List;

/**
 * @author ：jianghuilin
 * @date ：Created in {2019/8/28} {16:13}
 */
public interface WorkflowCategoryService {
    /**
     * 查询流程分类信息
     *
     * @return 流程分类列表
     * @throws  Exception 业务异常
     */
    List<WorkflowCategoryNode> search() throws Exception;


    /**
     * 通过流程分类Id查找对应的json 生成表格
     *
     * @param  id 流程分类ID
     * @return 流程表单实体
     * @throws  Exception 业务异常
     */
    WorkflowFormEntity json(Integer id) throws Exception;


    /**
     * 提交表单
     *
     * @param  submitFormRequest 请求
     * @param  userId 当前操作人
     * @throws  Exception 业务异常
     */
    void submitForm(SubmitFormRequest submitFormRequest, Long userId) throws Exception ;
}

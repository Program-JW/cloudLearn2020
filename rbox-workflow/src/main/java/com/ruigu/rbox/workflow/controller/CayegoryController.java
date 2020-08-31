package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.WorkflowFormEntity;
import com.ruigu.rbox.workflow.model.request.SubmitFormRequest;
import com.ruigu.rbox.workflow.model.vo.WorkflowCategoryNode;
import com.ruigu.rbox.workflow.service.WorkflowCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：jianghuilin
 * @date ：Created in {2019/8/28} {14:42}
 */
@RequestMapping(value = "/category")
@RestController
public class CayegoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CayegoryController.class);

    @Autowired
    private WorkflowCategoryService workflowCategoryService;

    /**
     * 查找流程分类信息
     *
     * @author ：jianghuilin
     * @date ：Created in {2019/8/29} {10:09}
     */
    @GetMapping(value = "/search")
    public ServerResponse search() {
        try {
            List<WorkflowCategoryNode> data = workflowCategoryService.search();
            return ServerResponse.ok(data);
        } catch (Exception e) {
            logger.error("", e);
            return ServerResponse.fail(e.getMessage());
        }
    }

    /**
     * 通过流程分类Id查找对应的json 生成表格
     *
     * @author ：jianghuilin
     * @date ：Created in {2019/8/29} {10:09}
     */
    @GetMapping(value = "/json")
    public ServerResponse json(@RequestParam(name = "id", required = true) Integer id) {
        try {
            WorkflowFormEntity data = workflowCategoryService.json(id);
            return ServerResponse.ok(data);
        } catch (Exception e) {
            logger.error("", e);
            return ServerResponse.fail(e.getMessage());
        }
    }

    /**
     * 表单提交后启动一个进程实体
     *
     * @author ：jianghuilin
     * @date ：Created in {2019/8/29} {20:15}
     */
    @PostMapping(value = "/submit/form", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServerResponse<String> submitForm(@RequestBody SubmitFormRequest request)  {
        try {
            workflowCategoryService.submitForm(request, userId());
            return ServerResponse.ok("提交成功");
        } catch (Exception e) {
            logger.error("", e);
            return ServerResponse.fail(e.getMessage());
        }
    }

}

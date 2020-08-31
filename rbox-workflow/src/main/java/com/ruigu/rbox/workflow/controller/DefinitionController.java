package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.NodeEntity;
import com.ruigu.rbox.workflow.model.entity.WorkflowDefinitionEntity;
import com.ruigu.rbox.workflow.model.request.SaveDefinitionRequest;
import com.ruigu.rbox.workflow.model.request.SaveFormRequest;
import com.ruigu.rbox.workflow.model.request.SearchDefinitionRequest;
import com.ruigu.rbox.workflow.model.response.WorkflowDefinitionDetail;
import com.ruigu.rbox.workflow.repository.WorkflowDefinitionRepository;
import com.ruigu.rbox.workflow.service.DistributedLocker;
import com.ruigu.rbox.workflow.service.WorkflowDefinitionService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author alan.zhao
 */
@Controller
@RequestMapping(value = "/definition")
public class DefinitionController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(DefinitionController.class);

    @Autowired
    private WorkflowDefinitionRepository workflowDefinitionRepository;

    @Autowired
    private WorkflowDefinitionService workflowDefinitionService;

    @RequestMapping(value = "/create-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServerResponse<WorkflowDefinitionDetail> createDraft(@RequestBody SaveDefinitionRequest data, HttpServletRequest request) throws Exception {
        String pk = workflowDefinitionService.createDraft(data, userId());
        return detail(pk);
    }

    @RequestMapping(value = "/save-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServerResponse<WorkflowDefinitionDetail> saveDraft(@RequestBody SaveDefinitionRequest data, HttpServletRequest request) throws Exception {
        String pk = workflowDefinitionService.saveAsDraft(data, userId());
        return detail(pk);
    }

    @RequestMapping(value = "/save-as-draft", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServerResponse<WorkflowDefinitionDetail> saveAsDraft(@RequestBody SaveDefinitionRequest data, HttpServletRequest request) throws Exception {
        data.setNewVersionIfReleased(true);
        String pk = workflowDefinitionService.saveAsDraft(data, userId());
        return detail(pk);
    }

    @RequestMapping(value = "/publish", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServerResponse<WorkflowDefinitionDetail> publish(@RequestBody SaveDefinitionRequest data, HttpServletRequest request) throws Exception {
        String pk = workflowDefinitionService.publish(data, userId());
        return detail(pk);
    }

    @RequestMapping(value = "/set-form", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServerResponse saveForm(@RequestBody SaveFormRequest data, HttpServletRequest request) throws Exception {
        workflowDefinitionService.setForm(data, userId());
        return ServerResponse.ok();
    }

    @GetMapping(value = "/graph")
    @ResponseBody
    public ServerResponse<String> graph(@RequestParam("id") String modelId) {
        return ServerResponse.ok(workflowDefinitionService.loadGraph(modelId));
    }

    @RequestMapping("/search")
    @ResponseBody
    public ServerResponse<Page<WorkflowDefinitionEntity>> list(SearchDefinitionRequest data) {
        return ServerResponse.ok(workflowDefinitionService.list(data));
    }

    @RequestMapping(value = "/info")
    @ResponseBody
    public ServerResponse<WorkflowDefinitionDetail> detail(@RequestParam("id") String modelId) {
        return ServerResponse.ok(workflowDefinitionService.info(modelId));
    }

    @RequestMapping(value = "/latest")
    @ResponseBody
    public ServerResponse<WorkflowDefinitionDetail> latest(@RequestParam("id") String modelId) {
        return ServerResponse.ok(workflowDefinitionService.latest(modelId));
    }

    @RequestMapping(value = "/latest/released")
    @ResponseBody
    public ServerResponse<WorkflowDefinitionDetail> latestReleased(@RequestParam("key") String key) {
        return ServerResponse.ok(workflowDefinitionService.latestReleased(key));
    }

    @RequestMapping(value = "/versions")
    @ResponseBody
    public ServerResponse<List<WorkflowDefinitionEntity>> version(@RequestParam("key") String key) {
        return ServerResponse.ok(workflowDefinitionRepository.versions(key));
    }

    @RequestMapping(value = "/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServerResponse<String> delete(@RequestBody SaveDefinitionRequest data) {
        return ServerResponse.ok(workflowDefinitionService.deleteDraft(data));
    }

    @GetMapping(value = "/list")
    @ResponseBody
    public ServerResponse<List<Map<String, Object>>> getDefinitionList() {
        return ServerResponse.ok(workflowDefinitionService.getDefinitionList());
    }

    @RequestMapping(value = "/node/info")
    @ResponseBody
    public ServerResponse<NodeEntity> nodeInfo(@RequestParam("id") String nodeId) {
        return ServerResponse.ok(workflowDefinitionService.nodeInfo(nodeId));
    }

    @Autowired
    private DistributedLocker distributedLocker;

    @RequestMapping(value = "/test")
    @ResponseBody
    public ServerResponse<String> test(@RequestParam("id") String id) {
        try {
            distributedLocker.lock("test1",1);
            logger.info("获得,id=" + id);
            Thread.sleep(10000);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            distributedLocker.unlock("test1");
            logger.info("释放,id=" + id);
        }
        return ServerResponse.ok("ok");
    }
}

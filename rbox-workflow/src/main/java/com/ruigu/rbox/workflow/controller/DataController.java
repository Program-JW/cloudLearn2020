package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.cloud.kanai.security.UserInfo;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.request.StartInstanceRequest;
import com.ruigu.rbox.workflow.service.WorkflowInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author alan.zhao
 */
@Controller
@RequestMapping(value = "/data")
public class DataController {

    @Autowired
    private WorkflowInstanceService workflowInstanceService;

    @PostMapping(value = "/apply")
    @ResponseBody
    public ServerResponse<List<String>> apply(@RequestBody StartInstanceRequest data) throws Exception {
        if (data.getCreatorId() == null || data.getCreatorId() == 0) {
            return ServerResponse.fail(400, "创建人ID不能为空或ID不能为0");
        }
        // 设置登陆人信息
        UserInfo info = new UserInfo();
        info.setUserId(data.getCreatorId().intValue());
        info.setUserName("mp");
        UserHelper.setUserInfo(info);
        List<String> pks = new ArrayList<>();
        int count = 10;
        for (int i = 0; i < count; i++) {
            data.setBusinessKey(UUID.randomUUID().toString());
            String pk = workflowInstanceService.start(data, UserHelper.getUserId().longValue());
            pks.add(pk);
        }
        return ServerResponse.ok(pks);
    }
}

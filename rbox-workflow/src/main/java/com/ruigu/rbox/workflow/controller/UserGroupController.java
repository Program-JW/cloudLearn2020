package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.entity.UserGroupAssoEntity;
import com.ruigu.rbox.workflow.model.entity.UserGroupEntity;
import com.ruigu.rbox.workflow.model.request.SelectRequest;
import com.ruigu.rbox.workflow.model.request.UserGroupAssoRequest;
import com.ruigu.rbox.workflow.model.request.UserGroupRequest;
import com.ruigu.rbox.workflow.model.vo.SelectOption;
import com.ruigu.rbox.workflow.model.vo.UserGroupAssoVO;
import com.ruigu.rbox.workflow.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/09/02 19:59
 */
@RestController
public class UserGroupController {

    @Autowired
    private UserGroupService userGroupService;

    @GetMapping("/user/groups")
    public ServerResponse<Page<UserGroupEntity>> getAllUserGroupPage(UserGroupRequest request) {
        return ServerResponse.ok(userGroupService.selectGroupPage(request));
    }

    @GetMapping("/user/group/{groupId}")
    public ServerResponse<Page<UserGroupAssoVO>> getAllUserGroupAssoPage(UserGroupAssoRequest request) {
        return ServerResponse.ok(userGroupService.selectGroupAssoPage(request));
    }

    @GetMapping("/user/group/info/{groupId}")
    public ServerResponse<UserGroupEntity> getUserGroupById(@PathVariable Integer groupId) {
        return ServerResponse.ok(userGroupService.getUserGroupById(groupId));
    }

    @PostMapping("/user/group/add")
    public ServerResponse addUserGroup(@RequestBody UserGroupEntity userGroupEntity) {
        userGroupService.createUserGroup(userGroupEntity);
        return ServerResponse.ok();
    }

    @PostMapping("/user/group/edit")
    public ServerResponse editUserGroup(@RequestBody UserGroupEntity userGroupEntity) {
        userGroupService.updateUserGroup(userGroupEntity);
        return ServerResponse.ok();
    }

    @PostMapping("/user/group/remove")
    public ServerResponse removeUserGroup(@RequestBody UserGroupAssoEntity userGroupEntity) {
        userGroupService.removeUserGroup(userGroupEntity.getId());
        return ServerResponse.ok();
    }

    @PostMapping("/user/group/adduser")
    public ServerResponse addUserToGroup(@RequestBody UserGroupAssoRequest userGroupAssoRequest) {
        userGroupService.addUserToGroup(userGroupAssoRequest);
        return ServerResponse.ok();
    }

    @PostMapping("/user/group/removeuser")
    public ServerResponse removeUserToGroup(@RequestBody UserGroupAssoEntity userGroupAsso) {
        return userGroupService.removeUserToGroup(userGroupAsso);
    }

    /**
     * 选择用户组接口
     *
     * @param request 参数
     * @return
     */
    @PostMapping(value = "/user/group/select")
    @ResponseBody
    public ServerResponse<List<SelectOption>> select(@RequestBody SelectRequest request) {
        return ServerResponse.ok(userGroupService.selectGroups(request));
    }
}

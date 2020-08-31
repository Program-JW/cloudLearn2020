package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.dto.BuildGroupDTO;
import com.ruigu.rbox.workflow.model.enums.LightningApplyStatus;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/01/08 15:31
 */
public interface ChatWebSocketService {

    /**
     * 登陆
     *
     * @param userId 登陆人
     */
    void login(String userId);

    /**
     * 机器人登陆
     */
    void loginRobot();

    /**
     * 建群
     *
     * @param buildGroupList 批量建表信息列表
     */
    void buildGroup(List<BuildGroupDTO> buildGroupList);


    /**
     * 添加群成员
     *
     * @param issueId   问题id
     * @param userId    人员id
     * @param addUserId 添加人员id
     * @param groupId   群id
     */
    void addUserToGroup(Integer issueId, Long groupId, Integer userId, Integer addUserId);

    /**
     * 机器人发送消息
     *
     * @param issueId 问题id
     * @param groupId 群组id
     * @param content 消息内容
     * @param action  动作
     */
    void sendMessage(Integer issueId, Long groupId, String content, String action);

    /**
     * 闪电链操作系统通知
     *
     * @param issueId 问题id
     * @param groupId 群组id
     * @param userId  操作人
     * @param action  动作
     */
    void sendActionMessage(Integer issueId, Long groupId, Integer userId, LightningApplyStatus action);

    /**
     * 发送交接信息
     *
     * @param issueId   问题id
     * @param userId    人员id
     * @param addUserId 添加人员id
     * @param groupId   群id
     * @param transferType     交接类型
     */
    void sendAssociateMessage(Integer issueId, Long groupId, Integer userId, Integer addUserId, Integer transferType);

    /**
     * 关闭群
     *
     * @param groupId 群组id
     * @param issueId 问题id
     */
    void closeGroup(Long groupId, Integer issueId);
}

package com.ruigu.rbox.workflow.model;

/**
 * @author cg1
 * @date 2019-12-21 17:37
 */
public interface ActionConstants {

    /**
     * 登陆
     */
    int LOGIN = 1;

    /**
     * 登出
     */
    int LOGOUT = 2;
    /**
     * 点对点发送消息
     */
    int SENDING_ONE_TEXT = 3;

    /**
     * 群发文本消息
     */
    int SENDING_GROUP_TEXT = 4;

    /**
     * 给个人发图片
     */
    int SENDING_ONE_PICTURE = 5;

    /**
     * 群发图片
     */
    int SENDING_GROUP_PICTURE = 6;

    /**
     * 拉人入群
     */
    int ADD_GROUP = 7;
    /**
     * 拉人并创建群组
     */
    int CREATE_GROUP = 8;

    /**
     * 退出群聊
     */
    int LEAVE_GROUP = 9;
    /**
     * 关闭群
     */
    int CLOSED_GROUP = 10;
    /**
     * 转出群
     */
    int GROUP_CHANGE_USER = 11;
    /**
     * 群消息撤回
     */
    int WITHDRAW_GROUP_MSG = 12;
    /**
     * 消息刷新
     */
    int FLUSH_GROUP_MSG = 13;
    /**
     * 消息应答
     */
    int GROUP_MSG_ACK = 14;
    /**
     * 主动进群
     */
    int ACTIVE_ADD_GROUP = 15;
    /**
     * 第三方机器人 发送消息
     */
    int EXT_ROBOT_GROUP = 16;
    /**
     * 被中途拉入群消息
     */
    //int MIDDLE_ADD_GROUP = 17;
    /**
     * 更新群title
     */
    int UPDATE_GROUP_TITLE = 18;
    /**
     * 群组中所有历史消息
     */
    int GROUP_HISTORY_MESSAGE = 19;
    /**
     * 获取群组未读消息
     */
    int GROUP_UNREAD_MESSAGE = 20;

    /**
     * 机器人发送群消息
     */
    int ROBOT_SEND_GROUP_MESSAGE = 21;

    /**
     * 机器人发送添加人员消息
     */
    int ROBOT_ADD_USER = 22;

    /**
     * 机器人关闭群
     */
    int ROBOT_CLOSE_GROUP = 25;
}

package com.ruigu.rbox.workflow.model.dto;

import lombok.Data;

/**
 * @author dong jing xi
 * @date 2019/9/5 18:45
 **/
@Data
public class WeChatCallbackUserDTO {

    /**
     * 企业微信CorpID
     */
//    @JsonProperty("ToUserName")
    private String toUserName;
    /**
     * 此事件该值固定为sys，表示该消息由系统生成
     */
//    @JsonProperty("FromUserName")
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
//    @JsonProperty("CreateTime")
    private Long createTime;
    /**
     * 消息的类型，此时固定为event
     */
//    @JsonProperty("MsgType")
    private String msgType;
    /**
     * 事件的类型，此时固定为change_contact
     */
//    @JsonProperty("Event")
    private String event;
    /**
     * 事件的类型
     */
//    @JsonProperty("ChangeType")
    private String changeType;
    /**
     * 成员UserID
     */
//    @JsonProperty("UserID")
    private String userId;

    private String newUserId;
    /**
     * 成员名称
     */
//    @JsonProperty("Name")
    private String name;
    /**
     * 成员部门列表，仅返回该应用有查看权限的部门id
     */
//    @JsonProperty("Department")
    private String department;
    /**
     * 表示所在部门是否为上级，0-否，1-是，顺序与Department字段的部门逐一对应
     */
//    @JsonProperty("IsLeaderInDept")
    private String isLeaderInDept;
    /**
     * 手机号码
     */
//    @JsonProperty("Mobile")
    private String mobile;
    /**
     * 职位信息。长度为0~64个字节
     */
//    @JsonProperty("Position")
    private String position;
    /**
     * 性别，1表示男性，2表示女性
     */
//    @JsonProperty("Gender")
    private Integer gender;
    /**
     * 邮箱
     */
//    @JsonProperty("Email")
    private String email;
    /**
     * 激活状态：1=已激活 2=已禁用 4=未激活 已激活代表已激活企业微信或已关注微工作台（原企业号）。
     */
//    @JsonProperty("Status")
    private Integer status;
    /**
     * 头像url。注：如果要获取小图将url最后的”/0”改成”/100”即可。
     */
//    @JsonProperty("Avatar")
    private String avatar;
    /**
     * 成员别名
     */
//    @JsonProperty("Alias")
    private String alias;
    /**
     * 座机
     */
//    @JsonProperty("Telephone")
    private String telephone;
    /**
     * 地址
     */
//    @JsonProperty("Address")
    private String address;

}

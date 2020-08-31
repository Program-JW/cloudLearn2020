package com.ruigu.rbox.workflow.model.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author caojinghong
 * @date 2020/03/16 16:02
 **/
public enum WeChatCallBackTypeEnum {


    /**
     * 创建用户
     */
    CREATE_USER("create_user"),
    /**
     * 更新用户
     */
    UPDATE_USER("update_user"),
    /**
     * 删除用户
     */
    DELETE_USER("delete_user"),
    /**
     * 创建部门
     */
    CREATE_PARTY("create_party"),
    /**
     * 更新部门
     */
    UPDATE_PARTY("update_party"),
    /**
     * 删除部门
     */
    DELETE_PARTY("delete_party"),
    ;

    @Getter
    @Setter
    private String type;

    WeChatCallBackTypeEnum(String type) {
        this.type = type;
    }
}

package com.ruigu.rbox.workflow.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuyimin
 * @date 2020/5/27 21:18
 */
@Data
@NoArgsConstructor
public class UserInfoVO {
    private Integer userId;
    private String nickname;
    private String position;
    private String avatar;

    public UserInfoVO(Integer userId, String nickname, String position, String avatar) {
        this.userId = userId;
        this.nickname = nickname;
        this.position = position;
        this.avatar = avatar;
    }
}

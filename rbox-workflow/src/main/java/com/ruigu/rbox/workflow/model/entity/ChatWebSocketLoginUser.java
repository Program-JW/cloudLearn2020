package com.ruigu.rbox.workflow.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author liqingtian
 * @date 2020/01/09 17:25
 */
@Data
@Component
@NoArgsConstructor
public class ChatWebSocketLoginUser {

    /**
     * 登陆人用户名
     */
    private volatile String loginUserName;

    public void clear() {
        this.loginUserName = null;
    }
}

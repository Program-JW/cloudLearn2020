package com.ruigu.rbox.workflow.controller;


import com.ruigu.rbox.cloud.kanai.security.UserHelper;

/**
 * @author alan.zhao
 */
public class BaseController {
    protected long userId() {
        return UserHelper.getUserId() != null ? UserHelper.getUserId() : 0;
    }
}

package com.ruigu.rbox.workflow.model.request;

import lombok.Data;

/**
 * 微信解密用户信息请求
 * @author jianghuilin
 */
@Data
public class DecodeUserInfoRequest {
    private String encryptedData;
    private String iv;
    private String code;
}

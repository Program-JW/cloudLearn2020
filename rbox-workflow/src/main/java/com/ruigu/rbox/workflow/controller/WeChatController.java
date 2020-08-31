package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.workflow.config.AesCbcUtil;
import com.ruigu.rbox.workflow.config.HttpRequest;
import com.ruigu.rbox.workflow.model.request.DecodeUserInfoRequest;
import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信接口
 * @author alan.zhao
 */
@RequestMapping(value = "/wx")
@Controller
public class WeChatController {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping(value = "/decodeUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map decodeUserInfo(@RequestBody DecodeUserInfoRequest request) {
        String encryptedData;
        String iv;
        String code;
        Map map = new HashMap(16);

        // 登录凭证不能为空
        if (request.getCode() == null || request.getCode().length() == 0) {
            map.put("status", 0);
            map.put("msg", "code 不能为空");
            return map;
        }
        encryptedData = request.getEncryptedData();
        iv = request.getIv();
        code = request.getCode();
        // 小程序唯一标识 (在微信小程序管理后台获取)
        String wxspAppid = "wxeb5ecb7ffa8307a0";
        // 小程序的 app secret (在微信小程序管理后台获取)
        String wxspSecret = "d5119dbc2dd1417c8141d34d1e19cc01";
        // 授权（必填）
        String grantType = "authorization_code";

        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
        //////////////// ////////////////
        // 请求参数
        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type="
                + grantType;
        // 发送请求
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        // 解析相应内容（转换成json对象）
        JSONObject json = new JSONObject(sr);
        // 获取会话密钥（session_key）
        String sessionKey = json.get("session_key").toString();
        // 用户的唯一标识（openid）
        String openid = (String) json.get("openid");

        //////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
        try {
            String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
            if (null != result && result.length() > 0) {
                map.put("status", 1);
                map.put("msg", "解密成功");

                JSONObject userInfoJson = new JSONObject(result);
                Map userInfo = new HashMap(16);
                userInfo.put("openId", userInfoJson.get("openId"));
                userInfo.put("nickName", userInfoJson.get("nickName"));
                userInfo.put("gender", userInfoJson.get("gender"));
                userInfo.put("city", userInfoJson.get("city"));
                userInfo.put("province", userInfoJson.get("province"));
                userInfo.put("country", userInfoJson.get("country"));
                userInfo.put("avatarUrl", userInfoJson.get("avatarUrl"));
                userInfo.put("openId", userInfoJson.get("openId"));
                map.put("userInfo", userInfo);
            } else {
                map.put("status", 0);
                map.put("msg", "解密失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}

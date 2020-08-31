package com.ruigu.rbox.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * 封装返回值
 *
 * @author alan.zhao
 * @date 2019/4/22 13:49
 */
@ApiModel(value = "响应结果")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerResponse<T> implements Serializable {

    @ApiModelProperty(value = "响应码")
    @Getter
    private int code;

    @ApiModelProperty(value = "错误详情")
    private String message;

    @ApiModelProperty(value = "错误详情")
    private String msg;

    @ApiModelProperty(value = "数据")
    @Getter
    private T data;

    private ServerResponse() {
    }

    private ServerResponse(int code) {
        this.code = code;
    }

    private ServerResponse(ResponseCode code) {
        this.code = code.getCode();
        this.message = code.getDesc();
    }

    private ServerResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    private ServerResponse(int code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    private ServerResponse(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public String getMessage() {
        return message != null ? message : msg;
    }

    public String getMsg() {
        return msg != null ? msg : message;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == ResponseCode.SUCCESS.getCode();
    }


    public static <T> ServerResponse<T> ok() {
        return new ServerResponse<>(ResponseCode.SUCCESS);
    }

    public static <T> ServerResponse<T> ok(T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> ok(String msg, T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }


    public static <T> ServerResponse<T> fail() {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> fail(ResponseCode code) {
        return new ServerResponse<>(code);
    }


    public static <T> ServerResponse<T> fail(String errorMessage) {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServerResponse<T> fail(int errorCode, String errorMessage) {
        return new ServerResponse<>(errorCode, errorMessage);
    }

    public static <T> ServerResponse<T> build(int code, T data, String message) {
        return new ServerResponse<>(code, message, data);
    }
}
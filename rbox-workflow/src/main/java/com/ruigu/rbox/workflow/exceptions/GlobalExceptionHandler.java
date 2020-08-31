package com.ruigu.rbox.workflow.exceptions;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ActivitiException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * Spring Web 全局异常处理
 *
 * @author alan.zhao
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ServerResponse globalRuntimeException(HttpServletRequest request, Exception e) {
        log.error("{} 请求异常", request.getRequestURL().toString(), e);
        return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), "");
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ServerResponse httpMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        return ServerResponse.fail(ResponseCode.METHOD_NOT_SUPPOTED.getCode(), "");
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public ServerResponse missingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException e) {
        log.error("{} 请求异常,参数缺失", request.getRequestURL().toString(), e);
        return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), "缺少必要参数" + e.getParameterName());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public ServerResponse requestParameterException(HttpServletRequest request, HttpMessageNotReadableException e) {
        log.error("{} 请求参数转换异常", request.getRequestURL().toString(), e);
        return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), "参数转换失败");
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public ServerResponse requestParameterException(HttpServletRequest request, HttpMediaTypeNotSupportedException e) {
        log.error("{} 请求参数转换异常", request.getRequestURL().toString(), e);
        return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), "不支持此请求类型" + e.getContentType());
    }

    @ExceptionHandler(value = VerificationFailedException.class)
    @ResponseBody
    public ServerResponse verificationFailedException(HttpServletRequest request, VerificationFailedException e) {
        log.error("{} 验证不通过", request.getRequestURL().toString(), e);
        return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public ServerResponse mvcVerificationFailedException(HttpServletRequest request, ConstraintViolationException e) {
        log.error("{} 验证不通过", request.getRequestURL().toString(), e);
        StringBuilder errMsg = new StringBuilder("异常：");
        e.getConstraintViolations().forEach(err -> {
            errMsg.append(err.getMessage() + " ");
        });
        return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), errMsg.toString());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ServerResponse methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.error("{} 验证不通过", request.getRequestURL().toString(), e);
        StringBuilder errMsg = new StringBuilder("异常：");
        e.getBindingResult().getAllErrors().forEach(err -> {
            errMsg.append(err.getDefaultMessage() + " ");
        });
        return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), errMsg.toString());
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ServerResponse bindException(HttpServletRequest request, BindException e) {
        log.error("{} 验证不通过", request.getRequestURL().toString(), e);
        StringBuilder errMsg = new StringBuilder("异常：");
        e.getBindingResult().getAllErrors().forEach(err -> {
            errMsg.append(err.getDefaultMessage() + " ");
        });
        return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), errMsg.toString());
    }

    @ExceptionHandler(value = ActivitiException.class)
    @ResponseBody
    public ServerResponse activitiException(HttpServletRequest request, ActivitiException e) {
        log.error("{} 验证不通过", request.getRequestURL().toString(), e);
        return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = LogicException.class)
    @ResponseBody
    public ServerResponse logicException(HttpServletRequest request, LogicException e) {
        log.error("{} 输入参数错误", request.getRequestURL().toString(), e);
        return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = GlobalRuntimeException.class)
    @ResponseBody
    public ServerResponse globalException(HttpServletRequest request, GlobalRuntimeException e) {
        return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), e.getMessage());
    }
}

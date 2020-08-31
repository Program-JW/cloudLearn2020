package com.ruigu.rbox.workflow.interceptor;

import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.workflow.supports.SecurityUtil;
import com.ruigu.rbox.workflow.supports.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author alan.zhao
 */
@Component
public class SecureInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SecureInterceptor.class);

    public SecureInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (UserHelper.getUserId() != null) {
            SpringUtil.getBean(SecurityUtil.class).logInAs(UserHelper.getUserId().toString());
        } else {
            SpringUtil.getBean(SecurityUtil.class).logInAs("0");
        }
        String username = StringUtils.isNotEmpty(UserHelper.getUsername()) ? UserHelper.getUsername() : "";
        logger.info("{}[{}] visit {}", username, UserHelper.getUserId(), request.getRequestURL());
        return true;
    }

}
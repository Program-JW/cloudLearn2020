package com.ruigu.rbox.workflow.supports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author alan.zhao
 */
@Component
public class SecurityUtil {

    private Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    private static final Authentication ANONYMOUS = new AnonymousAuthenticationToken("key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ACTIVITI_USER", "ROLE_ACTIVITI_ADMIN"));

    public void logInAs(String userId) {
        logger.info("> Logged in as: " + userId);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(ANONYMOUS);
        org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(userId);
    }
}

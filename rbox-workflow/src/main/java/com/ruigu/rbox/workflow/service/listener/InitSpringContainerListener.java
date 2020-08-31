package com.ruigu.rbox.workflow.service.listener;

import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.manager.RedisInitManager;
import com.ruigu.rbox.workflow.manager.UserManager;
import com.ruigu.rbox.workflow.model.dto.UserSearchRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Spring容器初始化完成之后,执行类
 *
 * @author liqingtian
 * @date 2020/01/09 17:11
 */
@Slf4j
@Component
public class InitSpringContainerListener implements CommandLineRunner {


    @Resource
    private RedisInitManager redisInitManager;

    @Resource
    private PassportFeignManager passportFeignManager;

    @Resource
    private UserManager userManager;

    @Override
    public void run(String... args) throws Exception {
        try {

            // 1. 用户信息初始化
            log.info(" ======================  检验用户信息是否初始化  ====================== ");
            if (!redisInitManager.checkPassportUserInit()) {
                log.info(" ======================  未初始化信息 ： 准备初始化用户信息  ====================== ");
                // 获取用户信息
                redisInitManager.initPassportUserInfo(passportFeignManager.getAllUserInfo());
                log.info(" ======================  初始化用户信息成功  ====================== ");
            }

            // 2 用户信息初始化后初始化人员部门信息
            log.info(" ======================  检验用户-部门信息是否初始化  ====================== ");
            if (!redisInitManager.checkUserGroupInit()) {
                log.info(" ======================  未初始化信息 ： 准备初始化用户-部门信息  ====================== ");
                // 获取用户id信息
                List<Integer> userIds = passportFeignManager.getAllRedisUserIds();
                if (CollectionUtils.isEmpty(userIds)) {
                    return;
                }
                log.info(" ======================  查询用户-部门信息  ====================== ");
                // 获取用户部门信息
                UserSearchRequestDTO request = new UserSearchRequestDTO();
                request.setUserIdList(userIds);
                redisInitManager.initUserGroupInfo(userManager.searchUserGroupFromApi(request));
                log.info(" ======================  初始化用户信息成功  ====================== ");
            }

        } catch (Exception e) {
            log.error("| -- 初始化异常，异常信息 - e : {} ", e);
        }
    }
}

package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.constants.RedisKeyConstants;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.manager.RedisInitManager;
import com.ruigu.rbox.workflow.manager.UserManager;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.dto.UserGroupSimpleDTO;
import com.ruigu.rbox.workflow.model.dto.UserSearchRequestDTO;
import com.ruigu.rbox.workflow.service.CacheManageService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2020/08/07 10:29
 */
@Service
public class CacheManageServiceImpl implements CacheManageService {

    @Resource
    private RedisInitManager redisInitManager;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private PassportFeignManager passportFeignManager;

    @Resource
    private UserManager userManager;

    @Override
    public void initUserInfo() {
        redisInitManager.initPassportUserInfo(passportFeignManager.getAllUserInfo());
    }

    @Override
    public void initUserGroupInfo() {
        List<Integer> userIds = passportFeignManager.getAllRedisUserIds();
        UserSearchRequestDTO request = new UserSearchRequestDTO();
        request.setUserIdList(userIds);
        redisInitManager.initUserGroupInfo(userManager.searchUserGroupFromApi(request));
    }

    @Override
    public Collection<PassportUserInfoDTO> queryCacheUserInfo(Collection<Integer> userIds) {
        Map<Integer, PassportUserInfoDTO> map = redisInitManager.batchGet(userIds, RedisKeyConstants.PASSPORT_USER_INFO, Integer.class, PassportUserInfoDTO.class);
        return map.values();
    }

    @Override
    public Collection<UserGroupSimpleDTO> queryCacheUserAndGroupInfo(Collection<Integer> userIds) {
        Map<Integer, UserGroupSimpleDTO> map = redisInitManager.batchGet(userIds, RedisKeyConstants.PASSPORT_USER_GROUP_INFO, Integer.class, UserGroupSimpleDTO.class);
        return map.values();
    }

    @Override
    public void removeUserInfo(Collection<Integer> userIds) {
        redisInitManager.removeKeys(convertKeyList(RedisKeyConstants.PASSPORT_USER_INFO, userIds));
    }

    @Override
    public void removeUserAndGroupInfo(Collection<Integer> userIds) {
        redisInitManager.removeKeys(convertKeyList(RedisKeyConstants.PASSPORT_USER_GROUP_INFO, userIds));
    }

    private List<String> convertKeyList(String prefix, Collection<Integer> userIds) {
        List<String> keys = new ArrayList<>();
        userIds.forEach(id -> {
            keys.add(prefix + id);
        });
        return keys;
    }
}

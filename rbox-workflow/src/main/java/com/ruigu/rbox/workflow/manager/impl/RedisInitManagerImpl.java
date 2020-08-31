package com.ruigu.rbox.workflow.manager.impl;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.constants.RedisKeyConstants;
import com.ruigu.rbox.workflow.manager.RedisInitManager;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.dto.UserGroupSimpleDTO;
import com.ruigu.rbox.workflow.model.dto.UserInfoLuaDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author liqingtian
 * @date 2020/04/25 11:18
 */
@Slf4j
@Service
public class RedisInitManagerImpl implements RedisInitManager {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private DefaultRedisScript<Void> batchSetUser;

    @Resource
    private DefaultRedisScript<String> batchGet;

    private final String INIT_ENABLE = "1";

    @Override
    public Boolean checkPassportUserInit() {
        try {
            String result = stringRedisTemplate.opsForValue().get(RedisKeyConstants.PASSPORT_USER_CACHE_ENABLE);
            return INIT_ENABLE.equals(result);
        } catch (Exception e) {
            log.error(" 检验用户信息redis是否初始化异常 - e : {} ", e);
        }
        return false;
    }

    @Override
    public boolean checkUserGroupInit() {
        try {
            String result = stringRedisTemplate.opsForValue().get(RedisKeyConstants.PASSPORT_USER_GROUP_CACHE_ENABLE);
            return INIT_ENABLE.equals(result);
        } catch (Exception e) {
            log.error(" 检验用户信息redis是否初始化异常 - e : {} ", e);
        }
        return false;
    }

    @Override
    public void initPassportUserInfo(List<UserInfoLuaDTO> userInfoList) {
        try {
            stringRedisTemplate.execute(batchSetUser, Arrays.asList(RedisKeyConstants.PASSPORT_USER_INFO, JsonUtil.toJsonString(userInfoList)));
            stringRedisTemplate.opsForValue().set(RedisKeyConstants.PASSPORT_USER_CACHE_ENABLE, INIT_ENABLE);
        } catch (Exception e) {
            log.error(" 用户信息Redis缓存初始化异常 - e : {} ", e);
        }
    }

    @Override
    public void initUserGroupInfo(Map<Integer, UserGroupSimpleDTO> userGroupInfo) {
        RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
        try {
            // 尝试使用管道批量初始化
            stringRedisTemplate.executePipelined(new RedisCallback<String>() {
                @Override
                public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    // 存入信息
                    userGroupInfo.forEach((id, info) -> {
                        String key = RedisKeyConstants.PASSPORT_USER_GROUP_INFO + id;
                        // 生成随机数 (防止大面积雪崩)
                        long random = ThreadLocalRandom.current().nextLong(10L);
                        redisConnection.set(
                                Objects.requireNonNull(serializer.serialize(key)),
                                Objects.requireNonNull(serializer.serialize(JsonUtil.toJsonString(info))),
                                Expiration.from(15 + random, TimeUnit.DAYS),
                                RedisStringCommands.SetOption.UPSERT);
                    });
                    // 设置标志位
                    redisConnection.set(
                            Objects.requireNonNull(serializer.serialize(RedisKeyConstants.PASSPORT_USER_GROUP_CACHE_ENABLE)),
                            Objects.requireNonNull(serializer.serialize(INIT_ENABLE)),
                            Expiration.from(30, TimeUnit.DAYS),
                            RedisStringCommands.SetOption.UPSERT
                    );
                    return null;
                }
            });
        } catch (Exception e) {
            log.error(" 用户-部门信息Redis缓存初始化异常 - e : {} ", e);
        }
    }

    @Async
    @Override
    public void asyncInitPassportUserInfo(List<PassportUserInfoDTO> userList) {
        List<UserInfoLuaDTO> luaInfoList = new ArrayList<>();
        userList.forEach(u -> {
            UserInfoLuaDTO luaInfo = new UserInfoLuaDTO();
            luaInfo.setUserId(u.getId());
            luaInfo.setInfo(JsonUtil.toJsonString(u));
            luaInfoList.add(luaInfo);
        });
        initPassportUserInfo(luaInfoList);
    }

    @Async
    @Override
    public void asyncInitUserGROUPInfo(Map<Integer, UserGroupSimpleDTO> data) {
        initUserGroupInfo(data);
    }


//    @Override
//    public Map<Integer, PassportUserInfoDTO> batchGetPassportUserInfo(Collection<Integer> ids) {
//        try {
//            String execute = stringRedisTemplate.execute(batchGet, Arrays.asList(RedisKeyConstants.PASSPORT_USER_INFO, JsonUtil.toJsonString(ids)));
//            if (StringUtils.isNotBlank(execute)) {
//                return JsonUtil.parseMap(execute, Integer.class, PassportUserInfoDTO.class);
//            }
//        } catch (Exception e) {
//            log.error(" 批量获取redis缓存用户信息异常 - e : {} ", e);
//        }
//        return new HashMap<>(4);
//    }

    @Override
    public <K, V> Map<K, V> batchGet(Collection<Integer> ids, String prefix, Class<K> keyClazz, Class<V> valueClazz) {
        try {
            String execute = stringRedisTemplate.execute(batchGet, Arrays.asList(prefix, JsonUtil.toJsonString(ids)));
            if (StringUtils.isNotBlank(execute)) {
                return JsonUtil.parseMap(execute, keyClazz, valueClazz);
            }
        } catch (Exception e) {
            log.error(" 批量获取redis缓存用户信息异常 - e : {} ", e);
        }
        return Collections.emptyMap();
    }

    @Override
    public void updatePassportInfo(PassportUserInfoDTO userInfo) {
        try {
            // 过期时间 ： 一个月
            stringRedisTemplate.opsForValue().set(RedisKeyConstants.PASSPORT_USER_INFO + userInfo.getId(),
                    JsonUtil.toJsonString(userInfo),
                    Duration.between(LocalDateTime.now(), LocalDateTime.now().plusMonths(1)));
        } catch (Exception e) {
            log.error(" 更新用户信息redis缓存异常  -  e : {} ", e);
        }
    }

    @Override
    public void removeUserGroupInfo(Integer userId) {
        stringRedisTemplate.delete(RedisKeyConstants.PASSPORT_USER_GROUP_INFO + userId);
    }

    @Override
    public void removeKeys(Collection<String> keys) {
        stringRedisTemplate.delete(keys);
    }
}

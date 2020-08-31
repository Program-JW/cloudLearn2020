package com.ruigu.rbox.workflow.manager.impl;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruigu.rbox.workflow.constants.RedisKeyConstants;
import com.ruigu.rbox.workflow.manager.LightningIssueManager;
import com.ruigu.rbox.workflow.model.entity.QLightningIssueApplyEntity;
import com.ruigu.rbox.workflow.model.entity.QLightningIssueLogEntity;
import com.ruigu.rbox.workflow.model.enums.LightningApplyStatus;
import com.ruigu.rbox.workflow.model.enums.LightningIssueLogActionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liqingtian
 * @date 2020/04/25 15:23
 */
@Slf4j
@Service
public class LightningIssueManagerImpl implements LightningIssueManager {

    @Resource
    private JPAQueryFactory queryFactory;

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Async
    @Override
    public void asynInitMyAcceptanceIds(Integer userId) {

        // 查询我受理列表
        List<Integer> issueIdList = queryMyAcceptanceIdList(userId);

        // 如果不存在则设置默认值 （防止缓存击穿）
        if (issueIdList.isEmpty()) {
            issueIdList.add(-1);
        }

        // 设置
        redisTemplate.opsForSet().add(RedisKeyConstants.MY_ACCEPTANCE_ISSUE_ID_SET + userId, issueIdList.toArray(new Integer[0]));
    }

    @Async
    @Override
    public void addIssueId(List<Integer> issueIdList, Integer userId) {
        String key = RedisKeyConstants.MY_ACCEPTANCE_ISSUE_ID_SET + userId;
        Boolean result = redisTemplate.hasKey(key);
        if (result != null && !result) {
            List<Integer> queryIdList = queryMyAcceptanceIdList(userId);
            issueIdList.addAll(queryIdList);
        }
        Integer[] idArray = issueIdList.toArray(new Integer[0]);
        redisTemplate.opsForSet().add(key, idArray);
    }

    @Override
    public void addIssueId(Integer issueId) {

        // 查询问题所有受理人
        List<Integer> userIdList = queryUserIdByIssueId(issueId);

        // 同步redis
        userIdList.forEach(id -> {
            String key = RedisKeyConstants.MY_ACCEPTANCE_ISSUE_ID_SET + id;
            Boolean result = redisTemplate.hasKey(key);
            if (result == null || !result) {
                List<Integer> issueIdList = queryMyAcceptanceIdList(id);
                Integer[] idArray = issueIdList.toArray(new Integer[0]);
                redisTemplate.opsForSet().add(key, idArray);
            }
            redisTemplate.opsForSet().add(key, issueId);
        });
    }

    @Async
    @Override
    public void addIssueId(Integer issueId, Integer userId) {
        String key = RedisKeyConstants.MY_ACCEPTANCE_ISSUE_ID_SET + userId;
        Boolean result = redisTemplate.hasKey(key);
        Set<Integer> issueIdList = new HashSet<>(16);
        issueIdList.add(issueId);
        if (result != null && !result) {
            List<Integer> queryIdList = queryMyAcceptanceIdList(userId);
            issueIdList.addAll(queryIdList);
        }
        Integer[] idArray = issueIdList.toArray(new Integer[0]);
        redisTemplate.opsForSet().add(key, idArray);
    }

    @Async
    @Override
    public void removeIssueId(Integer issueId) {
        // 查询问题所有受理人
        List<Integer> userIdList = queryUserIdByIssueId(issueId);
        // 同步redis
        userIdList.forEach(id -> {
            String key = RedisKeyConstants.MY_ACCEPTANCE_ISSUE_ID_SET + id;
            Boolean result = redisTemplate.hasKey(key);
            if (result == null || !result) {
                List<Integer> issueIdList = queryMyAcceptanceIdList(id);
                Integer[] idArray = issueIdList.toArray(new Integer[0]);
                redisTemplate.opsForSet().add(key, idArray);
            }
            redisTemplate.opsForSet().remove(key, issueId);
        });
    }

    @Override
    public Set<Integer> checkMyAcceptanceIssueInfoInit(Integer userId) {
        String key = RedisKeyConstants.MY_ACCEPTANCE_ISSUE_ID_SET + userId;
        Boolean result = redisTemplate.hasKey(key);
        if (result != null && result) {
            return redisTemplate.opsForSet().members(key);
        }
        return null;
    }

    @Override
    public void clearRedisCache(List<Integer> userIds) {
        List<String> keys = new ArrayList<>();
        userIds.forEach(id -> keys.add(RedisKeyConstants.MY_ACCEPTANCE_ISSUE_ID_SET + id));
        redisTemplate.delete(keys);
    }

    @Override
    public Set<Integer> queryRedisMyAcceptCache(Integer userId) {
        return redisTemplate.opsForSet().members(RedisKeyConstants.MY_ACCEPTANCE_ISSUE_ID_SET + userId);
    }

    private List<Integer> queryMyAcceptanceIdList(Integer userId) {

        QLightningIssueApplyEntity qApplyEntity = QLightningIssueApplyEntity.lightningIssueApplyEntity;
        QLightningIssueLogEntity qLogEntity = QLightningIssueLogEntity.lightningIssueLogEntity;

        // 查询我受理列表
        List<Integer> issueIdList = queryFactory.select(qApplyEntity.id)
                .from(qApplyEntity)
                .where(
                        qApplyEntity.status.notIn(LightningApplyStatus.RESOLVED.getCode(), LightningApplyStatus.REVOKED.getCode())
                                .and(qApplyEntity.id.in(
                                        JPAExpressions.selectDistinct(qLogEntity.issueId)
                                                .from(qLogEntity)
                                                .where(
                                                        qLogEntity.action.in(
                                                                LightningIssueLogActionEnum.TO_BE_ACCEPTED.getCode(),
                                                                LightningIssueLogActionEnum.LEADER_ADD.getCode(),
                                                                LightningIssueLogActionEnum.INVITE_HANDED_OVER.getCode()
                                                        )
                                                                .and(qLogEntity.createdBy.eq(userId))
                                                )
                                        )
                                )
                )
                .fetch();

        if (issueIdList == null) {
            return new ArrayList<>();
        }

        return issueIdList;
    }

    private List<Integer> queryUserIdByIssueId(Integer issueId) {
        QLightningIssueLogEntity qLogEntity = QLightningIssueLogEntity.lightningIssueLogEntity;
        List<Integer> userIdList = queryFactory.selectDistinct(qLogEntity.createdBy)
                .from(qLogEntity)
                .where(
                        qLogEntity.issueId.eq(issueId)
                                .and(qLogEntity.action.eq(LightningIssueLogActionEnum.TO_BE_ACCEPTED.getCode()))
                ).fetch();

        if (userIdList == null) {
            return new ArrayList<>();
        }
        return userIdList;
    }
}

package com.ruigu.rbox.workflow.manager;

import java.util.List;
import java.util.Set;

/**
 * @author liqingtian
 * @date 2020/04/25 15:23
 */
public interface LightningIssueManager {

    /**
     * 异步初始化我受理列表
     *
     * @param userId 用户id
     */
    void asynInitMyAcceptanceIds(Integer userId);

    /**
     * 加入问题id (redis)
     *
     * @param issueIdList 问题id列表
     * @param userId      用户id
     */
    void addIssueId(List<Integer> issueIdList, Integer userId);

    /**
     * 加入问题id (redis)
     *
     * @param issueId 问题id
     */
    void addIssueId(Integer issueId);

    /**
     * 加入问题id (redis)
     *
     * @param issueId 问题id列表
     * @param userId  用户id
     */
    void addIssueId(Integer issueId, Integer userId);

    /**
     * 移除问题
     *
     * @param issueId 问题id
     */
    void removeIssueId(Integer issueId);

    /**
     * 检验我受理问题是否初始化信息
     *
     * @param userId 用户id
     * @return 我受理列表id
     */
    Set<Integer> checkMyAcceptanceIssueInfoInit(Integer userId);

    /**
     * 清除用户缓存
     *
     * @param userIds 用户id
     */
    void clearRedisCache(List<Integer> userIds);

    /**
     * 查询redis缓存
     *
     * @param userId 用户id
     * @return redis缓存我受理列表
     */
    Set<Integer> queryRedisMyAcceptCache(Integer userId);
}

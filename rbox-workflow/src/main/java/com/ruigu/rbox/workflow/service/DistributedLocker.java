package com.ruigu.rbox.workflow.service;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 * @author alan.zhao
 */
public interface DistributedLocker {

    /**
     * 获取锁
     * @param lockKey 锁标识符
     */
    void lock(String lockKey);

    /**
     * 释放取锁
     * @param lockKey 锁标识符
     */
    void unlock(String lockKey);

    /**
     * 获取锁
     * @param lockKey 锁标识符
     * @param timeout 超时时间
     */
    void lock(String lockKey, int timeout);

    /**
     * 获取锁
     * @param lockKey 锁标识符
     * @param unit    超时时间单位
     * @param timeout 超时时间
     */
    void lock(String lockKey, TimeUnit unit, int timeout);

}


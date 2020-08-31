package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.service.DistributedLocker;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * redisson 分布式锁实现
 * @author alan.zhao
 */
public class RedissonDistributedLocker implements DistributedLocker {

    private RedissonClient redissonClient;

    @Override
    public void lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    @Override
    public void lock(String lockKey, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
    }

    @Override
    public void lock(String lockKey, TimeUnit unit ,int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}


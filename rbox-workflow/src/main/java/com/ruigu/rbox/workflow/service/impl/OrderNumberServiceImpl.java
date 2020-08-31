package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.cloud.kanai.model.ResponseCode;
import com.ruigu.rbox.cloud.kanai.util.TimeUtil;
import com.ruigu.rbox.cloud.kanai.web.exception.GlobalRuntimeException;
import com.ruigu.rbox.workflow.constants.RedisKeyConstants;
import com.ruigu.rbox.workflow.service.OrderNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author liqingtian
 * @date 2020/05/22 15:06
 */
@Slf4j
@Service
public class OrderNumberServiceImpl implements OrderNumberService {

    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    @Override
    public String createSasApplyOrderNumber() {
        // 单号字符串
        StringBuilder orderNumber = new StringBuilder();
        // 时间字符串
        String todayString = TimeUtil.format(new Date(), "yyyyMMdd");
        orderNumber.append(todayString);
        // key
        String key = String.format(RedisKeyConstants.SAS_APPLY_COUNT, todayString);
        try {
            // 数量
            Long increment = redisTemplate.opsForValue().increment(key);
            // 设置过期时间
            redisTemplate.expire(key, 1, TimeUnit.DAYS);
            // 补0
            orderNumber.append(String.format("%05d", increment));
            // 返回
            return orderNumber.toString();
        } catch (Exception e) {
            log.error(" 获取单号，操作redis失败", e);
            throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), "获取审批单号失败");
        }
    }
}

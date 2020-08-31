package com.ruigu.rbox.workflow.manager.impl;

import com.ruigu.rbox.cloud.kanai.util.TimeUtil;
import com.ruigu.rbox.workflow.constants.RedisKeyConstants;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.manager.LightningIssueConfigManager;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.model.dto.DutyUserByDayDTO;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.entity.DutyPlanEntity;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.repository.DutyPlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author liqingtian
 * @date 2020/05/13 10:04
 */
@Slf4j
@Service
public class LightningIssueConfigManagerImpl implements LightningIssueConfigManager {

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Resource
    private DutyPlanRepository dutyPlanRepository;

    @Resource
    private PassportFeignManager passportFeignManager;

    @Override
    public Integer distributionDuty(Integer categoryId) {
        String queue = RedisKeyConstants.ISSUE_CATEGORY_DUTY_POLL + categoryId;
        return redisTemplate.opsForList().rightPopAndLeftPush(queue, queue);
    }

    @Override
    public void initDutyPollUser(Integer categoryId, List<Integer> userIds) {
        redisTemplate.opsForList().leftPushAll(RedisKeyConstants.ISSUE_CATEGORY_DUTY_POLL + categoryId, userIds);
    }

    @Override
    public void initDutyWeekUser(Integer categoryId, List<Integer> userIds) {
        String key = RedisKeyConstants.ISSUE_CATEGORY_DUTY_POLL + categoryId;
        redisTemplate.opsForList().leftPushAll(key, userIds);
        redisTemplate.expire(key,
                Duration.between(LocalDateTime.now(), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)).toMillis(),
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void removeAndUpdateDutyPollUser(Integer categoryId, List<Integer> userIds) {
        String key = RedisKeyConstants.ISSUE_CATEGORY_DUTY_POLL + categoryId;
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.multi();
        redisTemplate.delete(key);
        redisTemplate.opsForList().leftPushAll(key, userIds);
        redisTemplate.exec();
    }

    @Override
    public void removeAndUpdateDutyWeekUser(Integer categoryId, List<Integer> userIds) {

        String key = RedisKeyConstants.ISSUE_CATEGORY_DUTY_POLL + categoryId;
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.multi();
        redisTemplate.delete(key);
        redisTemplate.opsForList().leftPushAll(key, userIds);
        redisTemplate.expire(key,
                Duration.between(LocalDateTime.now(), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)).toMillis(),
                TimeUnit.MILLISECONDS);
        redisTemplate.exec();

    }

    @Override
    public void removeDutyPollUser(Integer categoryId) {
        redisTemplate.delete(RedisKeyConstants.ISSUE_CATEGORY_DUTY_POLL + categoryId);
    }

    @Override
    public void removeDutyByDayUser(Integer categoryId) {
        String key = String.format(RedisKeyConstants.ISSUE_CATEGORY_DUTY_BY_DAY, TimeUtil.format(new Date(), TimeUtil.FORMAT_DATE)) + categoryId;
        redisTemplate.delete(key);
    }

    @Override
    public void updateDutyByDayUser(Integer categoryId, Integer ruleId) {
        DutyPlanEntity todayPlan = dutyPlanRepository.findByEnableRuleIdAndDutyDate(ruleId, LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        if (todayPlan == null) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "数据异常，未查询到当天值班人数据");
        }
        String key = String.format(RedisKeyConstants.ISSUE_CATEGORY_DUTY_BY_DAY, TimeUtil.format(new Date(), TimeUtil.FORMAT_DATE));
        redisTemplate.opsForValue().set(key + categoryId, todayPlan.getPersonId(),
                Duration.between(LocalDateTime.now(), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));
    }

    @Override
    public void replaceDutyByDayUser(Integer categoryId, Integer userId) {
        String key = String.format(RedisKeyConstants.ISSUE_CATEGORY_DUTY_BY_DAY, TimeUtil.format(new Date(), TimeUtil.FORMAT_DATE)) + categoryId;
        // 设置过期时间为当天晚上最后1s
        redisTemplate.opsForValue().set(key, userId,
                Duration.between(LocalDateTime.now(), LocalDateTime.of(LocalDate.now(), LocalTime.MAX)));
    }

    @Override
    public Page<DutyUserByDayDTO> queryPageDutyUserByDay(Integer ruleId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page < 0 ? 0 : page, size <= 0 ? 20 : size);
        Page<DutyPlanEntity> planInfoPage = dutyPlanRepository.findAllByRuleIdOrderByDutyDateDesc(ruleId, pageable);
        List<DutyUserByDayDTO> dutyUserList = new ArrayList<>();
        List<Integer> userIds = planInfoPage.getContent().stream().map(DutyPlanEntity::getPersonId).collect(Collectors.toList());
        Map<Integer, PassportUserInfoDTO> userInfoMap = passportFeignManager.getUserInfoMapFromRedis(userIds);
        planInfoPage.getContent().forEach(d -> {
            DutyUserByDayDTO user = new DutyUserByDayDTO();
            user.setPlanId(d.getId());
            Integer personId = d.getPersonId();
            user.setUserId(personId);
            PassportUserInfoDTO userInfo = userInfoMap.get(personId);
            if (userInfo != null) {
                user.setName(userInfo.getNickname());
            }
            user.setDutyDate(d.getDutyDate().toLocalDate());
            user.setModifiable(false);
            dutyUserList.add(user);
        });
        return new PageImpl<>(dutyUserList, pageable, planInfoPage.getTotalElements());
    }

    @Override
    public Integer queryTodayDutyByDayUser(Integer categoryId) {
        String keyPrefix = String.format(RedisKeyConstants.ISSUE_CATEGORY_DUTY_BY_DAY, TimeUtil.format(new Date(), TimeUtil.FORMAT_DATE));
        return redisTemplate.opsForValue().get(keyPrefix + categoryId);
    }

    @Override
    public List<Integer> queryDutyPollUser(Integer categoryId) {
        return redisTemplate.opsForList().range(RedisKeyConstants.ISSUE_CATEGORY_DUTY_POLL + categoryId, 0, -1);
    }

    @Override
    public Map<Integer, Integer> queryTodayDutyByDayUser(List<Integer> categoryIds) {
        Map<Integer, Integer> data = new HashMap<>(16);
        if (CollectionUtils.isEmpty(categoryIds)) {
            return data;
        }
        String keyPrefix = String.format(RedisKeyConstants.ISSUE_CATEGORY_DUTY_BY_DAY, TimeUtil.format(new Date(), TimeUtil.FORMAT_DATE));
        categoryIds.forEach(c -> {
            Integer userId = redisTemplate.opsForValue().get(keyPrefix + c);
            if (userId == null) {
                // 查询数据库
                DutyPlanEntity plan = dutyPlanRepository.findByCategoryIdAndDutyDate(c, LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
                if (plan == null || plan.getPersonId() == null) {
                    log.error("========================= 分类 Id : {} 的当日值班人查询失败 ========================", c);
                    // throw new VerificationFailedException(ResponseCode.INTERNAL_ERROR.getCode(), "当日值班人查询失败，请先检查问题分类配置");
                    return;
                }
                userId = plan.getPersonId();
                // 更换值班人
                replaceDutyByDayUser(c, userId);
            }
            data.put(c, userId);
        });
        return data;
    }
}

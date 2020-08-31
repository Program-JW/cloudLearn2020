package com.ruigu.rbox.workflow.strategy.impl;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.manager.LightningIssueConfigManager;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.model.dto.*;
import com.ruigu.rbox.workflow.model.entity.DutyRuleEntity;
import com.ruigu.rbox.workflow.model.entity.DutyWeekPlanEntity;
import com.ruigu.rbox.workflow.model.entity.LightningIssueCategoryEntity;
import com.ruigu.rbox.workflow.model.enums.DutyRuleTypeEnum;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.request.DutyUserRequest;
import com.ruigu.rbox.workflow.model.vo.DutyUserByWeekVO;
import com.ruigu.rbox.workflow.repository.DutyRuleRepository;
import com.ruigu.rbox.workflow.repository.DutyWeekPlanRepository;
import com.ruigu.rbox.workflow.repository.LightningIssueCategoryRepository;
import com.ruigu.rbox.workflow.strategy.DutyConfigHandleStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liqingtian
 * @date 2020/06/04 16:59
 */
@Service
public class DutyByWeekHandleStrategyImpl implements DutyConfigHandleStrategy {

    @Resource
    private DutyWeekPlanRepository dutyWeekPlanRepository;

    @Resource
    private DutyRuleRepository dutyRuleRepository;

    @Resource
    private LightningIssueCategoryRepository lightningIssueCategoryRepository;

    @Resource
    private LightningIssueConfigManager lightningIssueConfigManager;

    @Resource
    private PassportFeignManager passportFeignManager;

    @Override
    public Boolean match(Integer dutyRuleType) {
        return DutyRuleTypeEnum.DUTY_BY_WEEK.getCode().equals(dutyRuleType);
    }

    @Override
    public void save(DutyRuleEntity rule, DutyUserRequest request) {
        dutyWeekPlanRepository.saveAll(convertWeekPlan(rule.getId(), request.getWeekList()));
    }

    @Override
    public void update(DutyRuleEntity oldRule, DutyUserRequest request) {
        List<DutyWeekPlanEntity> dutyWeekPlanList = convertWeekPlan(oldRule.getId(), request.getWeekList());
        dutyWeekPlanRepository.saveAll(dutyWeekPlanList);
        // 更新redis
        final int today = LocalDateTime.now().getDayOfWeek().getValue();
        dutyWeekPlanList.stream().filter(w -> w.getDayOfWeek() == today).findFirst().ifPresent(
                w -> {
                    List<Integer> dutyUser = JsonUtil.parseArray(w.getUserIds(), Integer.class);
                    if (YesOrNoEnum.YES.getCode() == oldRule.getStatus() && CollectionUtils.isNotEmpty(dutyUser)) {
                        List<LightningIssueCategoryEntity> allCategory = lightningIssueCategoryRepository.findAllByRuleIdAndStatus(oldRule.getId(), YesOrNoEnum.YES.getCode());
                        allCategory.forEach(c -> lightningIssueConfigManager.removeAndUpdateDutyWeekUser(c.getId(), dutyUser));
                    }
                }
        );
    }

    @Override
    public List<Integer> queryTodayDutyUser(Integer categoryId) {
        return lightningIssueConfigManager.queryDutyPollUser(categoryId);
    }

    @Override
    public void onRule(DutyRuleEntity rule) {
        Integer ruleId = rule.getId();
        Integer dayOfWeek = LocalDateTime.now().getDayOfWeek().getValue();
        // 按天值班需要校验今天是否有值班人
        DutyWeekPlanEntity todayPlan = dutyWeekPlanRepository.findByRuleIdAndDayOfWeek(ruleId, dayOfWeek);
        if (todayPlan == null || StringUtils.isBlank(todayPlan.getUserIds())) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "启用失败，数据异常，查询不到该策略当天值班人信息");
        }
        List<Integer> userIds = JsonUtil.parseArray(todayPlan.getUserIds(), Integer.class);
        if (CollectionUtils.isEmpty(userIds)) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "启用失败，数据异常，查询不到该策略当天值班人信息");
        }
        dutyRuleRepository.updateStatus(ruleId, YesOrNoEnum.YES.getCode());
    }

    @Override
    public void categoryOnOff(Integer categoryId, DutyRuleEntity rule, boolean on) {
        if (on) {
            if (rule.getStatus() != YesOrNoEnum.YES.getCode()) {
                throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "启用失败，该分类所引用策略为禁用状态");
            }
            Integer dayOfWeek = LocalDateTime.now().getDayOfWeek().getValue();
            DutyWeekPlanEntity weekPlan = dutyWeekPlanRepository.findByRuleIdAndDayOfWeek(rule.getId(), dayOfWeek);
            List<Integer> userIds = JsonUtil.parseArray(weekPlan.getUserIds(), Integer.class);
            if (CollectionUtils.isEmpty(userIds)) {
                throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "启用失败，数据异常，查询不到该分类当天值班人信息");
            }
            lightningIssueConfigManager.initDutyWeekUser(categoryId, userIds);
        } else {
            lightningIssueConfigManager.removeDutyPollUser(categoryId);
        }
    }

    @Override
    public DutyUserDetailDTO queryDutyUser(DutyRuleEntity rule) {

        List<DutyWeekPlanEntity> weekPlan = dutyWeekPlanRepository.findAllByRuleId(rule.getId());

        List<DutyUserByWeekVO> weekDetail = new ArrayList<>();
        weekPlan.forEach(w -> {
            DutyUserByWeekVO week = new DutyUserByWeekVO();
            week.setId(w.getId());
            week.setDayOfWeek(w.getDayOfWeek());
            List<DutyUserByPollDTO> dutyUserList = new ArrayList<>();
            List<Integer> dutyUserId = JsonUtil.parseArray(w.getUserIds(), Integer.class);
            List<PassportUserInfoDTO> userInfoList = passportFeignManager.getUserInfoListFromRedis(dutyUserId);
            userInfoList.forEach(u -> {
                DutyUserByPollDTO user = new DutyUserByPollDTO();
                user.setId(u.getId());
                user.setName(u.getNickname());
                dutyUserList.add(user);
            });
            week.setDutyUserList(dutyUserList);
            weekDetail.add(week);
        });

        DutyUserDetailDTO detail = new DutyUserDetailDTO();
        detail.setWeekList(weekDetail);

        return detail;
    }

    @Override
    public DutyUserListDTO queryDutyUser(DutyRuleEntity rule, Integer page, Integer size) {
        DutyUserListDTO listDTO = new DutyUserListDTO();
        DutyUserDetailDTO dutyUserDetail = queryDutyUser(rule);
        if (dutyUserDetail != null) {
            listDTO.setWeekList(dutyUserDetail.getWeekList());
        }
        return listDTO;
    }

    @Override
    public void updateCache(Integer categoryId, DutyRuleEntity rule) {

        Integer dayOfWeek = LocalDateTime.now().getDayOfWeek().getValue();
        DutyWeekPlanEntity weekPlan = dutyWeekPlanRepository.findByRuleIdAndDayOfWeek(rule.getId(), dayOfWeek);
        if (weekPlan == null || StringUtils.isBlank(weekPlan.getUserIds())) {
            throw new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "");
        }
        List<Integer> userIds = JsonUtil.parseArray(weekPlan.getUserIds(), Integer.class);
        if (CollectionUtils.isEmpty(userIds)) {
            throw new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "");
        }
        lightningIssueConfigManager.removeAndUpdateDutyWeekUser(categoryId, userIds);

    }

    @Override
    public void removeCache(Integer categoryId) {
        lightningIssueConfigManager.removeDutyPollUser(categoryId);
    }

    private List<DutyWeekPlanEntity> convertWeekPlan(Integer ruleId, List<DutyUserByWeekDTO> weekList) {

        List<DutyWeekPlanEntity> dutyWeekPlan = new ArrayList<>(10);
        if (CollectionUtils.isEmpty(weekList)) {
            return dutyWeekPlan;
        }

        Integer userId = UserHelper.getUserId();
        LocalDateTime now = LocalDateTime.now();
        // 校验 week 是否有重复
        Map<Integer, Integer> map = new HashMap<>(16);
        // 将已有数据加入
        List<DutyWeekPlanEntity> allPlans = dutyWeekPlanRepository.findAllByRuleId(ruleId);
        Map<Integer, DutyWeekPlanEntity> existPlanMap = new HashMap<>(16);
        if (CollectionUtils.isNotEmpty(allPlans)) {
            allPlans.forEach(p -> {
                existPlanMap.put(p.getDayOfWeek(), p);
            });
        }
        weekList.forEach(w -> {
            Integer dayOfWeek = w.getDayOfWeek();
            List<Integer> dutyUserIdList = w.getDutyUserIdList();
            if (CollectionUtils.isEmpty(dutyUserIdList)) {
                return;
            }
            Integer flag = map.getOrDefault(dayOfWeek, null);
            if (flag != null) {
                throw new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "值班计划冲突，不可重复");
            }
            DutyWeekPlanEntity plan = existPlanMap.getOrDefault(dayOfWeek, null);
            if (plan == null) {
                plan = new DutyWeekPlanEntity();
            }
            Integer planId = plan.getId() == null ? w.getId() : plan.getId();
            plan.setId(planId);
            plan.setRuleId(ruleId);
            plan.setDayOfWeek(dayOfWeek);
            plan.setUserIds(JsonUtil.toJsonString(dutyUserIdList));
            plan.setCreatedBy(userId);
            plan.setCreatedOn(now);
            plan.setLastUpdatedBy(userId);
            plan.setLastUpdatedOn(now);
            dutyWeekPlan.add(plan);
            map.put(dayOfWeek, YesOrNoEnum.YES.getCode());
        });

        return dutyWeekPlan;
    }
}

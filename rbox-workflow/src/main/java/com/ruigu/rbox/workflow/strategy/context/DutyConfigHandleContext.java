package com.ruigu.rbox.workflow.strategy.context;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.model.dto.DutyUserDetailDTO;
import com.ruigu.rbox.workflow.model.dto.DutyUserListDTO;
import com.ruigu.rbox.workflow.model.entity.DutyPlanEntity;
import com.ruigu.rbox.workflow.model.entity.DutyRuleEntity;
import com.ruigu.rbox.workflow.model.entity.LightningIssueCategoryEntity;
import com.ruigu.rbox.workflow.model.enums.DutyRuleTypeEnum;
import com.ruigu.rbox.workflow.model.request.DutyUserRequest;
import com.ruigu.rbox.workflow.strategy.DutyConfigHandleStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/06/05 1:08
 */
@Service
public class DutyConfigHandleContext {

    @Resource
    private List<DutyConfigHandleStrategy> strategies;

    /**
     * 保存 （策略新增的后续处理，值班人数据的保存处理）
     *
     * @param rule            规则
     * @param dutyUserRequest 值班人员信息
     */
    public void save(DutyRuleEntity rule, DutyUserRequest dutyUserRequest) {

        strategies.stream()
                .filter(s -> s.match(rule.getType()))
                .findFirst()
                .ifPresent(s -> s.save(rule, dutyUserRequest));

    }

    /**
     * 更新 （包括redis更新）
     *
     * @param oldRule         老规则
     * @param dutyUserRequest 值班数据
     */
    public void update(DutyRuleEntity oldRule, DutyUserRequest dutyUserRequest) {

        strategies.stream()
                .filter(s -> s.match(oldRule.getType()))
                .findFirst()
                .ifPresent(s -> s.update(oldRule, dutyUserRequest));
    }

    /**
     * 查询当天值班人
     *
     * @param categoryId 问题分类id
     * @return 值班人
     */
    public List<Integer> queryTodayDutyUser(Integer categoryId, Integer type) {

        List<Integer> dutyUser = new ArrayList<>();

        strategies.stream()
                .filter(s -> s.match(type))
                .findFirst()
                .ifPresent(s -> dutyUser.addAll(s.queryTodayDutyUser(categoryId)));

        return dutyUser;
    }

    /**
     * 启用检验
     *
     * @param rule 规则
     */
    public void onRule(DutyRuleEntity rule) {

        strategies.stream()
                .filter(s -> s.match(rule.getType()))
                .findFirst()
                .ifPresent(s -> s.onRule(rule));

    }

    /**
     * 策略开关
     *
     * @param categoryId 分类id
     * @param rule       规则
     * @param on         true-on  false-off
     */
    public void categoryOnOff(Integer categoryId, DutyRuleEntity rule, boolean on) {

        strategies.stream()
                .filter(s -> s.match(rule.getType()))
                .findFirst()
                .ifPresent(s -> s.categoryOnOff(categoryId, rule, on));

    }

    /**
     * 查询相应值班人数据
     *
     * @param rule 策略
     * @return 值班数据
     */
    public DutyUserDetailDTO queryDutyUser(DutyRuleEntity rule) {

        DutyConfigHandleStrategy strategy = strategies.stream()
                .filter(s -> s.match(rule.getType()))
                .findFirst().orElse(null);
        if (strategy != null) {
            return strategy.queryDutyUser(rule);
        }

        return new DutyUserDetailDTO();
    }

    /**
     * 查询相应值班人数据
     *
     * @param rule 策略
     * @param page 分页页数
     * @param size 分页大小
     * @return 值班数据
     */
    public DutyUserListDTO queryDutyUser(DutyRuleEntity rule, Integer page, Integer size) {

        DutyConfigHandleStrategy strategy = strategies.stream()
                .filter(s -> s.match(rule.getType()))
                .findFirst().orElse(null);
        if (strategy != null) {
            return strategy.queryDutyUser(rule, page, size);
        }

        return new DutyUserListDTO();
    }

    /**
     *
     *
     */
    public void updateCache(Integer categoryId, DutyRuleEntity rule) {

        strategies.stream()
                .filter(s -> s.match(rule.getType()))
                .findFirst()
                .ifPresent(s -> s.updateCache(categoryId, rule));
    }

    public void removeCache(Integer categoryId, Integer type) {

        strategies.stream()
                .filter(s -> s.match(type))
                .findFirst()
                .ifPresent(s -> s.removeCache(categoryId));
    }


}

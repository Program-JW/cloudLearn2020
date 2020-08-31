package com.ruigu.rbox.workflow.strategy;

import com.ruigu.rbox.workflow.model.dto.DutyUserDetailDTO;
import com.ruigu.rbox.workflow.model.dto.DutyUserListDTO;
import com.ruigu.rbox.workflow.model.entity.DutyRuleEntity;
import com.ruigu.rbox.workflow.model.request.DutyUserRequest;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/06/02 16:13
 */
public interface DutyConfigHandleStrategy {

    /**
     * 匹配
     *
     * @param dutyRuleType 值班类型
     * @return 是否匹配
     */
    Boolean match(Integer dutyRuleType);

    /**
     * 保存 （策略新增的后续处理，值班人数据的保存处理）
     *
     * @param rule            规则
     * @param dutyUserRequest 值班人员信息
     */
    void save(DutyRuleEntity rule, DutyUserRequest dutyUserRequest);

    /**
     * 更新 （包括redis更新）
     *
     * @param oldRule         老规则
     * @param dutyUserRequest 值班数据
     */
    void update(DutyRuleEntity oldRule, DutyUserRequest dutyUserRequest);

    /**
     * 查询当天值班人
     *
     * @param categoryId 问题分类id
     * @return 值班人
     */
    List<Integer> queryTodayDutyUser(Integer categoryId);

    /**
     * 启用检验
     *
     * @param rule 规则
     */
    void onRule(DutyRuleEntity rule);

    /**
     * 策略开关
     *
     * @param categoryId 分类id
     * @param rule       规则
     * @param on         true-on  false-off
     */
    void categoryOnOff(Integer categoryId, DutyRuleEntity rule, boolean on);

    /**
     * 查询相应值班人数据
     *
     * @param rule 策略
     * @return 值班数据
     */
    DutyUserDetailDTO queryDutyUser(DutyRuleEntity rule);

    /**
     * 查询相应值班人数据
     *
     * @param rule 策略
     * @param page 分页页数
     * @param size 分页大小
     * @return 值班数据
     */
    DutyUserListDTO queryDutyUser(DutyRuleEntity rule, Integer page, Integer size);

    /**
     * redis更新
     *
     * @param categoryId 分类id
     * @param rule       策略
     */
    void updateCache(Integer categoryId, DutyRuleEntity rule);


    /**
     * redis移除
     *
     * @param categoryId 分类id
     */
    void removeCache(Integer categoryId);
}

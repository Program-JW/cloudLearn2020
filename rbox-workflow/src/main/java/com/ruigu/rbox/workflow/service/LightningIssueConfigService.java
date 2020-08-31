package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.dto.DutyUserByDayDTO;
import com.ruigu.rbox.workflow.model.entity.DutyRuleEntity;
import com.ruigu.rbox.workflow.model.request.*;
import com.ruigu.rbox.workflow.model.vo.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author liqingtian
 * @date 2020/05/07 17:36
 */
public interface LightningIssueConfigService {

    /**
     * 获取问题分类列表信息
     *
     * @param categoryName 模糊匹配分类名称
     * @return 分类列表
     */
    List<LightningCategoryConfigVO> selectIssueCategory(String categoryName);

    /**
     * 新增问题分类
     *
     * @param request 请求参数
     * @return 主键
     */
    Integer saveIssueCategory(LightningCategoryRequest request);

    /**
     * 更新问题分类
     *
     * @param request 请求参数
     */
    void updateIssueCategory(LightningCategoryRequest request);

    /**
     * 问题分类开关
     *
     * @param request 请求参数
     */
    void categoryOnOff(OnOffRequest request);

    /**
     * 查询规则详情
     *
     * @param ruleName 策略名称
     * @return 策略列表
     */
    List<LightningDutyRuleSelectVO> selectDutyRuleList(String ruleName);

    /**
     * 获取某规则的值班人员信息
     *
     * @param ruleId 策略id
     * @param page   页码
     * @param size   每页大小
     * @return 值班人数据
     */
    Page<DutyUserByDayDTO> selectDutyUserByRuleId(Integer ruleId, Integer page, Integer size);

    /**
     * 获取策略详情
     *
     * @param ruleId 策略id
     * @return 策略详情
     */
    LightningDutyRuleDetailVO getDutyRuleDetail(Integer ruleId);

    /**
     * 新增值班策略
     *
     * @param request 增加请求
     * @return id
     */
    Integer addDutyRule(AddDutyRuleRequest request);

    /**
     * 修改值班策略
     *
     * @param request 增加请求
     */
    void updateDutyRule(UpdateDutyRuleRequest request);

    /**
     * 策略下拉列表查询
     *
     * @return 策略列表
     */
    List<DutyRuleEntity> selectDutyRuleDropBoxList();

    /**
     * 规则开关
     *
     * @param request 请求参数
     */
    void ruleOnOff(OnOffRequest request);

    /**
     * 分配某类问题的值班人
     *
     * @param categoryId 分类id
     * @return 值班人信息
     */
    LightningIssueCategoryVO distributionDutyUser(Integer categoryId);

    /**
     * 更新redis缓存
     *
     * @param request 请求
     */
    void updateRedisConfig(UpdateRedisDutyConfigRequest request);

    /**
     * 查询redis缓存值班计划数据
     *
     * @param categoryId 分类id
     * @return 查询redis缓存值班计划数据
     */
    List<Integer> queryRedisDutyUser(Integer categoryId);
}

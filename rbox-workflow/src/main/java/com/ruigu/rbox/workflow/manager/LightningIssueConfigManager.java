package com.ruigu.rbox.workflow.manager;

import com.ruigu.rbox.workflow.model.dto.DutyUserByDayDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2020/05/13 9:55
 */
public interface LightningIssueConfigManager {

    /**
     * 分配某个类型的值班人 (轮询策略专用)
     *
     * @param categoryId 问题类型id
     * @return 值班人id
     */
    Integer distributionDuty(Integer categoryId);

    /**
     * 设置 (轮询策略专用)
     *
     * @param categoryId 问题分类id
     * @param userIds    值班人id
     */
    void initDutyPollUser(Integer categoryId, List<Integer> userIds);

    /**
     * 设置 (轮询策略专用)
     *
     * @param categoryId 问题分类id
     * @param userIds    值班人id
     */
    void initDutyWeekUser(Integer categoryId, List<Integer> userIds);

    /**
     * 设置 (轮询策略专用)
     *
     * @param categoryId 问题分类id
     * @param userIds    值班人id
     */
    void removeAndUpdateDutyPollUser(Integer categoryId, List<Integer> userIds);

    void removeAndUpdateDutyWeekUser(Integer categoryId, List<Integer> userIds);

    /**
     * 删除 （轮询策略）
     *
     * @param categoryId 分类id
     */
    void removeDutyPollUser(Integer categoryId);

    /**
     * 删除 （值班策略）
     *
     * @param categoryId 分类id
     */
    void removeDutyByDayUser(Integer categoryId);

    /**
     * 更新日值班 （按天值班）
     *
     * @param categoryId 问题分类id
     * @param ruleId     策略id
     */
    void updateDutyByDayUser(Integer categoryId, Integer ruleId);

    /**
     * 更换值班人
     *
     * @param categoryId 分类id
     * @param userId     用户id
     */
    void replaceDutyByDayUser(Integer categoryId, Integer userId);

    /**
     * 查询
     *
     * @param ruleId 策略id
     * @param page   分页页数
     * @param size   分页大小
     * @return 分页数据
     */
    Page<DutyUserByDayDTO> queryPageDutyUserByDay(Integer ruleId, Integer page, Integer size);

    /**
     * 根据问题类型id查询值班人
     *
     * @param categoryId 问题分类id
     * @return 值班人id
     */
    Integer queryTodayDutyByDayUser(Integer categoryId);

    /**
     * 查询轮询值班人redis缓存
     *
     * @param categoryId 问题分类id
     * @return 值班人列表
     */
    List<Integer> queryDutyPollUser(Integer categoryId);

    /**
     * 查询当天值班人
     *
     * @param categoryIds 分类id
     * @return 当天值班map ( k-问题分类id v-值班人id )
     */
    Map<Integer, Integer> queryTodayDutyByDayUser(List<Integer> categoryIds);
}

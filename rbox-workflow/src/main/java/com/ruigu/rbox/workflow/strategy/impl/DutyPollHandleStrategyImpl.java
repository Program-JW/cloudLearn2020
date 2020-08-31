package com.ruigu.rbox.workflow.strategy.impl;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.manager.LightningIssueConfigManager;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.model.dto.DutyUserByPollDTO;
import com.ruigu.rbox.workflow.model.dto.DutyUserDetailDTO;
import com.ruigu.rbox.workflow.model.dto.DutyUserListDTO;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.entity.DutyRuleEntity;
import com.ruigu.rbox.workflow.model.entity.LightningIssueCategoryEntity;
import com.ruigu.rbox.workflow.model.enums.DutyRuleTypeEnum;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.request.DutyUserRequest;
import com.ruigu.rbox.workflow.repository.DutyRuleRepository;
import com.ruigu.rbox.workflow.repository.LightningIssueCategoryRepository;
import com.ruigu.rbox.workflow.strategy.DutyConfigHandleStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/06/04 17:00
 */
@Service
public class DutyPollHandleStrategyImpl implements DutyConfigHandleStrategy {

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
        return DutyRuleTypeEnum.DUTY_POLL.getCode().equals(dutyRuleType);
    }

    @Override
    public void save(DutyRuleEntity rule, DutyUserRequest dutyUserRequest) {
        List<Integer> dutyUserIds = dutyUserRequest.getPollList();
        rule.setUserIds(JsonUtil.toJsonString(dutyUserIds));
        dutyRuleRepository.save(rule);
    }

    @Override
    public void update(DutyRuleEntity oldRule, DutyUserRequest request) {
        // 更新数据
        List<Integer> dutyUserIdList = request.getPollList();
        if (CollectionUtils.isEmpty(dutyUserIdList)) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "值班人不能为空");
        }
        // 更新
        oldRule.setUserIds(JsonUtil.toJsonString(dutyUserIdList));
        // 更新redis
        boolean enable = YesOrNoEnum.YES.getCode() == oldRule.getStatus();
        if (enable) {
            List<LightningIssueCategoryEntity> allCategory = lightningIssueCategoryRepository.findAllByRuleIdAndStatus(oldRule.getId(), YesOrNoEnum.YES.getCode());
            allCategory.forEach(c -> lightningIssueConfigManager.removeAndUpdateDutyPollUser(c.getId(), dutyUserIdList));
        }
    }


    @Override
    public List<Integer> queryTodayDutyUser(Integer categoryId) {
        return lightningIssueConfigManager.queryDutyPollUser(categoryId);
    }

    @Override
    public void onRule(DutyRuleEntity rule) {
        // 校验是否有可轮询的值班人
        List<Integer> dutyUserIds = JsonUtil.parseArray(rule.getUserIds(), Integer.class);
        if (CollectionUtils.isEmpty(dutyUserIds)) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "启用失败，数据异常，该策略值班人为空");
        }
        dutyRuleRepository.updateStatus(rule.getId(), YesOrNoEnum.YES.getCode());
    }

    @Override
    public void categoryOnOff(Integer categoryId, DutyRuleEntity rule, boolean on) {
        if (on) {
            if (rule.getStatus() != YesOrNoEnum.YES.getCode()) {
                throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "启用失败，该分类所引用策略为禁用状态");
            }
            lightningIssueConfigManager.removeAndUpdateDutyPollUser(categoryId, JsonUtil.parseArray(rule.getUserIds(), Integer.class));
        } else {
            lightningIssueConfigManager.removeDutyPollUser(categoryId);
        }
    }

    @Override
    public DutyUserDetailDTO queryDutyUser(DutyRuleEntity rule) {

        DutyUserDetailDTO dutyUser = new DutyUserDetailDTO();

        // 轮询
        List<Integer> dutyUserIdList = JsonUtil.parseArray(rule.getUserIds(), Integer.class);
        if (CollectionUtils.isNotEmpty(dutyUserIdList)) {
            List<DutyUserByPollDTO> pollList = new ArrayList<>(10);
            List<PassportUserInfoDTO> userInfoList = passportFeignManager.getUserInfoListFromRedis(dutyUserIdList);
            userInfoList.forEach(u -> {
                DutyUserByPollDTO user = new DutyUserByPollDTO();
                user.setId(u.getId());
                user.setName(u.getNickname());
                pollList.add(user);
            });
            dutyUser.setPollList(pollList);
        }

        return dutyUser;
    }

    @Override
    public DutyUserListDTO queryDutyUser(DutyRuleEntity rule, Integer page, Integer size) {
        DutyUserListDTO listDTO = new DutyUserListDTO();
        DutyUserDetailDTO dutyUserDetail = queryDutyUser(rule);
        if (dutyUserDetail != null) {
            listDTO.setPollList(dutyUserDetail.getPollList());
        }
        return listDTO;
    }

    @Override
    public void updateCache(Integer categoryId, DutyRuleEntity rule) {
        List<Integer> userIds = JsonUtil.parseArray(rule.getUserIds(), Integer.class);
        if (CollectionUtils.isEmpty(userIds)) {
            throw new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "数据异常，值班人查询为空");
        }
        lightningIssueConfigManager.removeAndUpdateDutyPollUser(categoryId, userIds);
    }

    @Override
    public void removeCache(Integer categoryId) {
        lightningIssueConfigManager.removeDutyPollUser(categoryId);
    }
}

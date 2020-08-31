package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.manager.UserManager;
import com.ruigu.rbox.workflow.model.dto.PassportGroupInfoDTO;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.dto.SpecialAfterSaleReviewPositionDTO;
import com.ruigu.rbox.workflow.model.dto.UserGroupSimpleDTO;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleReviewEntity;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleReviewNodeEntity;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleReviewPositionEntity;
import com.ruigu.rbox.workflow.model.enums.PositionEnum;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.Symbol;
import com.ruigu.rbox.workflow.model.request.SearchGroupRequest;
import com.ruigu.rbox.workflow.model.vo.SpecialAfterSaleNextNodeAndCcVO;
import com.ruigu.rbox.workflow.repository.SpecialAfterSaleReviewNodeRepository;
import com.ruigu.rbox.workflow.repository.SpecialAfterSaleReviewPositionRepository;
import com.ruigu.rbox.workflow.repository.SpecialAfterSaleReviewRepository;
import com.ruigu.rbox.workflow.service.SpecialAfterSaleConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liqingtian
 * @date 2020/08/11 16:37
 */
@Service
public class SpecialAfterSaleConfigServiceImpl implements SpecialAfterSaleConfigService {

    @Resource
    private SpecialAfterSaleReviewRepository specialAfterSaleReviewRepository;
    @Resource
    private SpecialAfterSaleReviewNodeRepository specialAfterSaleReviewNodeRepository;
    @Resource
    private SpecialAfterSaleReviewPositionRepository specialAfterSaleReviewPositionRepository;
    @Resource
    private PassportFeignManager passportFeignManager;
    @Resource
    private UserManager userManager;

    @Override
    public List<SpecialAfterSaleReviewPositionDTO> matchConfigs(Integer userId) {
        UserGroupSimpleDTO userGroupSimple = userManager.searchUserGroupFromCache(userId);
        if (Objects.isNull(userGroupSimple)) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "查询不到当前人员信息");
        }
        return matchConfigs(userGroupSimple);
    }

    @Override
    public List<SpecialAfterSaleReviewPositionDTO> matchConfigs(UserGroupSimpleDTO userInfo) {
        String position = userInfo.getPosition();
        List<UserGroupSimpleDTO.GroupInfoVO> groups = userInfo.getGroups();
        // 获取所有启用的配置
        List<SpecialAfterSaleReviewPositionDTO> reviewPositionList = queryEnableReviewPositionInfo();
        // 匹配
        List<SpecialAfterSaleReviewPositionDTO> matchReviewList = new ArrayList<>();
        reviewPositionList.forEach(r -> {
            List<UserGroupSimpleDTO.GroupInfoVO> collect = groups.stream()
                    .filter(g -> g.getGroupDecs().startsWith(r.getGroupName()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                if (r.getPositionList().contains(position)) {
                    matchReviewList.add(r);
                }
            }
        });
        return matchReviewList;
    }

    @Override
    public SpecialAfterSaleNextNodeAndCcVO queryNextNodeAndCcInfo() {
        Integer userId = UserHelper.getUserId();
        if (Objects.isNull(userId)) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "获取不到当前操作人id");
        }
        UserGroupSimpleDTO userGroupSimple = userManager.searchUserGroupFromCache(userId);
        if (Objects.isNull(userGroupSimple)) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "获取不到当前操作人信息");
        }
        List<SpecialAfterSaleReviewPositionDTO> matchConfigs = matchConfigs(userGroupSimple);
        if (CollectionUtils.isEmpty(matchConfigs)) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "当前用户没有相匹配的审批规则");
        }
        // 默认使用第一个
        SpecialAfterSaleReviewPositionDTO firstMatch = matchConfigs.get(0);
        SpecialAfterSaleNextNodeAndCcVO info = new SpecialAfterSaleNextNodeAndCcVO();
        List<PassportUserInfoDTO> userInfoList = passportFeignManager.getUserInfoListFromRedis(firstMatch.getCcIds());
        info.setCcList(userInfoList);
        // 根据职位不同判断下一个节点 （电销特殊，固定为电销主管）
        if (PositionEnum.DX.getPosition().equals(userGroupSimple.getPosition())) {
            info.setNextNode(PositionEnum.DXM.getPosition());
        } else {
            SpecialAfterSaleReviewNodeEntity firstNode = specialAfterSaleReviewNodeRepository.findTopByConfigIdOrderBySort(firstMatch.getConfigId());
            info.setNextNode(firstNode.getName());
        }
        return info;
    }

    private List<SpecialAfterSaleReviewPositionDTO> queryEnableReviewPositionInfo() {
        // 首先查询所有启用的配置
        List<SpecialAfterSaleReviewEntity> allReviews = specialAfterSaleReviewRepository.findAllByStatusOrderByIdDesc(YesOrNoEnum.YES.getCode());
        if (CollectionUtils.isEmpty(allReviews)) {
            return Collections.emptyList();
        }
        // 分别查询部门名称和对应职位
        List<Integer> configIdList = new ArrayList<>();
        List<Integer> groupIdList = new ArrayList<>();
        allReviews.forEach(r -> {
            configIdList.add(r.getId());
            groupIdList.add(r.getGroupId());
        });
        // 查询部门信息
        Map<Integer, PassportGroupInfoDTO> groupInfoMap = passportFeignManager.searchGroup(SearchGroupRequest.builder().groupIds(groupIdList).build());
        // 查询对应的职位信息
        List<SpecialAfterSaleReviewPositionEntity> allPositionList = specialAfterSaleReviewPositionRepository.findAllByConfigIdIn(configIdList);
        Map<Integer, List<SpecialAfterSaleReviewPositionEntity>> positionMap = allPositionList.stream().collect(Collectors.groupingBy(SpecialAfterSaleReviewPositionEntity::getConfigId));
        // 组装数据
        List<SpecialAfterSaleReviewPositionDTO> results = new ArrayList<>();
        for (SpecialAfterSaleReviewEntity review : allReviews) {
            SpecialAfterSaleReviewPositionDTO info = new SpecialAfterSaleReviewPositionDTO();
            Integer configId = review.getId();
            info.setConfigId(configId);
            Integer groupId = review.getGroupId();
            info.setGroupId(groupId);
            PassportGroupInfoDTO groupInfo = groupInfoMap.getOrDefault(groupId, null);
            if (groupInfo != null) {
                info.setGroupName(groupInfo.getDescription());
            }
            List<SpecialAfterSaleReviewPositionEntity> positionList = positionMap.getOrDefault(configId, null);
            if (CollectionUtils.isNotEmpty(positionList)) {
                info.setPositionList(positionList.stream().map(SpecialAfterSaleReviewPositionEntity::getPosition).collect(Collectors.toList()));
            }
            String cc = review.getCcIds();
            if (StringUtils.isNotBlank(cc)) {
                info.setCcIds(Stream.of(StringUtils.split(cc, Symbol.COMMA.getValue())).map(Integer::valueOf).collect(Collectors.toList()));
            }
            results.add(info);
        }
        return results;
    }
}

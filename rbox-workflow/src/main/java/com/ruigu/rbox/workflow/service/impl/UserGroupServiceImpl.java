package com.ruigu.rbox.workflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.entity.UserGroupAssoEntity;
import com.ruigu.rbox.workflow.model.entity.UserGroupEntity;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.request.SelectRequest;
import com.ruigu.rbox.workflow.model.request.UserGroupAssoRequest;
import com.ruigu.rbox.workflow.model.request.UserGroupRequest;
import com.ruigu.rbox.workflow.model.vo.SelectOption;
import com.ruigu.rbox.workflow.model.vo.UserGroupAssoVO;
import com.ruigu.rbox.workflow.repository.UserGroupAssoRepository;
import com.ruigu.rbox.workflow.repository.UserGroupRepository;
import com.ruigu.rbox.workflow.service.UserGroupService;
import com.ruigu.rbox.workflow.supports.ValidResult;
import com.ruigu.rbox.workflow.supports.ValidUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liqingtian
 * @date 2019/09/02 20:11
 */
@Service
@Slf4j
public class UserGroupServiceImpl implements UserGroupService {

    @Resource
    private UserGroupRepository userGroupRepository;

    @Resource
    private UserGroupAssoRepository userGroupAssoRepository;

    @Resource
    private PassportFeignClient passPortFeignClient;

    @Override
    public void createUserGroup(UserGroupEntity userGroupEntity) {
        ValidResult validResult = ValidUtil.validFields(userGroupEntity,
                new String[]{"id"},
                new Object[][]{
                        {ValidUtil.MUST_NULL}
                });
        if (!validResult.valid) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), validResult.message);
        }
        Integer userId = UserHelper.getUserId();
        userGroupEntity.setCreatedBy(userId);
        userGroupEntity.setStatus(userGroupEntity.getStatus());
        userGroupEntity.setLastUpdatedBy(userId);
        try {
            userGroupRepository.save(userGroupEntity);
        } catch (Exception e) {
            log.error("--> createUserGroup ", e);
            throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), "创建用户组失败。");
        }
    }

    @Override
    public void updateUserGroup(UserGroupEntity userGroupEntity) {
        ValidResult validResult = ValidUtil.validFields(userGroupEntity,
                new String[]{"id", "status"},
                new Object[][]{
                        {ValidUtil.REQUIRED, ValidUtil.NON_NEGATIVE_INTEGER},
                        {ValidUtil.REQUIRED, ValidUtil.NON_NEGATIVE_INTEGER}
                });
        if (!validResult.valid) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), validResult.message);
        }
        UserGroupEntity oldGroup = userGroupRepository.findById(userGroupEntity.getId()).orElseThrow(() -> new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "您所修改的用户组不存在。"));
        Integer userId = UserHelper.getUserId();
        oldGroup.setLastUpdatedBy(userId);
        oldGroup.setName(userGroupEntity.getName());
        oldGroup.setRemark(userGroupEntity.getRemark());
        oldGroup.setStatus(userGroupEntity.getStatus());

        try {
            userGroupRepository.save(oldGroup);
        } catch (Exception e) {
            log.error("--> updateUserGroup ", e);
            throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), "更新用户组信息失败。");
        }
    }

    @Override
    public void removeUserGroup(Integer groupId) {
        if (groupId == null) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "用户组id不能为空。");
        }
        if (!userGroupRepository.findById(groupId).isPresent()) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "删除的用户组不存在。");
        }
        try {
            userGroupRepository.deleteById(groupId);
        } catch (Exception e) {
            log.error("--> removeUserGroup ", e);
            throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), "删除失败。");
        }
    }

    @Override
    public Page<UserGroupEntity> selectGroupPage(UserGroupRequest req) {

        return userGroupRepository.findAll(new Specification<UserGroupEntity>() {
            @Override
            public Predicate toPredicate(Root<UserGroupEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (req.getName() != null && !"".equals(req.getName())) {
                    predicates.add(cb.like(root.get("name"), "%" + req.getName() + "%"));
                }
                if (req.getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), req.getStatus()));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        }, PageRequest.of(req.getPageNum(), req.getPageSize()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUserToGroup(UserGroupAssoRequest userGroupAssoRequest) {
        ValidResult validResult = ValidUtil.validFields(userGroupAssoRequest,
                new String[]{"groupId"},
                new Object[][]{
                        {ValidUtil.REQUIRED, ValidUtil.NON_NEGATIVE_INTEGER}
                });
        if (!validResult.valid) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), validResult.message);
        }

        List<Integer> userIds = userGroupAssoRequest.getUserIds();
        Integer groupId = userGroupAssoRequest.getGroupId();

        if (CollectionUtils.isEmpty(userIds)) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "所要添加的用户为空");
        }

        if (userGroupRepository.findByIdAndStatus(userGroupAssoRequest.getGroupId(), 1) == null) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "您所要添加的用户组不存在或已停用。");
        }

        List<UserGroupAssoEntity> userGroupAssoList = new ArrayList<>();
        // 修改为已有则不操作 新的则添加
        userIds.forEach(user -> {
            if (userGroupAssoRepository.findByGroupIdAndUserId(groupId, user) == null) {
                UserGroupAssoEntity userGroupAsso = new UserGroupAssoEntity();
                Integer operatorId = UserHelper.getUserId();
                userGroupAsso.setUserId(user);
                userGroupAsso.setGroupId(groupId);
                userGroupAsso.setCreatedBy(operatorId);
                userGroupAsso.setLastUpdatedBy(operatorId);
                userGroupAsso.setStatus(1);
                userGroupAssoList.add(userGroupAsso);
            }
        });

        try {
            userGroupAssoRepository.saveAll(userGroupAssoList);
        } catch (Exception e) {
            log.error("--> addUserToGroup ", e);
            throw new VerificationFailedException(ResponseCode.INTERNAL_ERROR.getCode(), "添加失败。");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse removeUserToGroup(UserGroupAssoEntity userGroupAsso) {
        ValidResult validResult = ValidUtil.validFields(userGroupAsso,
                new String[]{"userId", "groupId"},
                new Object[][]{
                        {ValidUtil.REQUIRED, ValidUtil.NON_NEGATIVE_INTEGER},
                        {ValidUtil.REQUIRED, ValidUtil.NON_NEGATIVE_INTEGER}
                });
        if (!validResult.valid) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), validResult.message);
        }
        UserGroupAssoEntity byGroupIdAndUserId = userGroupAssoRepository.findByGroupIdAndUserId(userGroupAsso.getGroupId(), userGroupAsso.getUserId());
        if (byGroupIdAndUserId == null) {
            return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), "您所要删除的用户不存在。");
        }
        try {
            userGroupAssoRepository.deleteByGroupIdAndUserId(userGroupAsso.getGroupId(), userGroupAsso.getUserId());
            return ServerResponse.ok();
        } catch (Exception e) {
            log.error("--> removeUserToGroup ", e);
            return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), "删除失败。");
        }
    }

    @Override
    public Page<UserGroupAssoVO> selectGroupAssoPage(UserGroupAssoRequest req) {
        ValidResult validResult = ValidUtil.validFields(req,
                new String[]{"groupId"},
                new Object[][]{
                        {ValidUtil.REQUIRED, ValidUtil.NON_NEGATIVE_INTEGER}
                });
        if (!validResult.valid) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), validResult.message);
        }
        Page<UserGroupAssoVO> page = userGroupAssoRepository.selectAllAssoPage(req, PageRequest.of(req.getPageNum(), req.getPageSize()));
        if (CollectionUtils.isEmpty(page.getContent())) {
            return page;
        }
        List<Integer> userIdList = page.getContent()
                .stream()
                .distinct()
                .map(UserGroupAssoVO::getUserId)
                .collect(Collectors.toList());
        ServerResponse<List<PassportUserInfoDTO>> userInfoResponse = passPortFeignClient.getUserMsgByIds(userIdList);
        if (userInfoResponse.getCode() == ResponseCode.SUCCESS.getCode()) {
            List<PassportUserInfoDTO> userInfoList = userInfoResponse.getData();
            page.getContent().forEach(data -> {
                userInfoList.stream()
                        .filter(user -> user.getId().equals(data.getUserId()))
                        .findFirst().ifPresent(userInfo -> data.setUserName(userInfo.getNickname()));
            });
        } else {
            log.error("| - 远程调用失败 ： [ 返回信息 ： " + JSONObject.toJSONString(userInfoResponse) + " ]");
        }
        return page;
    }

    @Override
    public UserGroupEntity getUserGroupById(Integer groupId) {
        if (groupId == null) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "用户组id不能为空。");
        }
        return userGroupRepository.findById(groupId).orElse(null);
    }

    @Override
    public List<SelectOption> selectGroups(SelectRequest request) {
        List<Integer> ids = new ArrayList<>();
        if (request.getIds() != null) {
            String[] idsStringList = request.getIds().split(",");
            for (String idString : idsStringList) {
                if (StringUtils.isBlank(idString)) {
                    continue;
                }
                ids.add(Integer.valueOf(idString.trim()));
            }
        }
        return selectGroups(ids, request.getKey(), request.getTop());
    }

    @Override
    public List<SelectOption> selectGroups(List<Integer> ids, String key, Integer top) {
        String theKey = "";
        Integer limit = 10;
        if (top != null) {
            limit = top;
        }
        if (StringUtils.isNotBlank(key)) {
            theKey = key;
        }
        Pageable pageable = PageRequest.of(0, limit);
        Page<UserGroupEntity> page = userGroupRepository.findByNameAndStatus(theKey, 1, pageable);
        List<UserGroupEntity> list = page.getContent();
        List<SelectOption> options = new ArrayList<>();
        Map<Integer, Boolean> dic = new HashMap<>(16);
        if (CollectionUtils.isNotEmpty(ids)) {
            List<UserGroupEntity> selects = userGroupRepository.findByIdInAndStatus(ids, 1);
            selects.forEach((entity) -> {
                dic.put(entity.getId(), true);
                options.add(new SelectOption(entity.getName(), entity.getId()));
            });
        }
        list.forEach((entity) -> {
            if (!dic.containsKey(entity.getId())) {
                options.add(new SelectOption(entity.getName(), entity.getId()));
            }
        });
        return options;
    }

    @Override
    public List<Integer> getUserListByGroupsInt(List<String> groupIds) {
        List<Integer> groups = new ArrayList<>();
        groupIds.forEach(group ->
                groups.add(Integer.valueOf(group))
        );
        return userGroupAssoRepository.selectAllUserByGroupsInt(groups);
    }
}

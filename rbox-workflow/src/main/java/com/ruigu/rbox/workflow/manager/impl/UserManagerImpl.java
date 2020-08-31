package com.ruigu.rbox.workflow.manager.impl;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.constants.RedisKeyConstants;
import com.ruigu.rbox.workflow.feign.UserFeignClient;
import com.ruigu.rbox.workflow.manager.RedisInitManager;
import com.ruigu.rbox.workflow.manager.UserManager;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.UserGroupSimpleDTO;
import com.ruigu.rbox.workflow.model.dto.UserSearchRequestDTO;
import com.ruigu.rbox.workflow.model.enums.Symbol;
import com.ruigu.rbox.workflow.model.request.UserGroupSearchReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liqingtian
 * @date 2020/08/05 22:07
 */
@Slf4j
@Service
public class UserManagerImpl implements UserManager {

    @Resource
    private UserFeignClient client;

    @Resource
    private RedisInitManager redisInitManager;

    @Override
    public Map<Integer, UserGroupSimpleDTO> searchUserGroupFromApi(UserSearchRequestDTO request) {
        UserGroupSearchReq searchReq = new UserGroupSearchReq();
        searchReq.setUserStatus(request.getUserStatus());
        searchReq.setUserDeleted(request.getUserDeleted());
        searchReq.setGroupStatus(request.getGroupStatus());
        searchReq.setGroupDeleted(request.getGroupDeleted());
        List<Integer> userIdList = request.getUserIdList();
        if (CollectionUtils.isNotEmpty(userIdList)) {
            searchReq.setUserIdList(StringUtils.join(userIdList, Symbol.COMMA.getValue()));
        }
        ResponseEntity<ServerResponse<Map<Integer, UserGroupSimpleDTO>>> response = client.getUserDeptInfo(searchReq);
        if (response.getStatusCode().is2xxSuccessful()) {
            ServerResponse<Map<Integer, UserGroupSimpleDTO>> serverResponse = response.getBody();
            if (!serverResponse.isSuccess()) {
                log.error("查询用户部门信息失败");
                return Collections.emptyMap();
            }
            return serverResponse.getData();
        } else {
            log.error(JsonUtil.toJsonString(response));
            return Collections.emptyMap();
        }
    }

    @Override
    public Map<Integer, UserGroupSimpleDTO> searchUserGroupFromCache(Collection<Integer> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        return embellishGroupDesc(queryAndInit(new HashSet<>(userIds)));
    }

    @Override
    public UserGroupSimpleDTO searchUserGroupFromCache(Integer userId) {
        Map<Integer, UserGroupSimpleDTO> userGroupInfo = queryAndInit(Collections.singleton(userId));
        return userGroupInfo.getOrDefault(userId, null);
    }

    private Map<Integer, UserGroupSimpleDTO> queryAndInit(Collection<Integer> userIdList) {
        // 先从缓存中获取部门信息
        Map<Integer, UserGroupSimpleDTO> userGroupMap = redisInitManager.batchGet(userIdList, RedisKeyConstants.PASSPORT_USER_GROUP_INFO, Integer.class, UserGroupSimpleDTO.class);
        // 比较结果时候缺失
        Set<Integer> keySet = userGroupMap.keySet();
        // 缺少的
        List<Integer> collect = userIdList.stream().filter(id -> !keySet.contains(id)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            // 调用api去查询数据库
            Map<Integer, UserGroupSimpleDTO> result = queryUserGroupByApi(collect);
            if (Objects.nonNull(result) && !result.isEmpty()) {
                userGroupMap.putAll(result);
                // 更新缓存中缺少的数据
                redisInitManager.initUserGroupInfo(result);
            }
        }
        // 返回搜索结果
        return userGroupMap;
    }

    private Map<Integer, UserGroupSimpleDTO> embellishGroupDesc(Map<Integer, UserGroupSimpleDTO> data) {
        if (!Objects.nonNull(data) || data.isEmpty()) {
            return data;
        }
        List<Integer> noGroupIdList = new ArrayList<>();
        // 循环处理
        data.forEach((id, info) -> {
            List<UserGroupSimpleDTO.GroupInfoVO> groups = info.getGroups();
            if (CollectionUtils.isNotEmpty(groups)) {
                UserGroupSimpleDTO.GroupInfoVO group = info.getGroups().get(0);
                String groupDecs = group.getGroupDecs();
                if (StringUtils.isBlank(groupDecs)) {
                    return;
                }
                List<String> descItem = new ArrayList<>(Arrays.asList(groupDecs.split("/")));
                if (CollectionUtils.isEmpty(descItem)) {
                    return;
                }
                // 去除第一个,第二个
                descItem.remove(0);
                descItem.remove(0);
                // 前端显示限制
                final int maxLimit = 5;
                String newDesc = "";
                if (descItem.size() <= maxLimit) {
                    newDesc = StringUtils.join(descItem, "/");
                } else {
                    List<String> newDescList = new ArrayList<>();
                    for (int i = 0; i < maxLimit; i++) {
                        newDescList.add(descItem.get(i));
                    }
                    newDesc = StringUtils.join(newDescList, "/");
                }
                group.setGroupDecs(newDesc);
            } else {
                // 删除
                noGroupIdList.add(id);
            }
        });
        if (CollectionUtils.isNotEmpty(noGroupIdList)) {
            log.error("查询用户-部门信息，部分人员没有id信息：{}", JsonUtil.toJsonString(noGroupIdList));
            // 删除不存在群组的
            data.keySet().removeIf(noGroupIdList::contains);
        }
        // 返回的数据中不存没有部门信息的
        return data;
    }

    private Map<Integer, UserGroupSimpleDTO> queryUserGroupByApi(List<Integer> userIds) {
        UserSearchRequestDTO request = new UserSearchRequestDTO();
        request.setUserIdList(userIds);
        return searchUserGroupFromApi(request);
    }
}

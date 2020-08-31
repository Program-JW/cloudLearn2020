package com.ruigu.rbox.workflow.manager.impl;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.constants.RedisKeyConstants;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.manager.RedisInitManager;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.PassportGroupInfoDTO;
import com.ruigu.rbox.workflow.model.dto.PassportUserAndGroupDTO;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.dto.UserInfoLuaDTO;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.Symbol;
import com.ruigu.rbox.workflow.model.request.PassportUserSearchReq;
import com.ruigu.rbox.workflow.model.request.SearchGroupRequest;
import com.ruigu.rbox.workflow.model.vo.UserExtraRelationshipVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liqingtian
 * @date 2020/03/13 15:31
 */
@Slf4j
@Service
public class PassportFeignManagerImpl implements PassportFeignManager {

    @Resource
    private PassportFeignClient client;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisInitManager redisInitManager;

    @Override
    public List<PassportUserInfoDTO> getSuperiorLeader(Integer userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        Map<Integer, List<List<PassportUserInfoDTO>>> leaderInfo = queryLeaderInfo(Collections.singleton(userId));
        if (leaderInfo.isEmpty()) {
            return new ArrayList<>();
        }
        return getLeaderByLevel(userId, leaderInfo);
    }

    @Override
    public List<List<PassportUserInfoDTO>> getAllLeader(Integer userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        Map<Integer, List<List<PassportUserInfoDTO>>> leaderInfo = queryLeaderInfoThrowExt(Collections.singleton(userId));
        List<List<PassportUserInfoDTO>> leaders = leaderInfo.get(userId);
        if (leaders == null) {
            leaders = new ArrayList<>();
        }
        return leaders;
    }

    @Override
    public Map<Integer, List<List<PassportUserInfoDTO>>> batchGetAllLeader(Collection<Integer> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new HashMap<>(2);
        }
        return queryLeaderInfo(userIds);
    }

    @Override
    public List<UserInfoLuaDTO> getAllUserInfo() {
        // 获取权限中心用户数据
        PassportUserAndGroupDTO passportUserAndGroupDTO = searchUser(PassportUserSearchReq.builder().build());
        List<UserInfoLuaDTO> userList = new ArrayList<>();
        treeList(passportUserAndGroupDTO, 0, userList);
        return userList;
    }

    @Override
    public List<PassportUserInfoDTO> getAllRedisUser() {
        // 获取所有的key
        Set<String> resultKeys = getAllUserKeys();
        // 尝试管道
        List<Object> resultValues = stringRedisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                if (CollectionUtils.isNotEmpty(resultKeys)) {
                    for (String key : resultKeys) {
                        if (key.startsWith(RedisKeyConstants.PASSPORT_USER_CACHE_ENABLE)) {
                            continue;
                        }
                        redisConnection.get(key.getBytes());
                    }
                }
                return null;
            }
        });
        // 转换类型
        List<PassportUserInfoDTO> userList = new ArrayList<>();
        resultValues.forEach(s -> {
            JsonUtil.parseObject((String) s, PassportUserInfoDTO.class);
        });
        return userList;
    }

    @Override
    public List<Integer> getAllRedisUserIds() {
        List<Integer> userIds = new ArrayList<>();
        Set<String> resultKeys = getAllUserKeys();
        if (CollectionUtils.isNotEmpty(resultKeys)) {
            resultKeys.forEach(key -> {
                if (key.startsWith(RedisKeyConstants.PASSPORT_USER_CACHE_ENABLE)) {
                    return;
                }
                String[] split = key.split(Symbol.COLON.getValue());
                userIds.add(Integer.valueOf(split[split.length - 1]));
            });
        }
        return userIds;
    }

    private Set<String> getAllUserKeys() {
        // 线上库 禁止用keys命令 ， 使用scan为最佳选择
        String match = RedisKeyConstants.PASSPORT_USER_INFO + "*";
        Set<String> resultKeys = stringRedisTemplate.execute((RedisConnection connection) -> {
            Set<String> matchKeys = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(match).count(1000).build());
            while (cursor.hasNext()) {
                matchKeys.add(new String(cursor.next()));
            }
            return matchKeys;
        });
        return resultKeys;
    }

    @Override
    public PassportUserInfoDTO getUserInfoFromPassport(Integer id) {
        if (Objects.nonNull(id)) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "用户id不能为空");
        }
        return queryUserInfoFromApi(Collections.singleton(id)).get(0);
    }

    @Override
    public PassportUserInfoDTO getUserInfoFromRedis(Integer id) {
        // 先搜索内存
        String json = stringRedisTemplate.opsForValue().get(RedisKeyConstants.PASSPORT_USER_INFO + id);
        if (StringUtils.isNotBlank(json)) {
            return JsonUtil.parseObject(json, PassportUserInfoDTO.class);
        }
        // 搜索为空 mysql
        List<PassportUserInfoDTO> userInfoList = queryUserInfoFromFeignAndLoadRedis(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(userInfoList)) {
            throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), "获取用户信息失败");
        }
        return userInfoList.get(0);
    }

    @Override
    public Map<Integer, PassportUserInfoDTO> getUserInfoMapFromRedis(Collection<Integer> ids) {
        // 去重
        Set<Integer> userIdSet = new HashSet<>(ids);
        if (CollectionUtils.isEmpty(userIdSet)) {
            return new HashMap<>(16);
        }
        // 批量 获取 采用lua脚本
        Map<Integer, PassportUserInfoDTO> data = redisInitManager.batchGet(userIdSet, RedisKeyConstants.PASSPORT_USER_INFO, Integer.class, PassportUserInfoDTO.class);
        if (!data.isEmpty()) {
            // 若查询数量少
            if (data.size() < userIdSet.size()) {
                List<Integer> lostIds = userIdSet.stream().filter(id -> !data.containsKey(id)).collect(Collectors.toList());
                List<PassportUserInfoDTO> lostUserInfoList = queryUserInfoFromFeignAndLoadRedis(lostIds);
                lostUserInfoList.forEach(u -> data.put(u.getId(), u));
            }
            return data;
        }

        // 搜索mysql
        List<PassportUserInfoDTO> userList = queryUserInfoFromFeignAndLoadRedis(userIdSet);
        Map<Integer, PassportUserInfoDTO> userMap = new HashMap<>(16);
        userList.forEach(u -> userMap.put(u.getId(), u));
        return userMap;

    }

    @Override
    public List<PassportUserInfoDTO> getUserInfoListFromRedis(Collection<Integer> ids) {
        // 去重
        Set<Integer> userIdSet = new HashSet<>(ids);
        if (CollectionUtils.isEmpty(userIdSet)) {
            return new ArrayList<>();
        }

        // 批量 获取 采用lua脚本
        Map<Integer, PassportUserInfoDTO> data = redisInitManager.batchGet(userIdSet, RedisKeyConstants.PASSPORT_USER_INFO, Integer.class, PassportUserInfoDTO.class);
        if (!data.isEmpty()) {
            List<PassportUserInfoDTO> userInfoList = new ArrayList<>(data.values());
            // 若查询数量少
            if (data.size() < userIdSet.size()) {
                List<Integer> lostIds = userIdSet.stream().filter(id -> !data.containsKey(id)).collect(Collectors.toList());
                List<PassportUserInfoDTO> lostUserInfoList = queryUserInfoFromFeignAndLoadRedis(lostIds);
                userInfoList.addAll(lostUserInfoList);
            }
            return userInfoList;
        }

        return queryUserInfoFromFeignAndLoadRedis(userIdSet);
    }

    @Override
    public PassportGroupInfoDTO getGroupInfoById(Integer groupId) {
        ServerResponse<PassportGroupInfoDTO> groupResponse = client.getGroupById(groupId);
        PassportGroupInfoDTO groupInfo = groupResponse.getData();
        if (ResponseCode.SUCCESS.getCode() != groupResponse.getCode() || groupInfo == null) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "部门信息查询失败");
        }
        return groupInfo;
    }

    @Override
    public Map<Integer, PassportGroupInfoDTO> searchGroup(SearchGroupRequest request) {
        List<Integer> groupIds = request.getGroupIds();
        if (CollectionUtils.isNotEmpty(groupIds)) {
            request.setGroupIdList(StringUtils.join(groupIds, Symbol.COMMA.getValue()));
        }
        ServerResponse<List<PassportGroupInfoDTO>> serverResponse = client.batchGetGroupInfo(request);
        if (!serverResponse.isSuccess()) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "搜索部门信息异常");
        }
        List<PassportGroupInfoDTO> data = serverResponse.getData();
        if (CollectionUtils.isNotEmpty(data)) {
            return data.stream().collect(Collectors.toMap(PassportGroupInfoDTO::getId, g -> g));
        }
        return Collections.emptyMap();
    }

    @Override
    public List<PassportGroupInfoDTO> getAllGroupInfo() {
        Map<String, Object> data = new HashMap<>(4);
        data.put("id", null);
        ServerResponse<List<PassportGroupInfoDTO>> allGroupResponse = client.getAllGroup(data);
        List<PassportGroupInfoDTO> allGroup = allGroupResponse.getData();
        if (ResponseCode.SUCCESS.getCode() != allGroupResponse.getCode() || CollectionUtils.isEmpty(allGroup)) {
            throw new VerificationFailedException(ResponseCode.INTERNAL_ERROR.getCode(), "获取部门信息失败");
        }
        return allGroup;
    }

    @Override
    public List<PassportUserInfoDTO> getListUserByRoleCode(String roleCode) {
        ServerResponse<List<PassportUserInfoDTO>> serverResponse = client.getListUserByRoleCode(roleCode);
        if (!serverResponse.isSuccess()) {
            throw new GlobalRuntimeException(serverResponse.getCode(),
                    StringUtils.isBlank(serverResponse.getMessage()) ? serverResponse.getMsg() : serverResponse.getMessage());
        }
        return serverResponse.getData();
    }

    @Override
    public PassportUserInfoDTO getUserByWxUserId(String wxUserId) {
        ServerResponse<PassportUserInfoDTO> serverResponse = client.getIdByUserId(wxUserId);
        if (!serverResponse.isSuccess()) {
            throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), "通过微信id获取用户信息失败");
        }
        return serverResponse.getData();
    }

    @Override
    public UserExtraRelationshipVO getExtraUserInfoByUserId(Integer userId) {
        ServerResponse<UserExtraRelationshipVO> serverResponse = client.getExtraUserInfoByUserId(userId);
        if (!serverResponse.isSuccess()) {
            throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), "获取第三方用户信息失败");
        }
        return serverResponse.getData();
    }

    private List<PassportUserInfoDTO> queryUserInfoFromApi(Collection<Integer> userIds) {
        ServerResponse<List<PassportUserInfoDTO>> serverResponse = client.getUserMsgByIds(userIds);
        List<PassportUserInfoDTO> userList = serverResponse.getData();
        if (ResponseCode.SUCCESS.getCode() != serverResponse.getCode() || CollectionUtils.isEmpty(userList)) {
            log.error(" ====================== 用户信息获取失败 ： userids - {}  ======================= ", userIds);
            return new ArrayList<>();
        }
        return userList;
    }

    private PassportUserAndGroupDTO searchUser(PassportUserSearchReq req) {
        ServerResponse<PassportUserAndGroupDTO> userAndGroup = client.getUserAndGroup(req);
        PassportUserAndGroupDTO userAndGroupFeignInfoDTO = userAndGroup.getData();
        if (ResponseCode.SUCCESS.getCode() != userAndGroup.getCode() || userAndGroupFeignInfoDTO == null) {
            throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(),
                    "请求权限中心搜索公司全体员工接口异常");
        }
        return userAndGroupFeignInfoDTO;
    }

    private List<PassportUserInfoDTO> queryUserInfoFromFeignAndLoadRedis(Collection<Integer> userIds) {
        List<PassportUserInfoDTO> userList = queryUserInfoFromApi(userIds);
        redisInitManager.asyncInitPassportUserInfo(userList);
        return userList;
    }

    private Map<Integer, List<List<PassportUserInfoDTO>>> queryLeaderInfo(Collection<Integer> userIds) {
        ServerResponse<Map<Integer, List<List<PassportUserInfoDTO>>>> leaderInfoResponse = client.getUserAllLeaderInfo(userIds);
        Map<Integer, List<List<PassportUserInfoDTO>>> leaderInfo = leaderInfoResponse.getData();
        if (leaderInfoResponse.getCode() != ResponseCode.SUCCESS.getCode() || leaderInfo == null) {
            log.error("获取 Leader Info 失败:{}", leaderInfoResponse.getMessage());
            return new HashMap<>(2);
        }
        return leaderInfoResponse.getData();
    }

    private Map<Integer, List<List<PassportUserInfoDTO>>> queryLeaderInfoThrowExt(Collection<Integer> userIds) {
        ServerResponse<Map<Integer, List<List<PassportUserInfoDTO>>>> leaderInfoResponse = client.getUserAllLeaderInfo(userIds);
        if (leaderInfoResponse.getCode() != ResponseCode.SUCCESS.getCode()) {
            log.error("获取 Leader Info 失败. response {}", JsonUtil.toJsonString(leaderInfoResponse));
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "获取上级信息失败");
        }
        return leaderInfoResponse.getData();
    }

    private void treeList(PassportUserAndGroupDTO menuList, Integer parentId, List<UserInfoLuaDTO> userList) {
        Integer voType = menuList.getVoType();
        if (voType.equals(2)) {
            UserInfoLuaDTO userInfoLuaDTO = new UserInfoLuaDTO();
            userInfoLuaDTO.setUserId(menuList.getUserId());
            userInfoLuaDTO.setInfo(JsonUtil.toJsonString(menuList.toUserInfo()));
            userList.add(userInfoLuaDTO);
        }
        List<PassportUserAndGroupDTO> childList = menuList.getChildList();
        for (PassportUserAndGroupDTO everyChild : childList) {
            Integer voTypeChild = everyChild.getVoType();
            if (voTypeChild.equals(2)) {
                UserInfoLuaDTO childUserInfo = new UserInfoLuaDTO();
                childUserInfo.setUserId(everyChild.getUserId());
                childUserInfo.setInfo(JsonUtil.toJsonString(everyChild.toUserInfo()));
                userList.add(childUserInfo);
            } else if (voTypeChild.equals(1) && parentId.equals(menuList.getParentId())) {
                treeList(everyChild, menuList.getGroupId(), userList);
            }
        }
    }

    private List<PassportUserInfoDTO> getLeaderByLevel(Integer userId, Map<Integer, List<List<PassportUserInfoDTO>>> leaderInfo) {
        List<List<PassportUserInfoDTO>> leaderList = leaderInfo.get(userId);
        // 获取上级领导
        List<PassportUserInfoDTO> firstLevel = leaderList.get(0);
        // 领导为自己
        if (firstLevel.stream().anyMatch(user -> user.getId().equals(userId))) {
            if (leaderList.size() == 1) {
                return null;
            }
            return leaderList.get(1);
        }
        return firstLevel;
    }
}

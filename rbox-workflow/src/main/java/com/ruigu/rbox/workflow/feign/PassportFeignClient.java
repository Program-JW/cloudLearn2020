package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.PassportGroupInfoDTO;
import com.ruigu.rbox.workflow.model.dto.PassportUserAndGroupDTO;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.request.DepartmentsAndEmployeesRequest;
import com.ruigu.rbox.workflow.model.request.PassportUserSearchReq;
import com.ruigu.rbox.workflow.model.request.SearchGroupRequest;
import com.ruigu.rbox.workflow.model.vo.*;
import com.ruigu.rbox.workflow.model.vo.lightning.UserDepartmentsAndNameVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/08/26 10:04
 */
@Component
@FeignClient(value = "passport", url = "${rbox.passport.feign.url:}", fallbackFactory = PassportFeignFallback.class)
public interface PassportFeignClient {

    /**
     * 获取用户部门、领导信息
     *
     * @param userIds 用户id
     * @return 用户部门、领导信息
     */
    @RequestMapping(value = "/passport/user/leader", method = RequestMethod.GET)
    ServerResponse<List<UserGroupLeaderVO>> getDeptLeaderInfoList(@RequestParam("userIds") Collection<String> userIds);

    /**
     * 根据用户的ids获取usersMsg
     *
     * @param userIds 用户的id
     * @return 相应的用户信息
     */
    @RequestMapping(value = "/passport/user/get/msg", method = RequestMethod.GET)
    ServerResponse<List<PassportUserInfoDTO>> getUserMsgByIds(@RequestParam("userIds") Collection<Integer> userIds);

    /**
     * 根据用户userId获取id
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @RequestMapping(value = "/passport/user/getUserByUserId", method = RequestMethod.GET)
    ServerResponse<PassportUserInfoDTO> getIdByUserId(@RequestParam("userId") String userId);

    /**
     * 根据关键词获取匹配用户姓名到的userId
     *
     * @param searchKey 关键词
     * @return 匹配到的人员id列表
     */
    @RequestMapping(value = "/passport/user/userIds-name", method = RequestMethod.GET)
    ServerResponse<List<Integer>> getUserIdByUserName(@RequestParam("searchKey") String searchKey);

    /**
     * 批量获取领导信息
     *
     * @param userIdList 用户id列表
     * @return 返回结果
     */
    @RequestMapping(value = "/passport/user/all-leaders-of-the-user", method = RequestMethod.GET)
    ServerResponse<Map<Integer, List<List<PassportUserInfoDTO>>>> getUserAllLeaderInfo(@RequestParam("userIdList") Iterable<Integer> userIdList);

    /**
     * 获取部门人员信息
     *
     * @param map 部门信息
     * @return 返回信息
     */
    @RequestMapping(value = "/passport/user/list", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, method = RequestMethod.POST)
    ServerResponse<TableDataVo> getUserListByGroupId(MultiValueMap<String, Object> map);

    /**
     * 根据用户Ids查询用户所在一级部门，二级部门，姓名
     *
     * @param userIds 用户Ids
     * @return 用户所在一级部门，二级部门，姓名
     */
    @RequestMapping(value = "/passport/user/user-basic-info-ids", method = RequestMethod.POST)
    ServerResponse<List<UserDepartmentsAndNameVO>> queryUserDepartmentsAndName(@RequestBody List<Integer> userIds);

    /**
     * 按级别查部门
     *
     * @param isNeedTopDepartment 是否需要顶级部门
     * @param level               级别
     * @return 数据
     */
    @RequestMapping(value = "/passport/group/departments-level", method = RequestMethod.GET)
    ServerResponse<List<GroupAndUserVO>> getGroupAndUserInfo(@RequestParam(name = "isNeedTopDepartment", required = false) Boolean isNeedTopDepartment,
                                                             @RequestParam(name = "level", required = false) Integer level);

    /**
     * 根据部门id查询部门名称
     *
     * @param groupId 部门id
     * @return 数据
     */
    @RequestMapping(value = "/passport/group/find-group-id", method = RequestMethod.GET)
    ServerResponse<PassportGroupInfoDTO> getGroupById(@RequestParam("groupId") Integer groupId);

    /**
     * 批量查询
     *
     * @param request 请求
     * @return 数据
     */
    @RequestMapping(value = "/passport/group/group-info", method = RequestMethod.GET)
    ServerResponse<List<PassportGroupInfoDTO>> batchGetGroupInfo(@SpringQueryMap SearchGroupRequest request);

    /**
     * 根据部门id查询部门名称
     *
     * @param data 部门id
     * @return 数据
     */
    @RequestMapping(value = "/passport/group/list", method = RequestMethod.GET)
    ServerResponse<List<PassportGroupInfoDTO>> getAllGroup(@SpringQueryMap Map<String, Object> data);

    /**
     * 通讯录接口
     *
     * @param request 请求参数
     * @return 数据
     */
    @RequestMapping(value = "/passport/user/departmentsAndEmployees", method = RequestMethod.GET)
    ServerResponse<List<GroupAndUserAndCountVO>> getDepartmentsAndEmployees(@SpringQueryMap DepartmentsAndEmployeesRequest request);

    /**
     * 判断当前用户是不是领导
     *
     * @param userId 当前用户 id
     * @return 是/否
     */
    @RequestMapping(value = "/passport/user/judge-leader-user-id", method = RequestMethod.GET)
    ServerResponse<Boolean> judgeIsLeader(@RequestParam("userId") Integer userId);

    /**
     * 搜索用户 可以是全公司所有员工
     *
     * @param passportUserSearchReq 搜索参数
     * @return ServerResponse<UserAndGroupFeignInfoDTO>
     */
    @RequestMapping(value = "/passport/user/search-user", method = RequestMethod.POST)
    ServerResponse<PassportUserAndGroupDTO> getUserAndGroup(@RequestBody PassportUserSearchReq passportUserSearchReq);

    /**
     * 通过角色获取用户
     *
     * @param roleCode 角色code
     * @return 用户信息
     */
    @GetMapping(value = "/passport/user/role-code/{roleCode}")
    ServerResponse<List<PassportUserInfoDTO>> getListUserByRoleCode(@PathVariable("roleCode") String roleCode);

    /**
     * 获取第三方用户信息
     *
     * @param userId 用户id
     * @return 第三方用户信息
     */
    @GetMapping(value = "/passport/user/{userId}/extra-info")
    ServerResponse<UserExtraRelationshipVO> getExtraUserInfoByUserId(@PathVariable("userId") Integer userId);


    /**
     * 获取部门下的所有员工ID
     *
     * @param groupIdList 部门ID集合
     * @return 所有员工ID
     */
    @GetMapping("/passport/user/under-the-group")
    ServerResponse<List<Integer>> getUserIdListUnderTheGroup(@RequestParam("groupIdList") Collection<Integer> groupIdList);

}

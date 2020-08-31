package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.PassportGroupInfoDTO;
import com.ruigu.rbox.workflow.model.dto.PassportUserAndGroupDTO;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.request.DepartmentsAndEmployeesRequest;
import com.ruigu.rbox.workflow.model.request.PassportUserSearchReq;
import com.ruigu.rbox.workflow.model.request.SearchGroupRequest;
import com.ruigu.rbox.workflow.model.vo.*;
import com.ruigu.rbox.workflow.model.vo.lightning.UserDepartmentsAndNameVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/09/30 15:38
 */
@Slf4j
@Component
public class PassportFeignFallback implements FallbackFactory<PassportFeignClient> {
    @Override
    public PassportFeignClient create(Throwable throwable) {
        String errMsgHead = "远程调用 - 权限中心 - 异常 - ";
        return new PassportFeignClient() {
            @Override
            public ServerResponse<List<UserGroupLeaderVO>> getDeptLeaderInfoList(Collection<String> userIds) {
                return ServerResponse.fail(500, errMsgHead + "获取部门及领导信息失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<List<PassportUserInfoDTO>> getUserMsgByIds(Collection<Integer> userIds) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(),
                        errMsgHead + "获取用户信息失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<PassportUserInfoDTO> getIdByUserId(String userId) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(),
                        errMsgHead + "通过微信ID获取人员信息失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<List<Integer>> getUserIdByUserName(String searchKey) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(),
                        errMsgHead + "通过用户名称模糊查询获取用户id失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<Map<Integer, List<List<PassportUserInfoDTO>>>> getUserAllLeaderInfo(Iterable<Integer> userIdList) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(),
                        errMsgHead + "获取用户领导信息失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<TableDataVo> getUserListByGroupId(MultiValueMap<String, Object> map) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(),
                        errMsgHead + "通过群组id获取群组用户列表。" + throwable.toString());
            }

            @Override
            public ServerResponse<List<GroupAndUserVO>> getGroupAndUserInfo(Boolean isNeedTopDepartment, Integer level) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(),
                        errMsgHead + "按级别查部门失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<PassportGroupInfoDTO> getGroupById(Integer groupId) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(),
                        errMsgHead + "获取部门信息失败。" + groupId + throwable.toString());
            }

            @Override
            public ServerResponse<List<PassportGroupInfoDTO>> batchGetGroupInfo(SearchGroupRequest request) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(),
                        errMsgHead + "批量获取部门信息失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<List<PassportGroupInfoDTO>> getAllGroup(Map<String, Object> data) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(),
                        errMsgHead + "获取所有部门信息失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<List<GroupAndUserAndCountVO>> getDepartmentsAndEmployees(DepartmentsAndEmployeesRequest request) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(),
                        errMsgHead + "获取通讯录信息失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<Boolean> judgeIsLeader(Integer userId) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(),
                        errMsgHead + "判断当前用户是否是领导失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<PassportUserAndGroupDTO> getUserAndGroup(PassportUserSearchReq passportUserSearchReq) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(),
                        errMsgHead + "搜索用户和部门接口失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<List<PassportUserInfoDTO>> getListUserByRoleCode(String roleCode) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(),
                        errMsgHead + "通过角色code获取用户信息失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<UserExtraRelationshipVO> getExtraUserInfoByUserId(Integer userId) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(),
                        errMsgHead + "获取用户第三方平台信息失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<List<Integer>> getUserIdListUnderTheGroup(Collection<Integer> groupIdList) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), errMsgHead + "根据用户组Id查询该组下的所有用户失败。" + throwable.toString());
            }

            @Override
            public ServerResponse<List<UserDepartmentsAndNameVO>> queryUserDepartmentsAndName(List<Integer> userIds) {
                return ServerResponse.fail(ResponseCode.INTERNAL_ERROR.getCode(), errMsgHead + "根据用户Ids查询用户所在一级部门，二级部门，姓名失败。" + throwable.toString());
            }
        };
    }
}

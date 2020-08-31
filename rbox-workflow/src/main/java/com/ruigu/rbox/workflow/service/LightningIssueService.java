package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.cloud.kanai.web.page.PageImpl;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.LightningIssueIdInfoDTO;
import com.ruigu.rbox.workflow.model.dto.LightningMySolvedDTO;
import com.ruigu.rbox.workflow.model.dto.LightningUnreadMessageDTO;
import com.ruigu.rbox.workflow.model.entity.DutyPlanEntity;
import com.ruigu.rbox.workflow.model.entity.DutyRosterEntity;
import com.ruigu.rbox.workflow.model.entity.LightningIssueApplyEntity;
import com.ruigu.rbox.workflow.model.request.BatchRevokeRequest;
import com.ruigu.rbox.workflow.model.request.DepartmentsAndEmployeesRequest;
import com.ruigu.rbox.workflow.model.request.LightningMyAcceptanceRequest;
import com.ruigu.rbox.workflow.model.request.lightning.*;
import com.ruigu.rbox.workflow.model.vo.GroupAndUserAndCountVO;
import com.ruigu.rbox.workflow.model.vo.LightningIssueCategoryVO;
import com.ruigu.rbox.workflow.model.vo.LightningMyAcceptanceVO;
import com.ruigu.rbox.workflow.model.vo.lightning.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

/**
 * @author caojinghong
 * @date 2019/12/27 14:49
 */
public interface LightningIssueService {
    /**
     * 新建申请，提交问题。若多个受理人，按人创建问题和启动流程
     *
     * @param req 新建问题所需的请求参数
     * @return 返回 ServerResponse
     * @throws Exception 异常
     */
    ServerResponse insertIssueApply(LightningIssueApplyReq req) throws Exception;

    /**
     * 重新提交申请。若多个受理人，按人重新启动流程
     *
     * @param req 重新提交问题所需的请求参数
     * @return 返回重新提交后的问题
     * @throws Exception 异常
     */
    LightningIssueApplyEntity resubmitIssueApply(IssueResubmitReq req) throws Exception;

    /**
     * 申请人确认问题是否解决,同时要保存评价信息
     *
     * @param req 申请人确认问题是否解决,所需参数
     * @return 返回 ServerResponse
     */
    ServerResponse confirmIssue(IssueConfirmReq req);

    /**
     * 系统自动确认问题已解决
     *
     * @param issueId 问题id
     * @return 返回ServerResponse
     */
    ServerResponse systemConfirmSolve(Integer issueId);

    /**
     * 系统自动确认问题已解决
     *
     * @param issueList 问题列表
     */
    void batchSystemConfirmSolve(List<LightningIssueIdInfoDTO> issueList);

    /**
     * 动态查询申请人已提交的列表
     *
     * @param req 查询条件
     * @return PageImpl<LightningMySolvedDTO> 查询的结果并分页
     */
    PageImpl<LightningMySolvedDTO> listMySubmitted(QueryMySubmittedReq req);


    /**
     * 动态查询我受理列表
     *
     * @param request 请求参数
     * @return 我受理列表数据
     */
    Page<LightningMyAcceptanceVO> listMyAcceptance(LightningMyAcceptanceRequest request);

    /**
     * 查询问题详情
     *
     * @param issueId 问题id
     * @return ServerResponse<LightningIssueItemVO> 返回查询问题详情实体
     * @throws Exception 异常
     */
    ServerResponse<LightningIssueItemVO> getIssueItem(Integer issueId) throws Exception;

    /**
     * 通过问题Id查询该问题的所有受理人
     *
     * @param issueId 问题id
     * @return 返回受理人信息集合
     */
    ServerResponse<List<LightningUserInfoVO>> listSolvers(Integer issueId);

    /**
     * 领导邀请人并由系统交接
     *
     * @param issueId   问题id
     * @param addUserId 被邀请人id
     * @return ServerResponse
     * @throws Exception 异常
     */
    ServerResponse leaderInvite(Integer issueId, Integer addUserId) throws Exception;

    /**
     * 交接问题
     *
     * @param req 交接问题所需参数
     * @return 返回 ServerResponse
     * @throws Exception 异常
     */
    ServerResponse transferIssueToAssignee(IssueTransferReq req) throws Exception;

    /**
     * 已解决问题
     *
     * @param req 处理人已解决所需参数
     * @return 返回 ServerResponse
     * @throws Exception 异常
     */
    ServerResponse finishIssue(IssueResolvedReq req) throws Exception;

    /**
     * 保存评价信息
     *
     * @param req 保存评价信息所需参数
     * @return 返回 ServerResponse
     */
    ServerResponse evaluateIssue(IssueEvaluateReq req);

    /**
     * 查询当日值班人员
     *
     * @return 返回当日值班人员信息实体
     */
    DutyRosterEntity getCurrentDutyRoster();

    /**
     * 查询技术部当日值班人员
     *
     * @return 当日值班人员
     */
    DutyPlanEntity getTodayDutyPlan();

    /**
     * 通讯录
     *
     * @param request 请求参数
     * @return 人员列表
     */
    List<GroupAndUserAndCountVO> getAddressBook(DepartmentsAndEmployeesRequest request);

    /**
     * 撤销问题
     *
     * @param request 批量撤销问题请求
     * @return 错误列表
     */
    List<Integer> batchRevokeIssue(BatchRevokeRequest request);

    /**
     * 催办问题
     *
     * @param issueId 问题id
     * @return 返回结果
     */
    ServerResponse urgeIssue(Integer issueId);

    /**
     * 获取当前用户的群id集合
     *
     * @return 返回当前用户的群id集合和用户信息
     */
    ServerResponse<LightningUsersGroupIdsVO> getGroupIds();

    /**
     * 搜索当前用户参与的问题
     *
     * @param req 查询条件
     * @return 查询的结果支持分页和不分页
     */
    Page<LightningMyParticipatedVO> getParticipatedList(QueryMySubmittedReq req);

    /**
     * 查询领导可看的超时问题的列表
     *
     * @param request 查询条件
     * @return 查询的结果支持分页和不分页
     */
    Page<LightningMyAcceptanceVO> listLeaderOverTime(LightningMyAcceptanceRequest request);

    /**
     * 维护问题受理人列表
     *
     * @param issueId 问题id
     * @param userId  人员id
     */
    void addUser(Integer issueId, Integer userId);

    /**
     * 通知未读消息
     *
     * @param unreadMessage 未读消息通知请求
     * @return 发送结果
     */
    void unreadMessageNotice(LightningUnreadMessageDTO unreadMessage);

    /**
     * 用户忽略预删除的问题
     *
     * @param issueId 问题id
     * @return ServerResponse
     */
    ServerResponse deleteIssue(Integer issueId);

    /**
     * 将离职人员的问题交接给其上一级领导
     *
     * @param userId 企业微信userId
     * @throws Exception 异常
     */
    void userLeaveOffice(String userId) throws Exception;


    /**
     * 根据群组列表查询相应问题
     *
     * @param req 请求实体
     * @return 列表信息
     */
    Page<LightningMyAcceptanceVO> getIssueItemByGroupId(QueryByGroupReq req);

    /**
     * 根据问题获取群成员相关信息
     *
     * @param issueId 问题Id
     * @return 群成员信息列表
     */
    List<LightningIssueRelevantUserVO> getRelevantUserByIssueId(Integer issueId);

    /**
     * 获取问题分类列表
     *
     * @param includeAvatar 是否包含头像信息
     * @return 问题分类列表
     */
    List<LightningIssueCategoryVO> selectCategory(boolean includeAvatar);

    /**
     * 清除用户缓存
     *
     * @param userIds 用户id列表
     */
    void clearRedisCache(List<Integer> userIds);

    /**
     * 查询用户缓存
     *
     * @param userId 用户id
     * @return Redis缓存我受理列表
     */
    Set<Integer> queryRedisMyAcceptCache(Integer userId);

    /**
     * 关闭自动确认问题 （定时任务调用）
     */
    void closeAutoConfirmIssue();
}

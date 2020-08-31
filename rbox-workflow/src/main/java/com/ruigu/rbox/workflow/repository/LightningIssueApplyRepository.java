package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.dto.LightningIssueCountDTO;
import com.ruigu.rbox.workflow.model.dto.LightningIssueIdInfoDTO;
import com.ruigu.rbox.workflow.model.entity.LightningIssueApplyEntity;
import com.ruigu.rbox.workflow.model.request.LightningMyAcceptanceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author caojinghong
 * @date 2019/12/27 15:19
 */
public interface LightningIssueApplyRepository extends JpaRepository<LightningIssueApplyEntity, Integer>, JpaSpecificationExecutor<LightningIssueApplyEntity> {

    /**
     * 动态查询问题
     *
     * @param request  请求参数实体
     * @param userId   当前操作人
     * @param pageable 分页参数
     * @return 问题
     */
    @Query("select a " +
            " from LightningIssueApplyEntity a " +
            " left join LightningIssueLogEntity l on a.id = l.issueId " +
            " where " +
            " l.action in (1,2,3) and l.createdBy = :userId " +
            " and " +
            " ( " +
            " a.description like concat('%',:#{#request.keyword},'%') " +
            " or a.createdBy in (:#{#request.userIds}) " +
            " or a.currentSolverId in (:#{#request.userIds}) " +
            " or :#{#request.keyword} is null " +
            " ) " +
            " and " +
            " ( " +
            " a.description like concat('%',:#{#request.keyword},'%') " +
            " or a.createdBy in (:#{#request.userIds}) " +
            " or a.currentSolverId in (:#{#request.userIds}) " +
            " or :#{#request.userIds} is null " +
            " ) ")
    Page<LightningIssueApplyEntity> findMyAcceptedApply(@Param("request") LightningMyAcceptanceRequest request, @Param("userId") Integer userId, Pageable pageable);


    /**
     * 动态查询已提交列表
     *
     * @param keyWord  搜索的关键字（描述字段）
     * @param userIds  搜索的关键字（针对人的时候如果查到的话就是人的id）
     * @param userId   用户Id
     * @param pageable 分页参数
     * @return 已提交列表查询结果
     */
    @Query(value = "select a.id 'issueId',a.current_solver_id 'currentSolverId',a.status,a.is_auto_confirm 'autoConfirm',a.description,a.last_updated_on 'lastUpdatedOn',g.group_id 'groupId',g.group_name 'groupName'  " +
            " from lightning_issue_apply a left join  lightning_issue_group g on a.id=g.issue_id " +
            " where a.created_by= ?3 " +
            " and (a.description LIKE CONCAT( '%', ?1, '%' ) OR a.current_solver_id IN ( ?2 ) OR ?1 IS NULL) " +
            " and (a.status in (?4) or ( a.status = 4 and a.is_auto_confirm = ?5 )) " +
            " order by a.status asc, a.last_updated_on desc "
            , nativeQuery = true,
            countQuery = "select count(1) from (select a.id 'issueId',a.current_solver_id 'currentSolverId',a.status,a.description,a.last_updated_on 'lastUpdatedOn',g.group_id 'groupId',g.group_name 'groupName'  " +
                    " from lightning_issue_apply a left join  lightning_issue_group g on a.id=g.issue_id" +
                    " where a.created_by= ?3 " +
                    " and (a.description LIKE CONCAT( '%', ?1, '%' ) OR a.current_solver_id IN ( ?2 ) OR ?1 IS NULL) " +
                    " and (a.status in (?4) or ( a.status = 4 and a.is_auto_confirm = ?5 )) " +
                    " order by a.status asc, a.last_updated_on desc) t")
    Page<Map> queryMySubmitted(String keyWord, List<Integer> userIds, Integer userId, List<Integer> statusList, Integer isAuto, Pageable pageable);

    /**
     * 报表-查询整体数据
     *
     * @param startTime 查询的开始时间
     * @param endTime   查询的结束时间
     * @param pageable  分页参数
     * @return 报表查询数据
     */
    @Query(value = "select count(if(a.`status` = 7, null, a.id)) 'issueCount', " +
            " sum(if(e.score, e.score, 0)) 'evaluateScoreCount', " +
            " sum(if(a.is_demand = 1, 1, 0)) 'issueDemandCount' " +
            " from lightning_issue_apply a " +
            " left join lightning_issue_evaluation e on e.issue_id = a.id " +
            " where a.created_on BETWEEN ?1 and ?2 ", nativeQuery = true,
            countQuery = "select count(1) from (select count(a.id) 'issueCount', " +
                    " sum(if(e.score, e.score, 0)) 'evaluateScoreCount', " +
                    " sum(if(a.is_demand = 1, 1, 0)) 'issueDemandCount' " +
                    " from lightning_issue_apply a " +
                    " left join lightning_issue_evaluation e on e.issue_id = a.id " +
                    " where a.created_on BETWEEN ?1 and ?2) b")
    Page<Map> queryWholeData(Date startTime, Date endTime, Pageable pageable);

    /**
     * 报表-按部门分
     *
     * @param startTime 查询的开始时间
     * @param endTime   查询的结束时间
     * @param pageable  分页参数
     * @return 报表查询数据
     */
    @Query(value = "select 0                       'firstLevelDepartmentName', " +
            " 0                             'secondLevelDepartmentName', " +
            " a.issue_department_id         'secondLevelDepartmentId', " +
            " count(a.id)                   'secondLevelIssueCount' " +
            " from lightning_issue_apply a " +
            " where a.issue_department_id is not null and a.created_on BETWEEN ?1 and ?2 " +
            " group by a.issue_department_id", nativeQuery = true,
            countQuery = " select count(1) from( " +
                    "select 0                       'firstLevelDepartmentName', " +
                    " 0                             'secondLevelDepartmentName', " +
                    " a.issue_department_id         'secondLevelDepartmentId', " +
                    " count(a.id)                   'secondLevelIssueCount' " +
                    " from lightning_issue_apply a " +
                    " where a.issue_department_id is not null and a.created_on BETWEEN ?1 and ?2 " +
                    " group by a.issue_department_id) t")
    Page<Map> queryDepartmentData(Date startTime, Date endTime, Pageable pageable);


    /**
     * 获取问题信息通过ids和状态
     *
     * @param idList     问题id列表
     * @param statusList 状态列表
     * @param createdBy  创建人
     * @return 查询的信息
     */
    List<LightningIssueApplyEntity> findAllByIdInAndStatusNotInAndCreatedBy(List<Integer> idList, List<Integer> statusList, Integer createdBy);

    /**
     * 获取问题数
     *
     * @param userIdList 用户id列表
     * @return 返回
     */
    @Query("select a.currentSolverId as userId,count(a.id) as count" +
            " from LightningIssueApplyEntity a " +
            " where a.status not in (4,7) and a.currentSolverId in (:userList) " +
            " group by a.currentSolverId")
    List<LightningIssueCountDTO> findUserIssueCount(@Param("userList") List<Integer> userIdList);

    /**
     * 根据问题id们 更新问题表相关字段（该方法主要针对批量改系统自动确认）
     *
     * @param autoConfirm   系统是否自动确认 1 是 0 否
     * @param status        问题状态
     * @param lastUpdatedBy 最后更新人 -2表示系统
     * @param time          最后更新时间
     * @param issueIds      要更新的问题id们
     */
    @Query("update LightningIssueApplyEntity a set a.autoConfirm = ?1, a.status = ?2, a.lastUpdatedBy= ?3, a.lastUpdatedOn= ?4 where a.id in ( ?5 )")
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void updateApplyByIds(Integer autoConfirm, Integer status, Integer lastUpdatedBy, Date time, List<Integer> issueIds);

    /**
     * 批量修改自动状态以及最后更新时间
     *
     * @param issueIds      问题id列表
     * @param autoConfirm   自动确认标志
     * @param lastUpdatedOn 最后更新时间
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update LightningIssueApplyEntity a set a.autoConfirm = :autoConfirm , a.lastUpdatedOn = :lastUpdatedOn where a.id in (:issueIds)")
    void updateApplyAutoConfirm(@Param("issueIds") List<Integer> issueIds, @Param("autoConfirm") Integer autoConfirm, @Param("lastUpdatedOn") Date lastUpdatedOn);

    /**
     * 根据问题状态查询问题
     *
     * @param status 问题状态
     * @return 问题列表
     */
    List<LightningIssueApplyEntity> findAllByStatus(Integer status);

    /**
     * 分页查询
     *
     * @param status   状态
     * @param pageable 分页
     * @return 分页数据
     */
    Page<LightningIssueApplyEntity> findAllByStatus(Integer status, Pageable pageable);

    /**
     * 根据问题状态查询问题
     *
     * @param statusList 状态列表
     * @return 问题列表
     */
    List<LightningIssueApplyEntity> findAllByStatusIn(List<Integer> statusList);

    /**
     * 通过群组id获取问题详情
     *
     * @param groupId 群组id
     * @return 问题数据
     */
    @Query("select a " +
            " from LightningIssueApplyEntity a left join LightningIssueGroupEntity g on a.id = g.issueId " +
            " where g.groupId = :groupId ")
    LightningIssueApplyEntity findByGroupId(@Param("groupId") String groupId);

    /**
     * 判断预删除的问题是否存在并且是否是已撤销/已解决状态
     *
     * @param issueId     问题id
     * @param autoConfirm 是否系统自动确认
     * @param status      状态
     * @return LightningIssueApplyEntity实体
     */
    Optional<LightningIssueApplyEntity> findFirstByIdAndAutoConfirmAndStatusIn(Integer issueId, Integer autoConfirm, List<Integer> status);

    /**
     * 获取需要自动确认的问题
     *
     * @return 自动确认的问题
     */
    @Query(value = " select a.id as issueId,a.instance_id as instanceId,a.created_by as createdBy from " +
            " (select id,instance_id,created_by from lightning_issue_apply where status = 3) a " +
            " left join (select id,issue_id from lightning_issue_log where action = 9) l on a.id = l.issue_id " +
            " where l.id is null limit 500 ", nativeQuery = true)
    List<LightningIssueIdInfoDTO> findAllNeedAutoConfirmIssue();
}

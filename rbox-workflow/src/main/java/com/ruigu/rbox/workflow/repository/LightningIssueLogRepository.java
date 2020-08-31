package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.LightningIssueLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author caojinghong
 * @date 2019/12/31 17:11
 */
public interface LightningIssueLogRepository extends JpaRepository<LightningIssueLogEntity, Integer>, JpaSpecificationExecutor<LightningIssueLogEntity> {
    /**
     * 根据问题id查询日志问题详情的操作日志记录
     * @param issueId 问题id
     * @return List<LightningIssueLogEntity> 日志记录Entity的集合
     */
    List<LightningIssueLogEntity> findAllByIssueId(Integer issueId);

    /**
     * 根据问题id和操作人id判断最佳处理人是否为申请流程中存在的用户
     * @param issueId 问题id
     * @param userId  操作人id
     * @return List<LightningIssueLogEntity> 日志记录Entity的集合
     */
    List<LightningIssueLogEntity> findAllByIssueIdAndCreatedBy(Integer issueId, Integer userId);

    /**
     * 根据问题id查询日志问题详情的操作日志记录(除了超时记录)
     * @param issueId 问题id
     * @param actions 超时记录的action
     * @return List<LightningIssueLogEntity> 日志记录Entity的集合
     */
    List<LightningIssueLogEntity> findAllByIssueIdAndActionNotIn(Integer issueId, List<Integer> actions);

    /**
     * 按人查询报表 （新）
     * @param startTime 查询的开始时间
     * @param endTime 查询的结束时间
     * @param pageable 分页参数
     * @return 报表查询数据
     */
    @Query(value = "select " +
            "       0                                                                   'firstLevelDepartmentName', " +
            "       0                                                                   'secondLevelDepartmentName', " +
            "       0                                                                   'personName', " +
            "       log.created_by                                                      'userId', " +
            "       sum(if(log.action = 0, 1, 0))                                       'applyIssueCount', " +
            "       sum(if(log.action = 6 and a.status = 7, 1, 0))                     'revockIssueCount', " +
            "       count(DISTINCT if((log.action = 1 or log.action = 10 )and a.status != 7, a.id, null))    'acceptIssueCount', " +
            "       count(DISTINCT if(a.current_solver_id = log.created_by and (log.action = 1 or log.action = 10) and a.status not in (4,7), a.id, null)) 'currentAcceptIssueCount', "+
            "       count(DISTINCT if(e.best_person_id = log.created_by, a.id, null))    'consideredBestCount', " +
            "       count(DISTINCT if(a.current_solver_id = log.created_by and a.status = 4, a.id, null))  'finishAndConfirmCount', " +
            "       count(DISTINCT if(log.action = 2, a.id, null))                      'transferIssueCount', " +
            "       count(DISTINCT if(log.action = 7, a.id, null))                      'firstLevelPromotedCount', " +
            "       count(DISTINCT if(log.action = 8, a.id, null))                      'secondLevelPromotedCount', " +
            "       count(DISTINCT if(log.action = 9, a.id, null))                      'thirdLevelPromotedCount' " +
            " from lightning_issue_log log " +
            "         left join lightning_issue_apply a on log.issue_id = a.id " +
            "         left join lightning_issue_evaluation e on e.issue_id = a.id " +
            " where a.created_on BETWEEN ?1 AND ?2 " +
            " group by log.created_by",nativeQuery = true)
    Page<Map> queryReportFormNew(Date startTime, Date endTime, Pageable pageable);

    /**
     * 按人查询报表 （旧）
     * @param startTime 查询的开始时间
     * @param endTime 查询的结束时间
     * @param pageable 分页参数
     * @return 报表查询数据
     */
    @Query(value = "select " +
            "       0                                                                   'firstLevelDepartmentName', " +
            "       0                                                                   'secondLevelDepartmentName', " +
            "       0                                                                   'personName', " +
            "       log.created_by                                                      'userId', " +
            "       sum(if(log.action = 0, 1, 0))                                       'applyIssueCount', " +
            "       sum(if(log.action = 6 and a.status = 7, 1, 0))                     'revockIssueCount', " +
            "       count(DISTINCT if(log.action = 1, a.id, null))                      'acceptIssueCount', " +
            "       count(DISTINCT if(e.best_person_id = log.created_by, a.id, null))    'consideredBestCount', " +
            "       count(DISTINCT if(a.current_solver_id = log.created_by and a.status = 4, a.id, null))  'finishAndConfirmCount', " +
            "       count(DISTINCT if(log.action = 2, a.id, null))                      'transferIssueCount', " +
            "       count(DISTINCT if(log.action = 7, a.id, null))                      'firstLevelPromotedCount', " +
            "       count(DISTINCT if(log.action = 8, a.id, null))                      'secondLevelPromotedCount', " +
            "       count(DISTINCT if(log.action = 9, a.id, null))                      'thirdLevelPromotedCount' " +
            " from lightning_issue_log log " +
            "         left join lightning_issue_apply a on log.issue_id = a.id " +
            "         left join lightning_issue_evaluation e on e.issue_id = a.id " +
            " where a.created_on BETWEEN ?1 AND ?2 " +
            " group by log.created_by",nativeQuery = true)
    Page<Map> queryReportForm(Date startTime, Date endTime, Pageable pageable);
    /**
     * 获取问题的超时记录
     *
     * @param issueIdList 问题id列表
     * @param actionList  状态列表
     * @return 超时记录
     */
    List<LightningIssueLogEntity> findAllByIssueIdInAndActionIn(List<Integer> issueIdList, List<Integer> actionList);
}

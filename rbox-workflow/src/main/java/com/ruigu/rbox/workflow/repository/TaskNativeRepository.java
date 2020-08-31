package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.cloud.kanai.util.TimeUtil;
import com.ruigu.rbox.workflow.model.entity.BusinessParamEntity;
import com.ruigu.rbox.workflow.model.enums.SqlConditionType;
import com.ruigu.rbox.workflow.model.request.MyTaskRequest;
import com.ruigu.rbox.workflow.model.vo.TaskVO;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/08/23 15:51
 */
@Component
@Repository
public class TaskNativeRepository {

    @Resource
    private EntityManager entityManager;

    public List<TaskVO> selectUnfinishedTask() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT id,name,instance_id instanceId, ")
                .append(" candidate_users candidateUsers,candidate_groups candidateGroups,due_time dueTime,created_on createdOn ")
                .append(" FROM task ")
                .append(" WHERE status < 2 ")
                .append(" AND ( ")
                .append(" SELECT is_working_day ")
                .append(" FROM work_day ")
                .append(" WHERE date_of_year = DATE_FORMAT(SYSDATE(),'%Y-%m-%d') ")
                .append(" )=1 ");
        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        dataQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(TaskVO.class));
        return dataQuery.getResultList();
    }

    public Page<TaskVO> selectAllTaskByCandidateId(Integer userId, MyTaskRequest req, List<BusinessParamEntity> businessParams) {

        StringBuilder businessSql = new StringBuilder(" ");
        if (CollectionUtils.isNotEmpty(businessParams)) {
            businessSql.append(" INNER JOIN (");
            businessSql.append(buildBusinessSql(businessParams));
            businessSql.append(")b ");
            businessSql.append(" ON i.id = b.instance_id ");
        }

        boolean flag = req.getStatus() == null ? false : true;
        String statusSql = flag ? " AND t.status in (:status) " : "";

        StringBuilder dataSql = new StringBuilder();
        dataSql.append(" SELECT t.id,t.name,t.summary,t.remarks,t.instance_id AS instanceId,i.name AS instanceName,t.`status`,t.created_on AS createdOn,i.created_by AS instanceCreatedBy,i.business_key AS orderNumber ")
                .append(" FROM task t INNER JOIN ")
                .append(" (SELECT id,name,business_key,definition_id,definition_code,created_by from workflow_instance where status>0 UNION ALL SELECT id,name,business_key,definition_id,definition_code,created_by FROM workflow_history where status>0)i ")
                .append(" ON t.instance_id = i.id ")
                .append(businessSql)
                .append(" WHERE ")
                .append(" ((SELECT FIND_IN_SET(:userId,t.candidate_users)>0) ")
                .append(" OR ")
                .append(" (:userId IN (SELECT user_id FROM user_group u LEFT JOIN user_group_asso a ON u.id = a.group_id WHERE (SELECT FIND_IN_SET(u.id,t.candidate_groups))>0))) ")
                .append(" AND ( t.remarks like :keyWord OR :keyWord is NULL ) ")
                .append(" AND ( i.definition_id = :definitionId OR :definitionId is NULL ) ")
                .append(" AND ( i.definition_code = :definitionCode OR :definitionCode is NULL ) ")
                .append(" AND ( t.created_on >= :begin OR :begin is NULL ) ")
                .append(" AND ( t.created_on <= :end OR :end is NULL ) ")
                .append(statusSql)
                .append(" ORDER BY t.created_on DESC ");

        Query dataQuery = entityManager.createNativeQuery(dataSql.toString());
        dataQuery.setParameter("userId", userId);
        dataQuery.setParameter("keyWord", req.getKeyWord());
        dataQuery.setParameter("definitionId", req.getDefinitionId());
        dataQuery.setParameter("definitionCode", req.getDefinitionCode());
        dataQuery.setParameter("begin", req.getBegin());
        dataQuery.setParameter("end", req.getEnd());
        if (flag) {
            dataQuery.setParameter("status", req.getStatus());
        }
        dataQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(TaskVO.class));
        dataQuery.setFirstResult((req.getPageNum()) * 10);
        dataQuery.setMaxResults(10);
        List<TaskVO> resultList = dataQuery.getResultList();

        StringBuilder countSql = new StringBuilder();
        countSql.append(" SELECT count(*) ")
                .append(" FROM task t LEFT JOIN ")
                .append(" (SELECT id,name,business_key,definition_id from workflow_instance where status>0 UNION ALL SELECT id,name,business_key,definition_id FROM workflow_history where status>0)i ")
                .append(" ON t.instance_id = i.id ")
                .append(businessSql)
                .append(" WHERE ")
                .append(" ((SELECT FIND_IN_SET(:userId,t.candidate_users)>0) ")
                .append(" OR ")
                .append(" (:userId IN (SELECT user_id FROM user_group u LEFT JOIN user_group_asso a ON u.id = a.group_id WHERE (SELECT FIND_IN_SET(u.id,t.candidate_groups))>0))) ")
                .append(" AND ( t.remarks like :keyWord OR :keyWord is NULL ) ")
                .append(" AND ( i.definition_id = :definitionId OR :definitionId is NULL ) ")
                .append(" AND ( t.created_on >= :begin OR :begin is NULL ) ")
                .append(" AND ( t.created_on <= :end OR :end is NULL ) ")
                .append(statusSql);

        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        countQuery.setParameter("userId", userId);
        countQuery.setParameter("keyWord", req.getKeyWord());
        countQuery.setParameter("definitionId", req.getDefinitionId());
        countQuery.setParameter("begin", req.getBegin());
        countQuery.setParameter("end", req.getEnd());
        if (flag) {
            countQuery.setParameter("status", req.getStatus());
        }

        Long count = Long.valueOf(countQuery.getSingleResult().toString());
        Page<TaskVO> page = new PageImpl<>(resultList, PageRequest.of(req.getPageNum(), 10), count);
        return page;
    }

    public List<TaskVO> selectTaskInstanceList(Integer candidateId) {
        StringBuilder dataSql = new StringBuilder();
        dataSql.append(" SELECT t.instance_id as instanceId,i.name as instanceName ")
                .append(" FROM task t INNER JOIN workflow_instance i ON t.instance_id = i.id ")
                .append(" WHERE ")
                .append(" ((SELECT FIND_IN_SET(:userId,t.candidate_users)>0) ")
                .append(" OR ")
                .append(" (:userId in (SELECT user_id FROM user_group u LEFT JOIN user_group_asso a ON u.id = a.group_id WHERE (SELECT FIND_IN_SET(u.id,t.candidate_groups))>0))) ");
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString());
        dataQuery.setParameter("userId", candidateId);
        dataQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(TaskVO.class));
        return dataQuery.getResultList();
    }

    public List<TaskVO> selectHisTaskInstanceList(Integer candidateId) {
        StringBuilder dataSql = new StringBuilder();
        dataSql.append(" SELECT t.instance_id as instanceId,i.name as instanceName  ")
                .append(" FROM task t INNER JOIN workflow_history i ON t.instance_id = i.id ")
                .append(" WHERE ")
                .append(" ((SELECT FIND_IN_SET(:userId,t.candidate_users)>0) ")
                .append(" OR ")
                .append(" (:userId in (SELECT user_id FROM user_group u LEFT JOIN user_group_asso a ON u.id = a.group_id WHERE (SELECT FIND_IN_SET(u.id,t.candidate_groups))>0))) ");
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString());
        dataQuery.setParameter("userId", candidateId);
        dataQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(TaskVO.class));
        return dataQuery.getResultList();
    }

    public Map<String, Object> selectMonthWorkloadData(Integer year, Integer month, Integer userId) {
        LocalDate monthDate = LocalDate.of(year, month, 1);
        Map<String, Object> statisticsData = new HashMap<>(8);
        /**
         * 用户需要处理的
         */
        String workTaskSql = " SELECT " +
                " WEEK(t.created_on,1) workWeek, " +
                " t.status, " +
                " count(t.id) count " +
                " FROM( " +
                " SELECT " +
                " id, " +
                " `status`, " +
                " candidate_users, " +
                " candidate_groups, " +
                " created_on " +
                " FROM " +
                " task " +
                " where " +
                " ((SELECT FIND_IN_SET(:userId,candidate_users)>0) " +
                " OR " +
                " (:userId IN (SELECT user_id FROM user_group u LEFT JOIN user_group_asso a ON u.id = a.group_id WHERE (SELECT FIND_IN_SET(u.id,candidate_groups))>0))) " +
                " AND DATE_FORMAT(created_on,'%Y-%m') = DATE_FORMAT(:month,'%Y-%m') " +
                " )t " +
                " GROUP BY WEEK(t.created_on,1),t.status " +
                " ORDER BY WEEK(t.created_on,1) ";
        Query workTaskQuery = entityManager.createNativeQuery(workTaskSql);
        workTaskQuery.setParameter("userId", userId);
        workTaskQuery.setParameter("month", TimeUtil.localDate2Date(monthDate));
        workTaskQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> workTaskData = workTaskQuery.getResultList();
        statisticsData.put("work", workTaskData);

        /**
         * 用户需要创建的
         */
        String createInstanceSql = " SELECT " +
                " WEEK(t.created_on,1) workWeek," +
                " count(t.id) count " +
                " FROM " +
                " (SELECT id,created_on from workflow_instance WHERE created_by = :userId AND DATE_FORMAT(created_on,'%Y-%m') = DATE_FORMAT(:month,'%Y-%m') " +
                " UNION ALL " +
                " SELECT id,created_on from workflow_history WHERE created_by = :userId AND DATE_FORMAT(created_on,'%Y-%m') = DATE_FORMAT(:month,'%Y-%m'))t" +
                " GROUP BY WEEK(t.created_on,1)" +
                " ORDER BY WEEK(t.created_on,1) ";
        Query createInstanceQuery = entityManager.createNativeQuery(createInstanceSql);
        createInstanceQuery.setParameter("userId", userId);
        createInstanceQuery.setParameter("month", TimeUtil.localDate2Date(monthDate));
        createInstanceQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> createInstanceData = createInstanceQuery.getResultList();
        statisticsData.put("create", createInstanceData);

        /**
         * 超时的
         */
        String timeoutTaskSql = " SELECT " +
                " WEEK(t.created_on,1) workWeek, " +
                " count(t.id) count " +
                " FROM " +
                " (SELECT id,created_on FROM notice WHERE type=0 AND DATE_FORMAT(created_on,'%Y-%m') = DATE_FORMAT(:month,'%Y-%m') AND (SELECT FIND_IN_SET(:userId,targets)>0))t " +
                " GROUP BY WEEK(t.created_on,1) " +
                " ORDER BY WEEK(t.created_on,1) ";
        Query timeoutTaskQuery = entityManager.createNativeQuery(timeoutTaskSql);
        timeoutTaskQuery.setParameter("userId", userId);
        timeoutTaskQuery.setParameter("month", TimeUtil.localDate2Date(monthDate));
        timeoutTaskQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> timeoutTaskData = timeoutTaskQuery.getResultList();
        statisticsData.put("timeout", timeoutTaskData);

        return statisticsData;
    }

    public Map<String, Object> selectYearWorkloadData(Integer year, Integer userId) {
        Map<String, Object> statisticsData = new HashMap<>(8);
        /**
         * 用户需要处理的
         */
        String workTaskSql = " SELECT " +
                " MONTH(t.created_on) workMonth, " +
                " t.status, " +
                " count(t.id) count " +
                " FROM( " +
                " SELECT " +
                " id, " +
                " `status`, " +
                " candidate_users, " +
                " candidate_groups, " +
                " created_on " +
                " FROM " +
                " task " +
                " where " +
                " ((SELECT FIND_IN_SET(:userId,candidate_users)>0) " +
                " OR " +
                " (:userId IN (SELECT user_id FROM user_group u LEFT JOIN user_group_asso a ON u.id = a.group_id WHERE (SELECT FIND_IN_SET(u.id,candidate_groups))>0))) " +
                " AND YEAR(created_on) = :year " +
                " )t " +
                " GROUP BY MONTH(t.created_on),t.status " +
                " ORDER BY MONTH(t.created_on) ";
        Query workTaskQuery = entityManager.createNativeQuery(workTaskSql);
        workTaskQuery.setParameter("userId", userId);
        workTaskQuery.setParameter("year", year);
        workTaskQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> workTaskData = workTaskQuery.getResultList();
        statisticsData.put("work", workTaskData);

        /**
         * 用户需要创建的
         */
        String createInstanceSql = " SELECT " +
                " MONTH(t.created_on) workMonth, " +
                " count(t.id) count " +
                " FROM " +
                " (SELECT id,created_on from workflow_instance WHERE created_by = :userId AND YEAR(created_on) = :year " +
                " UNION ALL " +
                " SELECT id,created_on from workflow_history WHERE created_by = :userId AND YEAR(created_on) = :year)t" +
                " GROUP BY MONTH(t.created_on) " +
                " ORDER BY WEEK(t.created_on) ";
        Query createInstanceQuery = entityManager.createNativeQuery(createInstanceSql);
        createInstanceQuery.setParameter("userId", userId);
        createInstanceQuery.setParameter("year", year);
        createInstanceQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> createInstanceData = createInstanceQuery.getResultList();
        statisticsData.put("create", createInstanceData);

        /**
         * 超时的
         */
        String timeoutTaskSql = " SELECT " +
                " MONTH(t.created_on) workMonth, " +
                " count(t.id) count " +
                " FROM " +
                " (SELECT id,created_on FROM notice WHERE type=0 AND YEAR(created_on) = :year AND (SELECT FIND_IN_SET(:userId,targets)>0))t " +
                " GROUP BY MONTH(t.created_on) " +
                " ORDER BY MONTH(t.created_on) ";
        Query timeoutTaskQuery = entityManager.createNativeQuery(timeoutTaskSql);
        timeoutTaskQuery.setParameter("userId", userId);
        timeoutTaskQuery.setParameter("year", year);
        timeoutTaskQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> timeoutTaskData = timeoutTaskQuery.getResultList();
        statisticsData.put("timeout", timeoutTaskData);

        return statisticsData;
    }

    private String buildBusinessSql(List<BusinessParamEntity> businessParams) {
        if (CollectionUtils.isEmpty(businessParams)) {
            return null;
        }
        StringBuilder businessSql = new StringBuilder();
        StringBuilder paramSql = new StringBuilder();
        StringBuilder havingSql = new StringBuilder();
        businessParams.forEach(param -> {
            String key = param.getParamKey();
            String value = param.getParamValue();
            Integer way = param.getFindWay();
            paramSql.append(" GROUP_CONCAT(CASE param_key WHEN '" + key + "' THEN param_value ELSE NULL END) '" + key + "' ,");
            if (SqlConditionType.EQUALS.getCode() == way) {
                havingSql.append(" and " + key + " = " + value);
            } else if (SqlConditionType.LIKE.getCode() == way) {
                havingSql.append(" and " + key + " like " + "'%" + value + "%'");
            }
        });
        businessSql.append(" SELECT ")
                .append(paramSql)
                .append(" instance_id ")
                .append(" FROM business_param GROUP BY instance_id ")
                .append(" HAVING 1=1 ")
                .append(havingSql);
        return businessSql.toString();
    }
}

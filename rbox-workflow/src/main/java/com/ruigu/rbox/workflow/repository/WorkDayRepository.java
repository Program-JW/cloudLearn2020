package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.WorkDayEntity;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liqingtian
 * @date 2019/08/22 15:53
 */
@Repository
public class WorkDayRepository {

    @Autowired
    private EntityManager entityManager;

    public List<WorkDayEntity> getUpToDate(LocalDateTime createTime, Integer days) {

        String sql = "SELECT date_of_year dateOfYear " +
                "FROM work_day " +
                "WHERE is_working_day = 1 " +
                "AND date_of_year > DATE_FORMAT(:createTime,'%Y-%m-%d') " +
                "ORDER BY date_of_year LIMIT :days";
        Query dataQuery = entityManager.createNativeQuery(sql);
        dataQuery.setParameter("createTime", createTime);
        dataQuery.setParameter("days", days);
        dataQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(WorkDayEntity.class));
        return dataQuery.getResultList();

    }

    public long getSubDays(LocalDateTime start, LocalDateTime end) {

        String sql = "SELECT count(*) " +
                "FROM work_day " +
                "WHERE is_working_day = 1 " +
                "AND date_of_year > DATE_FORMAT(:start,'%Y-%m-%d') " +
                "AND date_of_year <= DATE_FORMAT(:endTime,'%Y-%m-%d')";

        Query countQuery = entityManager.createNativeQuery(sql);
        countQuery.setParameter("start", start);
        countQuery.setParameter("endTime", end);

        return Long.valueOf(String.valueOf(countQuery.getSingleResult()));
    }
}

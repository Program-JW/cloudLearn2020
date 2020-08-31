package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.WorkflowDefinitionEntity;
import com.ruigu.rbox.workflow.model.request.SearchDefinitionRequest;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author liqingtian
 * @date 2019/08/19 15:29
 */
@Repository
public class WorkflowDefinitionNativeRepository {

    @Autowired
    private EntityManager em;

    public Page<WorkflowDefinitionEntity> listPage(SearchDefinitionRequest request) {
        StringBuilder sqlList = new StringBuilder();
        sqlList.append(" select t.id,  ")
                .append("        t.name, ")
                .append("        t.description, ")
                .append("        t.initial_code         initialCode, ")
                .append("        t.is_released          isReleased, ")
                .append("        t.status               status, ")
                .append("        ifnull(p1.version, -1) version ")
                .append("from workflow_definition t ")
                .append("inner join(SELECT t1.initial_code, max(t1.version) version FROM workflow_definition t1 group by t1.initial_code) p on t.initial_code = p.initial_code and t.version = p.version ")
                .append("left join(SELECT t1.initial_code, max(t1.version) version FROM workflow_definition t1 where t1.is_released = 1 group by t1.initial_code) p1 on t.initial_code = p1.initial_code ")
                .append("where t.status != -1 ");

        StringBuilder sqlCount = new StringBuilder();
        sqlCount.append(" select count(*) from workflow_definition where status != -1  ");
        boolean hasKey = StringUtils.isNotBlank(request.getKey());
        if (hasKey) {
            sqlList.append(" and t.name like concat('%',:name,'%')  ");
            sqlCount.append("and name like concat('%',:name,'%') ");
        }

        sqlCount.append(" group by initial_code ");


        Query dataQuery = em.createNativeQuery(sqlList.toString());

        if (hasKey) {
            dataQuery.setParameter("name", request.getKey());
        }

        dataQuery.setFirstResult((request.getPageIndex()) * request.getPageSize());
        dataQuery.setMaxResults(request.getPageSize());
        dataQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(WorkflowDefinitionEntity.class));
        List<WorkflowDefinitionEntity> list = dataQuery.getResultList();

        Query countQuery = em.createNativeQuery(sqlCount.toString());
        if (hasKey) {
            countQuery.setParameter("name", request.getKey());
        }
        int total = countQuery.getResultList().size();
        Page<WorkflowDefinitionEntity> result = new PageImpl<>(list, PageRequest.of(request.getPageIndex(), request.getPageSize()), total);
        return result;
    }
}

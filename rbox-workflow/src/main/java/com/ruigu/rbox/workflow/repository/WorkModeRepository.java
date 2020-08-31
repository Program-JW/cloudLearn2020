package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.WorkModeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liqingtian
 * @date 2019/09/10 1:04
 */
@Repository
public interface WorkModeRepository extends JpaRepository<WorkModeEntity, Integer> {

    /**
     * 获取当天各部门工作时间
     *
     * @param day 本周的第x天
     * @return List<WorkModeEntity>
     */

    @Query("select m from WorkModeEntity m where m.day =:day")
    List<WorkModeEntity> selectWorkModeToday(@Param("day") Integer day);
}

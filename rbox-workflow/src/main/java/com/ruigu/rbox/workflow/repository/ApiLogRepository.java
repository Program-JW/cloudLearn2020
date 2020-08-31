package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.ApiLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chenzhenya
 * @date 2019/11/22 12:13
 */
public interface ApiLogRepository extends JpaRepository<ApiLogEntity, Integer> {
}

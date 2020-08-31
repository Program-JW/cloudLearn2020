package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleReviewPositionEntity;
import jdk.nashorn.internal.runtime.FindProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/11 14:47
 */
public interface SpecialAfterSaleReviewPositionRepository extends JpaRepository<SpecialAfterSaleReviewPositionEntity, Integer> {

    /**
     * 通过配置id查找相应职位数据
     *
     * @param configIdList 配置id列表
     * @return 配置职位信息
     */
    List<SpecialAfterSaleReviewPositionEntity> findAllByConfigIdIn(List<Integer> configIdList);
}

package com.ruigu.rbox.workflow.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruigu.rbox.workflow.model.entity.QSpecialAfterSaleReviewEntity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liqingtian
 * @date 2020/08/11 16:02
 */
public class CustomSpecialAfterSaleReviewRepositoryImpl implements CustomSpecialAfterSaleReviewRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    private final QSpecialAfterSaleReviewEntity qReview = QSpecialAfterSaleReviewEntity.specialAfterSaleReviewEntity;
    
}

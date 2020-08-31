package com.ruigu.rbox.workflow.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruigu.rbox.workflow.model.entity.QSpecialAfterSaleApplyEntity;
import com.ruigu.rbox.workflow.model.entity.QSpecialAfterSaleCcEntity;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleApplyEntity;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/8/21 9:27
 */
@Repository
public class SpecialAfterSaleCcListRepositoryImpl implements SpecialAfterSaleCcListRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    private final static QSpecialAfterSaleCcEntity ccEntity = QSpecialAfterSaleCcEntity.specialAfterSaleCcEntity;

    private final static QSpecialAfterSaleApplyEntity applyEntity = QSpecialAfterSaleApplyEntity.specialAfterSaleApplyEntity;

    @Override
    public QueryResults<SpecialAfterSaleApplyEntity> queryMyCcApplyList(SpecialAfterSaleSearchRequest req, Integer userId) {
        Integer page = req.getPage();
        Integer limit = req.getSize();
        String applyName = StringUtils.trim(req.getKeyWord());
        Predicate predicate = ccEntity.userId.eq(userId);
        //执行动态条件拼装
        predicate = StringUtils.isEmpty(applyName) ?
                predicate : ExpressionUtils.and(predicate, applyEntity.applyNickname.like("%" + applyName + "%"));
        return queryFactory.select(applyEntity).from(applyEntity).join(ccEntity)
                .on(ccEntity.applyId.eq(applyEntity.id))
                .where(predicate)
                .offset(page * limit).limit(limit)
                .orderBy(ccEntity.userId.asc())
                .fetchResults();
    }
}

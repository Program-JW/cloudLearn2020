package com.ruigu.rbox.workflow.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.workflow.model.entity.QSpecialAfterSaleApplyApproverEntity;
import com.ruigu.rbox.workflow.model.entity.QSpecialAfterSaleApplyEntity;
import com.ruigu.rbox.workflow.model.entity.QSpecialAfterSaleLogEntity;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleApplyEntity;
import com.ruigu.rbox.workflow.model.enums.SpecialAfterSaleLogActionEnum;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liqingtian
 * @date 2020/08/12 22:39
 */
public class CustomSpecialAfterSaleApplyRepositoryImpl implements CustomSpecialAfterSaleApplyRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    private final QSpecialAfterSaleApplyEntity qApply = QSpecialAfterSaleApplyEntity.specialAfterSaleApplyEntity;

    @Override
    public QueryResults<SpecialAfterSaleApplyEntity> queryListMyApproved(SpecialAfterSaleSearchRequest request) {
        BooleanBuilder totalBuilder = new BooleanBuilder();

        String keyWord = request.getKeyWord();
        if (StringUtils.isNotBlank(keyWord)) {
            totalBuilder.and(qApply.applyNickname.like(keyWord + "%"));
        }

        Integer userId = UserHelper.getUserId();
        QSpecialAfterSaleLogEntity qLog = QSpecialAfterSaleLogEntity.specialAfterSaleLogEntity;
        totalBuilder.and(qApply.id.in(
                JPAExpressions.select(qLog.applyId).from(qLog)
                        .where(
                                qLog.action.in(
                                        SpecialAfterSaleLogActionEnum.DX_MANAGER_TRANSFERRED.getCode(),
                                        SpecialAfterSaleLogActionEnum.PASS.getCode(),
                                        SpecialAfterSaleLogActionEnum.REJECT.getCode()
                                ).and(qLog.createdBy.eq(userId))
                        )
                )
        );
        Integer page = request.getPage(), size = request.getSize();
        // 关于查我已审批过的
        return queryFactory.select(qApply)
                .from(qApply)
                .where(totalBuilder)
                .offset(page * size).limit(size)
                .fetchResults();
    }

    @Override
    public QueryResults<SpecialAfterSaleApplyEntity> queryListMyPendingApprove(SpecialAfterSaleSearchRequest request) {
        BooleanBuilder totalBuilder = new BooleanBuilder();
        String keyWord = request.getKeyWord();
        if (StringUtils.isNotBlank(keyWord)) {
            totalBuilder.and(qApply.applyNickname.like(keyWord + "%"));
        }
        QSpecialAfterSaleApplyApproverEntity qApplyApprover = QSpecialAfterSaleApplyApproverEntity.specialAfterSaleApplyApproverEntity;
        totalBuilder.and(qApplyApprover.currentApproverId.eq(UserHelper.getUserId()));
        Integer page = request.getPage(), size = request.getSize();
        return queryFactory.select(qApply)
                .from(qApply).leftJoin(qApplyApprover).on(qApply.id.eq(qApplyApprover.applyId))
                .where(totalBuilder)
                .offset(page * size).limit(size)
                .fetchResults();
    }
}

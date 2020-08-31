package com.ruigu.rbox.workflow.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.model.entity.QSpecialAfterSaleApplyApproverEntity;
import com.ruigu.rbox.workflow.model.entity.QSpecialAfterSaleApplyEntity;
import com.ruigu.rbox.workflow.model.entity.QSpecialAfterSaleLogEntity;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleApplyEntity;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.SpecialAfterSaleApplyStatusEnum;
import com.ruigu.rbox.workflow.model.enums.SpecialAfterSaleLogActionEnum;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleApplyExportRequest;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleApplyRequest;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/12 22:39
 */
public class CustomSpecialAfterSaleApplyRepositoryImpl implements CustomSpecialAfterSaleApplyRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private PassportFeignClient passportFeignClient;

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
    
    @Override
    public QueryResults<SpecialAfterSaleApplyEntity> queryAfterSaleListByPage(SpecialAfterSaleApplyRequest req) {
        Integer page = req.getPage();
        Integer limit = req.getSize();
        return queryFactory.selectFrom(qApply)
                .where(this.buildQueryConditions(req))
                .offset(page * limit)
                .limit(limit)
                .orderBy(qApply.id.asc())
                .fetchResults();
    }

    @Override
    public List<SpecialAfterSaleApplyEntity> queryAllAfterSaleList(SpecialAfterSaleApplyExportRequest req) {
        return queryFactory.selectFrom(qApply)
                .where(this.buildQueryConditions(req))
                .orderBy(qApply.id.asc())
                .fetch();
    }

    @Override
    public void updateAfterSaleApplyStatus(Integer status, Long applyId) {
        queryFactory.update(qApply)
                .set(qApply.status, SpecialAfterSaleApplyStatusEnum.UNDO.getCode())
                .set(qApply.lastUpdateAt, LocalDateTime.now())
                .set(qApply.lastUpdateBy, UserHelper.getUserId())
                .where(qApply.id.eq(applyId))
                .execute();
    }


    private Predicate buildQueryConditions(SpecialAfterSaleApplyExportRequest req) {
        Integer status = req.getStatus();
        Integer deptId = req.getDeptId();
        String userName = StringUtils.trim(req.getUserName());
        LocalDate startDate = req.getStartTime();
        LocalDate endDate = req.getEndTime();
        QSpecialAfterSaleApplyEntity qApply = QSpecialAfterSaleApplyEntity.specialAfterSaleApplyEntity;
        Predicate predicate = qApply.isNotNull().or(qApply.isNull());
        //执行动态条件拼装
        predicate = StringUtils.isBlank(userName) ?
                predicate : ExpressionUtils.and(predicate, qApply.applyNickname.like("%" + userName + "%"));
        predicate = status == null ?
                predicate : ExpressionUtils.and(predicate, qApply.status.eq(status));
        if (deptId != null) {
            List<Integer> userList = passportFeignClient.getUserIdListUnderTheGroup(Collections.singletonList(deptId)).getData();
            if (userList == null) {
                throw new GlobalRuntimeException(ResponseCode.ERROR.getCode(), "申请人编号" + deptId + "对应的部门无法找到");
            }
            predicate = ExpressionUtils.and(predicate, qApply.createdBy.in(userList));
        }
        if (startDate != null && endDate != null) {
            LocalDateTime start = LocalDateTime.of(req.getStartTime(), LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(req.getEndTime(), LocalTime.MAX);
            predicate = ExpressionUtils.and(predicate, qApply.createdAt.between(start, end));
        } else if (startDate != null) {
            LocalDateTime start = LocalDateTime.of(req.getStartTime(), LocalTime.MIN);
            predicate = ExpressionUtils.and(predicate, qApply.createdAt.goe(start));
        } else if (endDate != null) {
            LocalDateTime end = LocalDateTime.of(req.getEndTime(), LocalTime.MAX);
            predicate = ExpressionUtils.and(predicate, qApply.createdAt.loe(end));
        }
        return predicate;
    }

}

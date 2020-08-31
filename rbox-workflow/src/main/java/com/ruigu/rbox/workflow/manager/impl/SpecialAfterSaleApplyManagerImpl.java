package com.ruigu.rbox.workflow.manager.impl;

import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.workflow.manager.SpecialAfterSaleApplyManager;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleApplyApproverEntity;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleApplyEntity;
import com.ruigu.rbox.workflow.model.entity.SpecialAfterSaleDetailEntity;
import com.ruigu.rbox.workflow.model.request.SpecialAfterSaleDetailRequest;
import com.ruigu.rbox.workflow.model.request.lightning.AddSpecialAfterSaleApplyRequest;
import com.ruigu.rbox.workflow.repository.SpecialAfterSaleApplyApproverRepository;
import com.ruigu.rbox.workflow.repository.SpecialAfterSaleApplyRepository;
import com.ruigu.rbox.workflow.repository.SpecialAfterSaleDetailRepository;
import com.ruigu.rbox.workflow.service.OrderNumberService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liqingtian
 * @date 2020/08/11 18:09
 */
@Service
public class SpecialAfterSaleApplyManagerImpl implements SpecialAfterSaleApplyManager {

    @Resource
    private SpecialAfterSaleApplyRepository specialAfterSaleApplyRepository;
    @Resource
    private SpecialAfterSaleApplyApproverRepository specialAfterSaleApplyApproverRepository;
    @Resource
    private SpecialAfterSaleDetailRepository specialAfterSaleDetailRepository;
    @Resource
    private OrderNumberService orderNumberService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SpecialAfterSaleApplyEntity saveApply(AddSpecialAfterSaleApplyRequest request, Integer configId, Integer userId, String userName) {

        SpecialAfterSaleApplyEntity apply = new SpecialAfterSaleApplyEntity();
        apply.setConfigId(configId);
        // 主体信息
        apply.setCommon(request.getCommon());
        apply.setAreaId(request.getAreaId());
        apply.setAreaName(request.getAreaName());
        apply.setCityId(request.getCityId());
        apply.setCityName(request.getCityName());
        apply.setApplyReason(request.getApplicationReasons());
        apply.setType(request.getType());
        apply.setNeedSupport(request.getNeedSupport());
        apply.setStatus(YesOrNoEnum.NO.getCode());
        apply.setApplyNickname(userName);
        // 客户信息
        apply.setCustomerId(request.getCustomerId());
        apply.setCustomerName(request.getCustomerName());
        apply.setCustomerPhone(request.getCustomerPhone());
        apply.setCustomerRating(request.getCustomerRating());
        // 基础信息
        LocalDateTime now = LocalDateTime.now();
        apply.setCreatedAt(now);
        apply.setCreatedBy(userId);
        apply.setLastUpdateAt(now);
        apply.setLastUpdateBy(userId);
        // 详情信息
        BigDecimal totalApplyAmount = BigDecimal.ZERO;
        List<SpecialAfterSaleDetailEntity> detailEntities = new ArrayList<>();
        List<SpecialAfterSaleDetailRequest> details = request.getDetails();
        for (SpecialAfterSaleDetailRequest d : details) {
            SpecialAfterSaleDetailEntity entity = new SpecialAfterSaleDetailEntity();
            entity.setOrderType(d.getOrderType());
            entity.setOrderDate(d.getOrderDate());
            entity.setApplyAfterSaleDate(d.getApplyAfterSaleDate());
            entity.setOrderNumber(d.getOrderNumber());
            entity.setProductBrand(d.getProductBrand());
            entity.setProductName(d.getProductName());
            entity.setProductNumber(d.getProductNumber());
            entity.setProductCount(d.getProductCount());
            // 申请金额
            BigDecimal applyAmount = d.getApplyAmount();
            entity.setApplyAmount(applyAmount);
            totalApplyAmount = totalApplyAmount.add(applyAmount);
            detailEntities.add(entity);
        }
        // 计算总金额
        apply.setTotalApplyAmount(totalApplyAmount);
        // 生成单号
        apply.setCode(orderNumberService.createSasApplyOrderNumber());
        // 保存
        specialAfterSaleApplyRepository.save(apply);

        Long applyId = apply.getId();

        detailEntities.forEach(d -> {
            d.setApplyId(applyId);
        });
        specialAfterSaleDetailRepository.saveAll(detailEntities);
        return apply;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SpecialAfterSaleApplyEntity saveApply(SpecialAfterSaleApplyEntity apply) {
        return specialAfterSaleApplyRepository.save(apply);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCurrentApprover(Long applyId, List<Integer> approverIdList) {
        specialAfterSaleApplyApproverRepository.saveAll(buildApprover(applyId, approverIdList));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCurrentApprover(Long applyId, List<Integer> approverIdList) {
        specialAfterSaleApplyApproverRepository.deleteByApplyId(applyId);
        specialAfterSaleApplyApproverRepository.saveAll(buildApprover(applyId, approverIdList));
    }

    @Override
    public List<Integer> queryCurrentApprover(Long applyId) {
        List<SpecialAfterSaleApplyApproverEntity> currentApprovers = specialAfterSaleApplyApproverRepository.findAllByApplyId(applyId);
        if (CollectionUtils.isNotEmpty(currentApprovers)) {
            return currentApprovers.stream().map(SpecialAfterSaleApplyApproverEntity::getCurrentApproverId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean checkIsApprover(Long applyId, Integer userId) {
        List<SpecialAfterSaleApplyApproverEntity> collect = specialAfterSaleApplyApproverRepository.findAllByApplyId(applyId)
                .stream().filter(u -> u.getCurrentApproverId().equals(userId))
                .collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(collect);
    }

    private List<SpecialAfterSaleApplyApproverEntity> buildApprover(Long applyId, List<Integer> approverIdList) {
        List<SpecialAfterSaleApplyApproverEntity> applyApproverList = new ArrayList<>();
        approverIdList.forEach(id -> {
            SpecialAfterSaleApplyApproverEntity u = new SpecialAfterSaleApplyApproverEntity();
            u.setApplyId(applyId);
            u.setCurrentApproverId(id);
            applyApproverList.add(u);
        });
        return applyApproverList;
    }
}

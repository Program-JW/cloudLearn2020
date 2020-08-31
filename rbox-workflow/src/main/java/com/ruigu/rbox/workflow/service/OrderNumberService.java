package com.ruigu.rbox.workflow.service;

/**
 * 单号服务
 *
 * @author liqingtian
 * @date 2020/05/22 15:02
 */
public interface OrderNumberService {

    /**
     * 生成审批单号服务 （特殊售后审批单号）
     *
     * @return 单号
     */
    String createSasApplyOrderNumber();

}

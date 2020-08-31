package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.ServerResponse;


/**
 * @author caojinghong
 * @date 2020/01/09 15:15
 */
public interface LightningIssueReportService {
    /**
     * 导出闪电链问题报表数据并发送邮件至pmo处
     * @param queryType 查询类型
     * @return 返回导出报表结果
     * @throws Exception 异常
     */
    ServerResponse exportExcel(Integer queryType) throws Exception;

}

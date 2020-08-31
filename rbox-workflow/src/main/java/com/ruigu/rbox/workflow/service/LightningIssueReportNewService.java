package com.ruigu.rbox.workflow.service;

import com.ruigu.rbox.workflow.model.ServerResponse;

import java.io.IOException;
import java.time.LocalDateTime;


/**
 * @author caojinghong
 * @date 2020/01/09 15:15
 */
public interface LightningIssueReportNewService {
    /**
     * 导出闪电链问题报表数据并发送邮件至pmo处
     * @param queryType 查询类型
     * @param startTime 查询的开始时间
     * @param endTime  查询的结束时间
     * @return 返回导出报表结果
     * @throws Exception 异常
     */
    ServerResponse exportExcel(Integer queryType, LocalDateTime startTime, LocalDateTime endTime) throws Exception;

    /**
     * 获取公司全体员工所在一二级部门名称
     * @return 公司全体员工所在一二级部门名称表格
     * @throws IOException IOException 异常
     */
    ServerResponse getAllUserDepartmentInfo() throws IOException;
}

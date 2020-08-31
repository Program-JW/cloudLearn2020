package com.ruigu.rbox.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用来完成审批规则模块 DTO
 */
@Data
public class SpecialAfterSaleApprovalRulesDTO {
    /**
     * 最后更新时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastUpdateAt;

    /**
     * 组id
     */
//    private Integer groupId;

    /**
     * 申请人部门
     */
    private String groupName;

    /**
     * 申请人Id
     */
//    private String id;

    /**
     * 申请人职位
     */
    private String position;

    /**
     * 审批人ID
     */
//    private Integer approvalUserId;

    /**
     * 审批人名字
     */
    private String nickname;

    /**
     * 审批职位
     */
    private String positions;


    /**
     * 抄送人Ids
     */
//    private String ids;

    /**
     * 抄送人ID和名字
     */
    private List<String> ccNickname;








}

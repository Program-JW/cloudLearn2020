package com.ruigu.rbox.workflow.model.vo;

import com.ruigu.rbox.workflow.model.dto.GroupInfoDTO;
import com.ruigu.rbox.workflow.model.dto.LeaveReportTypeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenzhenya
 * @date 2020/5/23 10:16
 */
@Data
public class ReviewConfigVO {
    @ApiModelProperty(name = "id", value = "审核配置ID")
    private Integer id;
    /**
     * 抄送人信息集合
     */
    @ApiModelProperty(name = "ccList", value = "抄送人信息集合")
    private List<CcVO> ccList;
    /**
     * 审核配置名称
     */
    @ApiModelProperty(name = "name", value = "审核配置名称")
    private String name;
    /**
     * 审核配置编码
     */
    @ApiModelProperty(name = "code", value = "审核配置编码")
    private String code;
    /**
     * 审核配置描述
     */
    @ApiModelProperty(name = "description", value = "审核配置描述")
    private String description;
    /**
     * 请假报备类型集合
     */
    @ApiModelProperty(name = "leaveReportTypeList", value = "请假报备类型集合")
    private List<LeaveReportTypeDTO> leaveReportTypeList;
    /**
     * 时间刻度
     */
    @ApiModelProperty(name = "timeScale", value = "时间刻度, 1:小时, 2:天")
    private Integer timeScale;
    /**
     * 申请人标志：1为职位 2为组
     */
    @ApiModelProperty(name = "applyFlag", value = "申请人标志,1为职位 2为组")
    private Integer applyFlag;
    /**
     * 职位集合
     */
    @ApiModelProperty(name = "positionList", value = "职位集合")
    private List<String> positionList;
    /**
     * 部门树
     */
    @ApiModelProperty(name = "groupTree", value = "部门层级")
    private List<GroupInfoDTO> groupTree;
    /**
     * 申请时长
     */
    @ApiModelProperty(name = "applyDuration", value = "申请时长,单位为小时")
    private Integer applyDuration;
    /**
     * 时长比较方式 ：1>=    2>    3=    4<=    5<
     */
    @ApiModelProperty(name = "durationCompare", value = "时长比较方式,1>=    2>    3=    4<=    5<")
    private Integer durationCompare;
    /**
     * 审核人信息
     */
    @ApiModelProperty(name = "reviewUserConfigList", value = "审核人信息")
    private List<ReviewUserConfigVO> reviewUserConfigList;

    @Data
    public static class ReviewUserConfigVO {

        /**
         * 审核人ID
         */
        @ApiModelProperty(name = "userId", value = "审核人ID")
        private Integer userId;
        /**
         * 审核人姓名
         */
        @ApiModelProperty(name = "username", value = "审核人姓名")
        private String username;
        /**
         * 审核人头像
         */
        @ApiModelProperty(name = "avatar", value = "审核人头像")
        private String avatar;
        /**
         * 标签ID
         */
        @ApiModelProperty(name = "tagId", value = "标签ID")
        private Integer tagId;
        /**
         * 标签名
         */
        @ApiModelProperty(name = "tagName", value = "标签名")
        private String tagName;
        /**
         * 审核人标志：1上级、2标签、3单人、4申请人
         */
        @ApiModelProperty(name = "userFlag", value = "审核人标志,1上级、2标签、3单人、4申请人")
        private Integer userFlag;
        /**
         * 相对当前人的n级领导，n为1,2,3...
         */
        @ApiModelProperty(name = "leaderGrade", value = "相对当前人的n级领导，n为1,2,3...")
        private Integer leaderGrade;
        /**
         * 会签/或签：1会签 2或签
         */
        @ApiModelProperty(name = "reviewFlag", value = "会签/或签,1会签 2或签 暂时无用")
        private Integer reviewFlag;
        /**
         * 审核顺序
         */
        @ApiModelProperty(name = "reviewOrder", value = "审核顺序, 审核顺序为1,2,3...")
        private Integer reviewOrder;
    }

}

package com.ruigu.rbox.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenzhenya
 * @date 2020/5/23 10:16
 */
@Data
@JsonIgnoreProperties
public class ReviewConfigDetailVO {
    @ApiModelProperty(name = "id", value = "审核配置ID")
    private Integer id;
    /**
     * 抄送人信息集合
     */
    @ApiModelProperty(name = "ccList", value = "抄送人信息集合")
    private List<CcVO> ccList;
    /**
     * 审核人信息
     */
    @ApiModelProperty(name = "reviewUserConfigList", value = "审核人信息")
    private List<ReviewUserConfigVO> reviewUserConfigList;

    @Data
    public static class ReviewUserConfigVO{

        /**
         * 审核人ID集合
         */
        @ApiModelProperty(name = "userIdList", value = "审核人ID集合1111")
        private List<Integer> userIdList;
        /**
         * 审核顺序
         */
        @ApiModelProperty(name = "reviewOrder", value = "审核顺序")
        private Integer reviewOrder;
    }

}

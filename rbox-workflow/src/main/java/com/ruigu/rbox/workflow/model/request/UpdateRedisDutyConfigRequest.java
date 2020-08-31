package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/05/15 17:48
 */
@Data
@NoArgsConstructor
public class UpdateRedisDutyConfigRequest {

    @ApiModelProperty(value = "分类id", name = "categoryId")
    @NotNull(message = "分类id不能为空")
    private Integer categoryId;

    @ApiModelProperty(value = "策略类型", name = "type")
    @NotNull(message = "策略类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "值班人id ( 按天值班策略 ) ", name = "userId")
    private Integer userId;

    @ApiModelProperty(value = "值班人列表 ( 轮询策略 )", name = "userIds")
    private List<Integer> userIds;
}

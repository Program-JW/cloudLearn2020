package com.ruigu.rbox.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenzhenya
 * @date 2020/3/2 14:19
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RsAreaDTO implements Serializable {

    /**
     * id : 9
     * name : 无锡
     * parent_id : 27
     * path : 1,100,19,27,9
     * is_sales : 1
     * bd_create_order : 0
     * i_enable : 1
     * sort : 12
     * remark :
     * created : 2017-03-13 15:36:13
     * modified : 2019-09-05 15:44:45
     * is_pure_group : 0
     * sales_group_type : 1
     * child : []
     */

    private Integer id;
    private String name;
    @JsonSetter("parent_id")
    private Integer parentId;
    private String path;
    @JsonSetter("is_sales")
    private Integer isSales;
    @JsonSetter("bd_create_order")
    private Integer bdCreateOrder;
    @JsonSetter("i_enable")
    private Integer enable;
    private Integer sort;
    private String remark;
    private String created;
    private String modified;
    @JsonSetter("is_pure_group")
    private Integer isPureGroup;
    @JsonSetter("sales_group_type")
    private Integer salesGroupType;
    private List<RsAreaDTO> child;

}

package com.ruigu.rbox.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenzhenya
 * @date 2020/3/2 16:43
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityVO implements Serializable {

    private Integer id;
    private String name;
    private List<CityVO> child;
}

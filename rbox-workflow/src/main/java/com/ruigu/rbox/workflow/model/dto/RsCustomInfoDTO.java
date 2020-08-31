package com.ruigu.rbox.workflow.model.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @author liqingtian
 * @date 2020/08/17 13:19
 */
@Data
public class RsCustomInfoDTO {

    @JsonSetter("smi_id")
    private Integer shopId;
    @JsonSetter("user_id")
    private Integer customId;
    private String name;
    private String level;
    @JsonSetter("is_lock")
    private Integer lock;

}

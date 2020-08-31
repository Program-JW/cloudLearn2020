package com.ruigu.rbox.workflow.model.request;

import com.ruigu.rbox.workflow.supports.ObjectUtil;
import lombok.Data;

/**
 * @author alan.zhao
 */
@Data
public class SaveDefinitionRequest {
    private String id;
    private String name;
    private String description;
    private String key;
    private String graph;
    private Integer status;

    /**
     * 附加参数 如果是新版本，则另保存为新数据
     */
    private Boolean newVersionIfReleased;

    public SaveDefinitionRequest cloneAndSetValues(String id, String graph) {
        SaveDefinitionRequest copy = new SaveDefinitionRequest();
        ObjectUtil.extendObject(copy, this, true);
        copy.id = id;
        copy.graph = graph;
        return copy;
    }
}

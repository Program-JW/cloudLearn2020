package com.ruigu.rbox.workflow.factory;

import com.ruigu.rbox.workflow.model.enums.EnvelopeTypeEnum;
import com.ruigu.rbox.workflow.model.request.EnvelopeReq;

/**
 * @author liqingtian
 * @date 2020/07/29 10:04
 */
public interface EnvelopeProduct {

    /**
     * 是否匹配
     *
     * @param type 类型
     * @return 是否
     */
    Boolean match(EnvelopeTypeEnum type);

    /**
     * 生产
     */
    EnvelopeReq product();
}

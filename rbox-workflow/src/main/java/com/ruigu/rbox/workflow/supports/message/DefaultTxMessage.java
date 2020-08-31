package com.ruigu.rbox.workflow.supports.message;

import lombok.Builder;

/**
 * @author liqingtian
 * @date 2020/07/10 16:17
 */
@Builder
public class DefaultTxMessage implements TxMessage {

    private String businessModule;

    private String businessKey;

    private String content;

    @Override
    public String businessModule() {
        return businessModule;
    }

    @Override
    public String businessKey() {
        return businessKey;
    }

    @Override
    public String content() {
        return content;
    }
}

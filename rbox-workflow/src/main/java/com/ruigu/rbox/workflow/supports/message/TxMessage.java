package com.ruigu.rbox.workflow.supports.message;

/**
 * @author liqingtian
 * @date 2020/07/10 16:09
 */
public interface TxMessage {

    String businessModule();

    String businessKey();

    String content();

}

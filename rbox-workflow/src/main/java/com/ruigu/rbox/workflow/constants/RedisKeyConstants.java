package com.ruigu.rbox.workflow.constants;

/**
 * @author caojinghong
 * @date 2020-03-09 18:04
 */
public interface RedisKeyConstants {

    /**
     * 我受理列表id
     */
    String MY_ACCEPTANCE_ISSUE_ID_SET = "rbox:workflow:lightning:my-acceptance-issue-id-set:";

    /**
     * 删除问题的键
     */
    String IGNORE_ISSUE = "rbox:workflow:lightning:ignore-issue:userId:";

    /**
     * 限制催办时长的键
     */
    String URGE_RESTRICT = "rbox:workflow:lightning:urge:userId:";
    /**
     * 推推棒维护的passport的用户信息的键
     */
    String PASSPORT_USER_INFO = "rbox:workflow:userInfo:";
    /**
     * 推推棒维护的passport的用户部门信息
     */
    String PASSPORT_USER_GROUP_INFO = "rbox:workflow:user-group-info:";
    /**
     * 推推棒维护用户信息redis标志位
     */
    String PASSPORT_USER_CACHE_ENABLE = "rbox:workflow:userInfo:cache:enable";
    /**
     * 推推棒维护用户-部门redis标志位
     */
    String PASSPORT_USER_GROUP_CACHE_ENABLE = "rbox:workflow:user-group-info:cache:enable";
    /**
     * 推推棒 - 闪电链 - 问题类型值班人列表 （轮询值班）
     */
    String ISSUE_CATEGORY_DUTY_POLL = "rbox:workflow:lightning:category:duty-queue:";

    /**
     * 推推棒 - 闪电链 - 问题类型值班人 （按天值班）
     */
    String ISSUE_CATEGORY_DUTY_BY_DAY = "rbox:workflow:lightning:category:%s:duty-user:";

    /**
     * mqLogId 和 uuid之间关系
     */
    String MQ_ID_UUID_CORRELATION_KEY = "rbox:workflow:msg-id:uuid:correlation:%s";

    /***
     * 特殊售后审批每天申请数量key
     *
     */
    String SAS_APPLY_COUNT = "rbox:workflow:sas-apply-count:day:%s";

    /**
     *
     */
    String SPECIAL_SALE_URGE_RESTRICT = "rbox:workflow:special-sale:urge:";
}

package com.ruigu.rbox.workflow.mq;

import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.cloud.kanai.security.UserInfo;
import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.config.RabbitMqConfig;
import com.ruigu.rbox.workflow.exceptions.LogicException;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.manager.RedisInitManager;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.dto.TaskCardReturnMessageDTO;
import com.ruigu.rbox.workflow.model.dto.WeChatCallbackUserDTO;
import com.ruigu.rbox.workflow.model.entity.NoticeEntity;
import com.ruigu.rbox.workflow.model.entity.RabbitmqMsgLogEntity;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.WeChatCallBackTypeEnum;
import com.ruigu.rbox.workflow.model.enums.WeixinMsgElement;
import com.ruigu.rbox.workflow.service.LightningIssueService;
import com.ruigu.rbox.workflow.service.NoticeLogService;
import com.ruigu.rbox.workflow.service.RabbitmqMsgService;
import com.ruigu.rbox.workflow.strategy.context.TaskCardHandleContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2019/09/09 0:56
 */

@Slf4j
@Service
public class RabbitWeiXinMsgListener {
    @Resource
    private NoticeLogService noticeLogService;

    @Resource
    private RabbitmqMsgService rabbitmqMsgService;

    @Resource
    private PassportFeignManager passportFeignManager;

    @Resource
    private TaskCardHandleContext taskCardHandleContext;

    @Autowired
    private LightningIssueService lightningIssueService;

    @Resource
    private RedisInitManager redisInitManager;

    @RabbitListener(queues = RabbitMqConfig.WEIXIN_QUEUE_NAME)
    public void process(Message message) {
        log.info("=============================== 收到新的 WEIXIN Rabbit MQ 消息 ================================");
        String messageString = new String(message.getBody());
        TaskCardReturnMessageDTO returnMessageDTO = null;
        try {
            returnMessageDTO = JsonUtil.parseObject(messageString, TaskCardReturnMessageDTO.class);
            if (returnMessageDTO == null) {
                log.error("| - rabbit mq 消息类型转换失败");
                return;
            }
        } catch (Exception e) {
            log.error("| - rabbit mq 消息类型转换失败");
            return;
        }
        if (WeixinMsgElement.MSG_TYPE_EVENT.getDesc().equals(returnMessageDTO.getMsgType())) {
            // 排除不是推推棒需要接收的消息
            if (StringUtils.isBlank(returnMessageDTO.getTaskId())
                    || !returnMessageDTO.getTaskId().startsWith(WeixinMsgElement.TASK_ID_PREFIX.getDesc())) {
                return;
            }
            // 验证是否重读
            RabbitmqMsgLogEntity msgByTaskId = rabbitmqMsgService.getMsgByTaskId(returnMessageDTO.getTaskId());
            if (msgByTaskId != null) {
                return;
            }
            // 验证是否是自己的
            NoticeEntity notice = noticeLogService.getNoticeByTaskIdWeixin(returnMessageDTO.getTaskId());
            if (notice != null) {
                // 保存数据库一份
                RabbitmqMsgLogEntity rabbitmqMsg = new RabbitmqMsgLogEntity();
                rabbitmqMsg.setMessage(messageString);
                rabbitmqMsg.setTaskIdWeixin(returnMessageDTO.getTaskId());
                int rabbitmqMsgId = rabbitmqMsgService.saveMsg(rabbitmqMsg);
                if (rabbitmqMsgId == -1) {
                    log.error("rabbit数据保存失败，数据：{}", messageString);
                }
                // 设置操作人ID
                setOperator(returnMessageDTO);
                // 解析类型
                ServerResponse handle = taskCardHandleContext.handle(returnMessageDTO, notice);
                if (handle.getCode() != ResponseCode.SUCCESS.getCode()) {
                    log.error("| - 任务卡片回调：[ 处理结果 ]：{}", handle.getMessage());
                }
            }
        }
    }

    @RabbitListener(queues = "${rbox.mq.msg.weixin.queue}")
    public void receiveUserUpdateMessage(Message message) {
        log.info("=============================== 收到新的 WEIXIN Rabbit MQ 消息 成员变更================================");
        try {
            String bodyStr = new String(message.getBody());
            Map<String, String> bodyMap = JsonUtil.parseMap(bodyStr, String.class, String.class);
            handleMessageType(bodyMap.get("changeType"), bodyMap, bodyStr);
            log.info("==============================消息处理完成,返回收到的message--->{}", message);
        } catch (Exception e) {
            log.error("WEIXIN Rabbit MQ 消息 成员变更消息处理异常,异常信息 : {}", e.getMessage());
        }
    }

    private void handleMessageType(String changeType, Map<String, String> msgMap, String bodyStr) throws Exception {
        String userId = msgMap.get("userId");
        if (StringUtils.isBlank(userId)) {
            throw new LogicException(" 企业微信回调：解析用户userid失败 ，msgMap：" + msgMap);
        }

        if (WeChatCallBackTypeEnum.UPDATE_USER.getType().equals(changeType)) {
            String status = msgMap.get("status");
            if (StringUtils.isNotBlank(status) && status.equals(String.valueOf(2))) {
                RabbitmqMsgLogEntity rabbitmqMsg = new RabbitmqMsgLogEntity();
                rabbitmqMsg.setMessage(bodyStr);
                rabbitmqMsg.setCreatedOn(new Date());
                rabbitmqMsgService.saveMsg(rabbitmqMsg);
                lightningIssueService.userLeaveOffice(userId);
            }
            updateRedisUserAndGroup(bodyStr);
        } else if (WeChatCallBackTypeEnum.DELETE_USER.getType().equals(changeType)) {
            RabbitmqMsgLogEntity rabbitmqMsg = new RabbitmqMsgLogEntity();
            rabbitmqMsg.setMessage(bodyStr);
            rabbitmqMsg.setCreatedOn(new Date());
            rabbitmqMsgService.saveMsg(rabbitmqMsg);
            lightningIssueService.userLeaveOffice(userId);
        } else if (WeChatCallBackTypeEnum.CREATE_USER.getType().equals(changeType)) {
            updateRedisUser(userId);
        } else {
            log.info("无用的队列消息");
        }
    }

    private void updateRedisUser(String userId) {
        // 只要用户信息发生变化就更新redis
        try {
            redisInitManager.updatePassportInfo(passportFeignManager.getUserByWxUserId(userId));
        } catch (Exception e) {
            log.error("监听企业微信回调，更新人员信息缓存异常。e:{}", e);
        }
    }

    private void updateRedisUserAndGroup(String message) {
        // 微信回调信息
        WeChatCallbackUserDTO callbackInfo = JsonUtil.parseObject(message, WeChatCallbackUserDTO.class);
        // 获取此次更新的人员id
        String wxUserId = callbackInfo.getUserId();
        PassportUserInfoDTO userInfo = passportFeignManager.getUserByWxUserId(wxUserId);
        // 更新用户信息
        try {
            redisInitManager.updatePassportInfo(userInfo);
        } catch (Exception e) {
            log.error("监听企业微信回调，更新人员信息缓存异常。e:{}", e);
        }
        // 更新用户部门信息
        if (StringUtils.isNotBlank(callbackInfo.getDepartment())) {
            try {
                // 如果有的话，说明有变更，直接删除
                // 考虑到一种情况，如果删除的同时正好有线程查到老数据要去init,那么删除在前的话，init操作就会将查的老数据插入
                // 因此延迟1s
                Thread.sleep(1000L);
                // 删除
                redisInitManager.removeUserGroupInfo(userInfo.getId());
            } catch (Exception e) {
                log.error("监听企业微信回调，更新人员-部门信息缓存异常。e:{}", e);
            }
        }
    }

    // 因为mq回调，不经过网关，因此没有操作人信息，此时手动插入
    private void setOperator(TaskCardReturnMessageDTO message) {
        String userId = message.getFromUserName();
        PassportUserInfoDTO userInfo = passportFeignManager.getUserByWxUserId(userId);
        if (userInfo != null) {
            UserInfo info = new UserInfo();
            info.setUserId(userInfo.getId());
            info.setUserName(userInfo.getUsername());
            UserHelper.setUserInfo(info);
        }
    }
}

package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.model.ActionConstants;
import com.ruigu.rbox.workflow.model.LightningReturnMessageInfoMap;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.client.AbstractReconnectWebSocketClient;
import com.ruigu.rbox.workflow.model.dto.*;
import com.ruigu.rbox.workflow.model.entity.ChatWebSocketUser;
import com.ruigu.rbox.workflow.model.entity.LightningIssueGroupEntity;
import com.ruigu.rbox.workflow.model.entity.WsApiLogEntity;
import com.ruigu.rbox.workflow.model.enums.LightningApplyStatus;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.enums.TransferTypeEnum;
import com.ruigu.rbox.workflow.repository.LightningIssueGroupRepository;
import com.ruigu.rbox.workflow.service.ChatWebSocketService;
import com.ruigu.rbox.workflow.service.WsApiLogService;
import com.ruigu.rbox.workflow.supports.WebSocketClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liqingtian
 * @date 2020/01/08 15:47
 */
@Slf4j
@Service
public class ChatWebSocketServiceImpl implements ChatWebSocketService {

    @Value("${rbox.chat.websocket.robot}")
    private String robotName;

    @Resource
    private WsApiLogService wsApiLogService;

    @Resource
    private AbstractReconnectWebSocketClient reconnectWebSocketClient;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private LightningIssueGroupRepository lightningIssueGroupRepository;

    @Resource
    private LightningReturnMessageInfoMap lightningReturnMessageInfoMap;

    @Value("${rbox.workflow.lightning.close.group.enable}")
    private boolean closeGroupEnable;

    @Override
    public void login(String userId) {
        if (userId == null) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(),
                    "异常，登陆人ID为空");
        }
        SendWebSocketMessageDTO message = new SendWebSocketMessageDTO();
        SendWebSocketMessageDTO.MessageContent content = new SendWebSocketMessageDTO.MessageContent();
        content.setFromConnName("0-" + userId);
        message.setContent(content);
        message.setAction(ActionConstants.LOGIN);
        reconnectWebSocketClient.send(JsonUtil.toJsonString(message));
    }

    @Override
    public void loginRobot() {
        RobotAndWebSocketMessageDTO info = WebSocketClientUtil.getRobotLoginMessage(robotName);
        SendWebSocketMessageDTO message = info.getSendWebSocketMessageDTO();
        String loginRobotName = info.getLoginRobotName();
        try {
            reconnectWebSocketClient.send(JsonUtil.toJsonString(message));
            log.info("| - > [ WebSocket ] 机器人登陆 {}", loginRobotName);
        } catch (Exception e) {
            log.info("| - > [ WebSocket ] 机器人登陆失败 失败原因： {}", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buildGroup(List<BuildGroupDTO> buildGroupList) {
        String errorHead = "| - > [ WebSocket ] - 群组新增用户 - ";
        if (CollectionUtils.isEmpty(buildGroupList)) {
            logAndThrowException(errorHead, "异常，请求参数为空");
        }
        // 消息
        SendWebSocketMessageDTO sendMessage;
        SendWebSocketMessageDTO.MessageContent content;
        // 获取批量数据中所有的相关用户，请求获取用户信息
        Set<Integer> userIds = buildGroupList.stream()
                .map(BuildGroupDTO::getMasterId)
                .distinct()
                .collect(Collectors.toSet());
        buildGroupList.parallelStream()
                .map(BuildGroupDTO::getMemberIds)
                .forEach(members -> {
                    if (CollectionUtils.isNotEmpty(members)) {
                        userIds.addAll(members);
                    }
                });
        if (CollectionUtils.isEmpty(userIds)) {
            logAndThrowException(errorHead, "异常，群成员不能为空");
        }
        for (BuildGroupDTO group : buildGroupList) {
            Integer issueId = group.getIssueId();
            Integer masterId = group.getMasterId();
            Long messageId = System.currentTimeMillis();
            content = new SendWebSocketMessageDTO.MessageContent();
            content.setRandomCode(messageId);
            content.setAppId(0);
            content.setFromConnName("0-" + masterId);
            content.setGroupTitle("闪电链问题群 [ No." + issueId + " ]");
            List<ChatWebSocketUser> userList = new ArrayList<>();
            List<Integer> members = group.getMemberIds();
            if (CollectionUtils.isNotEmpty(members)) {
                members.forEach(memberId -> {
                    ChatWebSocketUser user = new ChatWebSocketUser();
                    user.setConnName("0-" + memberId);
                    userList.add(user);
                });
            }
            content.setUserList(userList);
            if (StringUtils.isBlank(robotName)) {
                logAndThrowException(errorHead, "异常，机器人配置信息异常，Name is Empty");
            }
            content.setRobotConnName(robotName);
            // 初始化信息载体
            sendMessage = new SendWebSocketMessageDTO();
            sendMessage.setContent(content);
            sendMessage.setAction(ActionConstants.EXT_ROBOT_GROUP);
            // 记录日志
            log(messageId, issueId, sendMessage.getParam(), masterId);
            // 发送建群消息
            try {
                reconnectWebSocketClient.send(JsonUtil.toJsonString(sendMessage));
            } catch (Exception e) {
                logAndThrowException(errorHead, "异常，聊天室连接异常，建立群组失败 ");
            }
            // 同步阻塞获取websocket回调
            ReturnWebSocketMessageDTO returnMessage = lightningReturnMessageInfoMap.getMessage(messageId);
            if (returnMessage == null) {
                logAndThrowException(errorHead, "异常，建立群组失败或超时 ");
            } else {
                Integer result = returnMessage.getResult();
                if (result != ResponseCode.SUCCESS.getCode()) {
                    logAndThrowException(errorHead, "异常，聊天室建群失败");
                }
                Long groupId = returnMessage.getGroupId();
                if (groupId == null) {
                    logAndThrowException(errorHead, "异常，聊天室没有返回相应群组id ");
                }
                LightningIssueGroupEntity groupEntity = new LightningIssueGroupEntity();
                groupEntity.setIssueId(issueId);
                groupEntity.setGroupId(groupId.toString());
                groupEntity.setGroupName(returnMessage.getGroupTitle());
                groupEntity.setCreatedOn(new Date());
                lightningIssueGroupRepository.save(groupEntity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserToGroup(Integer issueId, Long groupId, Integer userId, Integer addUserId) {
        String errorHead = "| - > [ WebSocket ] - 群组新增用户 - ";
        if (groupId == null || addUserId == null) {
            logAndThrowException(errorHead, "异常，请求参数为空");
        }
        // 初始化信息载体
        SendWebSocketMessageDTO sendMessage = new SendWebSocketMessageDTO();
        sendMessage.setAction(ActionConstants.ROBOT_ADD_USER);
        // 设置消息内容
        Long messageId = System.currentTimeMillis();
        SendWebSocketMessageDTO.MessageContent content = WebSocketClientUtil.buildRobotMessage(robotName);
        content.setRandomCode(messageId);
        content.setGroupId(groupId);
        // add user列表
        ChatWebSocketUser addUser = new ChatWebSocketUser();
        addUser.setConnName("0-" + addUserId);
        content.setAddUserList(Collections.singletonList(addUser));
        sendMessage.setContent(content);
        // 记录
        log(messageId, issueId, sendMessage.getParam(), 0);
        // 发送添加成员消息
        try {
            reconnectWebSocketClient.send(JsonUtil.toJsonString(sendMessage));
            // 同步阻塞获取回调信息
            ReturnWebSocketMessageDTO returnMessage = lightningReturnMessageInfoMap.getMessage(messageId);
            if (returnMessage == null) {
                logAndThrowException(errorHead, "异常，建立群组失败或超时 ");
            } else {
                Integer result = returnMessage.getResult();
                if (result != ResponseCode.SUCCESS.getCode()) {
                    logAndThrowException(errorHead, "异常，加入群成员失败 ");
                }
            }
            sendActionMessage(issueId, groupId, addUserId, LightningApplyStatus.ADD_MEMBER);
        } catch (Exception e) {
            logAndThrowException(errorHead, "异常，聊天室连接异常，添加用户失败");
        }
    }

    @Override
    public void sendMessage(Integer issueId, Long groupId, String content, String action) {
        String errorHead = "| - > [ WebSocket ] - " + action + " - ";
        if (issueId == null || groupId == null || StringUtils.isBlank(content)) {
            log.error(errorHead + "请求参数为空");
            return;
        }
        // 初始化信息载体
        SendWebSocketMessageDTO sendMessage = new SendWebSocketMessageDTO();
        sendMessage.setAction(ActionConstants.ROBOT_SEND_GROUP_MESSAGE);
        // 拼装参数
        SendWebSocketMessageDTO.MessageContent messageContent = WebSocketClientUtil.buildRobotMessage(robotName);
        messageContent.setGroupId(groupId);
        messageContent.setContent(content);
        sendMessage.setContent(messageContent);
        // 记录
        log(null, issueId, sendMessage.getParam(), 0);
        // 发送信息
        try {
            reconnectWebSocketClient.send(JsonUtil.toJsonString(sendMessage));
        } catch (Exception e) {
            log.error(errorHead + "消息发送失败");
        }
    }

    @Override
    public void sendActionMessage(Integer issueId, Long groupId, Integer userId, LightningApplyStatus status) {
        String action = null;
        switch (status) {
            case TO_BE_CONFIRMED:
                action = "已解决并填写了原因总结";
                break;
            case RESOLVED:
                action = "确认已解决该申请";
                break;
            case UNRESOLVED:
                action = "确认未解决该申请";
                break;
            case REVOKED:
                action = "已撤销该申请";
                break;
            case URGE:
                action = "催办了该申请";
                break;
            case ACCEPTING:
                action = "已受理了该申请";
                break;
            case RESUBMIT:
                action = "重新提交了该申请";
                break;
            case ADD_MEMBER:
                action = "加入群组";
                break;
            default:
                break;
        }
        if (StringUtils.isBlank(action)) {
            return;
        }
        try {
            Map<String, PassportUserInfoDTO> userInfoMap = getUserInfoMap(Collections.singletonList(userId));
            String userName = userInfoMap.get(userId.toString()).getNickname();
            String content = userName + action;
            sendMessage(issueId, groupId, content, status.getDesc());
        } catch (Exception e) {
            log.error("发送交接系统消息报错：{}", e);
        }
    }

    @Override
    public void sendAssociateMessage(Integer issueId, Long groupId, Integer userId, Integer assigneeId, Integer transferType) {
        try {
            Map<String, PassportUserInfoDTO> userInfoMap = getUserInfoMap(Arrays.asList(userId, assigneeId));
            String assigneeName = userInfoMap.get(assigneeId.toString()).getNickname();
            String userName = null;
            if (TransferTypeEnum.LEAVE_TRANSFER.getCode().equals(transferType)) {
                userName = "系统已将问题交接给";
            } else if (TransferTypeEnum.INVITE_TRANSFER.getCode().equals(transferType)){
                userName = userInfoMap.get(userId.toString()).getNickname() + "邀请并将问题交接给";
            } else if (TransferTypeEnum.SOLVER_TRANSFER.getCode().equals(transferType)){
                userName = userInfoMap.get(userId.toString()).getNickname() + "已将问题交接给";
            } else {
                throw new RuntimeException("transferType错误");
            }
            String content = userName + assigneeName;
            sendMessage(issueId, groupId, content, "问题交接");
        } catch (Exception e) {
            log.error("发送交接系统消息报错：{}", e);
        }
    }

    @Override
    public void closeGroup(Long groupId, Integer issueId) {
        if (!closeGroupEnable) {
            return;
        }
        String errorHead = "| - > [ WebSocket ] - 关闭群组 - ";
        if (groupId == null) {
            log.error(errorHead + "群组id为空");
            return;
        }
        // 初始化信息载体
        SendWebSocketMessageDTO sendMessage = new SendWebSocketMessageDTO();
        sendMessage.setAction(ActionConstants.ROBOT_CLOSE_GROUP);
        // 拼装消息内容
        Long messageId = System.currentTimeMillis();
        SendWebSocketMessageDTO.MessageContent messageContent = WebSocketClientUtil.buildRobotMessage(robotName);
        messageContent.setGroupId(groupId);
        messageContent.setRandomCode(messageId);
        sendMessage.setContent(messageContent);
        // 记录
        log(messageId, issueId, sendMessage.getParam(), 0);
        try {
            reconnectWebSocketClient.send(JsonUtil.toJsonString(sendMessage));
        } catch (Exception e) {
            log.error(errorHead + "消息发送失败");
        }
    }

    private Map<String, PassportUserInfoDTO> getUserInfoMap(Collection<Integer> userList) {
        ServerResponse<List<PassportUserInfoDTO>> userInfoResponse = passportFeignClient.getUserMsgByIds(userList);
        if (userInfoResponse.getCode() != ResponseCode.SUCCESS.getCode()) {
            logAndThrowException("", "异常，通过权限中心获取用户信息失败");
        }
        Map<String, PassportUserInfoDTO> userInfoMap = new HashMap<>(16);
        if (CollectionUtils.isNotEmpty(userInfoResponse.getData())) {
            List<PassportUserInfoDTO> userInfoList = userInfoResponse.getData();
            userInfoList.forEach(user -> userInfoMap.put(String.valueOf(user.getId()), user));
        }
        return userInfoMap;
    }

    private void log(Long messageId, Integer issueId, String content, Integer createdBy) {
        WsApiLogEntity wsLog = new WsApiLogEntity();
        if (messageId != null) {
            wsLog.setMessageId(messageId.toString());
        }
        wsLog.setSendMessage(content);
        wsLog.setIssueId(issueId);
        wsLog.setCreatedBy(createdBy);
        wsLog.setCreatedOn(LocalDateTime.now());
        wsApiLogService.insertLog(wsLog);
    }

    private void logAndThrowException(String errorHead, String errMsg) {
        log.error(errorHead + errMsg);
        throw new GlobalRuntimeException(ResponseCode.INTERNAL_ERROR.getCode(), errMsg);
    }
}

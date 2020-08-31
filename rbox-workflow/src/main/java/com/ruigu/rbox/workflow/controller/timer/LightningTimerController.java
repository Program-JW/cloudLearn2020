package com.ruigu.rbox.workflow.controller.timer;

import com.ruigu.rbox.cloud.kanai.util.TimeUtil;
import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.manager.LightningIssueLogManager;
import com.ruigu.rbox.workflow.manager.LightningTimeoutLogManager;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.BaseTaskInfoDTO;
import com.ruigu.rbox.workflow.model.dto.LightningIssueIdInfoDTO;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.*;
import com.ruigu.rbox.workflow.model.request.lightning.LightningOverTimeReq;
import com.ruigu.rbox.workflow.model.vo.MessageInfoVO;
import com.ruigu.rbox.workflow.model.vo.TableDataVo;
import com.ruigu.rbox.workflow.repository.*;
import com.ruigu.rbox.workflow.service.*;
import com.ruigu.rbox.workflow.strategy.context.SendNoticeContext;
import com.ruigu.rbox.workflow.supports.NoticeContentUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liqingtian
 * @date 2019/12/30 10:41
 */
@Slf4j
@RestController
public class LightningTimerController {

    @Resource
    private WorkflowInstanceService workflowInstanceService;

    @Resource
    private WorkflowInstanceRepository workflowInstanceRepository;

    @Resource
    private WorkflowDefinitionService workflowDefinitionService;

    @Resource
    private NoticeLogService noticeLogService;

    @Resource
    private SendNoticeContext sendNoticeContext;

    @Resource
    private NoticeContentUtil noticeContentUtil;

    @Resource
    private NoticeConfigService noticeConfigService;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private LightningIssueService lightningIssueService;

    @Resource
    private LightningIssueApplyRepository lightningIssueApplyRepository;

    @Resource
    private LightningIssueLogManager lightningIssueLogManager;

    @Resource
    private LightningTimeoutLogManager lightningTimeoutLogManager;

    @Resource
    private LightningIssueLogRepository lightningIssueLogRepository;

    @Resource
    private LightningTimeoutLogRepository lightningTimeoutLogRepository;

    @Resource
    private ChatWebSocketService chatWebSocketService;

    @Resource
    private LightningIssueGroupRepository lightningIssueGroupRepository;

    @Resource
    private LightningIssueRelevantUserRepository lightningIssueRelevantUserRepository;

    @Resource
    private RuntimeService runtimeService;

    @Value("${rbox.workflow.definition.lightning}")
    private String lightningKey;

    @Value("${rbox.workflow.manage.group.id}")
    private String manageGroupId;

    /**
     * 入口思路：
     * 1 查询未完成任务，如果列表为空则进行下一步处理，如果不为空 则 遍历其businessKey
     * 2 查询待确认问题，如果为空则返回 如果不为空 则 遍历问题id (同busineeKey)
     * 3.合并问题id ,一次性查询所有日志
     * 4.处理未完成且超时的任务 （ 超时通知）
     * 5.处理待确认的问题 （自动确认）
     **/

    @GetMapping("/timeout/lightning")
    public ServerResponse checkTimeout() {

        log.info("| - > [ 闪电链 ] [ 超时提醒 ] -- 超时扫描开始 -- ");

        /**
         * 待确认超时处理
         */
        log.info("| - > [ 闪电链 ] [ 超时提醒 ] -- 待确认超时处理 [ 开始 ] -- ");
        toBeConfirmedHandle();
        log.info("| - > [ 闪电链 ] [ 超时提醒 ] -- 待确认超时处理 [ 结束 ] -- ");

        /**
         * 未完成超时处理
         */
        log.info("| - > [ 闪电链 ] [ 超时提醒 ] -- 未完成超时处理 [ 开始 ] -- ");
        incompleteHandle();
        log.info("| - > [ 闪电链 ] [ 超时提醒 ] -- 未完成超时处理 [ 结束 ] -- ");


        log.info("| - > [ 闪电链 ] [ 超时提醒 ] -- 超时扫描结束 -- ");
        return ServerResponse.ok();

    }

    /**
     * 因业务需求，超时处理将大致分为两步
     * <p>
     * 1 对于待确认的问题自动确认解决
     * <p>
     * <p>
     * 2 对于除待确认以外的未解决的问题的超时升级
     * 2.1 未解决又细分为 未受理 和 未完成 的问题
     */

    private void toBeConfirmedHandle() {
        // 查询待确认
        List<LightningIssueIdInfoDTO> toBeConfirmedList = lightningIssueApplyRepository.findAllNeedAutoConfirmIssue();
        if (CollectionUtils.isEmpty(toBeConfirmedList)) {
            return;
        }
        Map<String, LightningIssueIdInfoDTO> issueMap = toBeConfirmedList.stream().collect(Collectors.toMap(LightningIssueIdInfoDTO::getInstanceId, a -> a));
        // 查询日志
        List<Integer> toBeConfirmedIds = toBeConfirmedList.stream().map(LightningIssueIdInfoDTO::getIssueId).collect(Collectors.toList());
        List<LightningIssueLogEntity> allLogList = lightningIssueLogRepository.findAllByIssueIdInAndActionIn(toBeConfirmedIds,
                Arrays.asList(LightningIssueLogActionEnum.TIME_OUT_4.getCode(), LightningIssueLogActionEnum.TIME_OUT_24.getCode()));
        // 根据问题id进行分类
        Map<String, List<LightningIssueLogEntity>> logMap = allLogList.parallelStream().collect(Collectors.groupingBy(log -> String.valueOf(log.getIssueId())));
        // 查询对应流程
        List<String> businessKeyList = toBeConfirmedIds.stream().map(String::valueOf).collect(Collectors.toList());
        List<WorkflowInstanceEntity> instanceList = workflowInstanceRepository.findAllByDefinitionCodeAndBusinessKeyInAndStatusNot(lightningKey, businessKeyList, InstanceState.INVALID.getState());
        // 保存当前时间
        LocalDateTime now = LocalDateTime.now();
        // 查询距离创建时间已经多少小时
        List<LightningIssueIdInfoDTO> autoConfirmedIssueList = new ArrayList<>();
        instanceList.forEach(instance -> {
            LightningIssueIdInfoDTO issue = issueMap.get(instance.getId());
            if (issue == null) {
                return;
            }
            // 问题创建时间
            LocalDateTime issueCreatedOn = TimeUtil.date2LocalDateTime(instance.getCreatedOn());
            // 计算间隔时长
            long hours = Duration.between(issueCreatedOn, now).toHours();
            // 待确认状态问题是受理过的问题，最早的超时也应是4小时超时
            if (hours < TimeoutParam.TIME_OUT_FOUR_HOURS.getCode()) {
                return;
            }
            // 获取最大级别日志
            List<LightningIssueLogEntity> logList = logMap.get(instance.getBusinessKey());
            if (CollectionUtils.isEmpty(logList) && hours >= TimeoutParam.TIME_OUT_FOUR_HOURS.getCode()) {
                autoConfirmedIssueList.add(issue);
                return;
            }
            logList.sort(Comparator.comparing(LightningIssueLogEntity::getAction).reversed());
            Integer maxLevel = logList.get(0).getAction();
            // 处理思路略有不同
            // 1.如果创建时间未超过4小时，默认不做处理
            // 2.如果超过或等于4小时，小于24小时，则检查是否有4小时通知，如果有，则不做处理，没有则自动确认
            // 3.如果超过或等于24小时，小于48小时，则检查是否有24小时通知，如果有，则不做处理，没有则自动确认
            // 4.如果超过或等于48小时，则检查是否有48小时通知，如果有，则不处理，没有则自动确认
            if (LightningIssueLogActionEnum.TIME_OUT_4.getCode().equals(maxLevel)) {
                if (hours >= TimeoutParam.TIME_OUT_TWENTY_FOUR_HOURS.getCode()) {
                    autoConfirmedIssueList.add(issue);
                }
            } else if (LightningIssueLogActionEnum.TIME_OUT_24.getCode().equals(maxLevel)) {
                if (hours >= TimeoutParam.TIME_OUT_FORTY_EIGHT_HOURS.getCode()) {
                    autoConfirmedIssueList.add(issue);
                }
            }
        });
        if (CollectionUtils.isNotEmpty(autoConfirmedIssueList)) {
            lightningIssueService.batchSystemConfirmSolve(autoConfirmedIssueList);
        }
    }

    private void incompleteHandle() {
        List<WorkflowInstanceWithTaskEntity> timeoutInstanceWithTaskList = workflowInstanceService.getIncompleteInstanceWithTaskByDefinitionKeyAndTaskStatusIn(lightningKey,
                Arrays.asList(TaskState.UNTREATED.getState(), TaskState.RUNNING.getState()));
        // 获取所有未完成流程信息，若空则结束
        if (CollectionUtils.isEmpty(timeoutInstanceWithTaskList)) {
            return;
        }
        // 所有超时的实例id列表
        List<String> timeoutNotAcceptInstanceIds = new ArrayList<>();
        // 获取未完成流程中超时1小时以上且未受理的
        List<WorkflowInstanceWithTaskEntity> timeoutNotAcceptList = new ArrayList<>();
        // 获取未完成流程中超时4小时以上者，若空则结束
        List<WorkflowInstanceWithTaskEntity> timeoutNotCompleteList = new ArrayList<>();
        // 筛除未超时的
        timeoutInstanceWithTaskList.forEach(instance -> {
            LocalDateTime instanceCreatedOn = TimeUtil.date2LocalDateTime(instance.getCreatedOn());
            LocalDateTime taskCreatedOn = TimeUtil.date2LocalDateTime(instance.getTaskCreatedOn());
            if (instanceCreatedOn.plusHours(TimeoutParam.TIME_OUT_FOUR_HOURS.getCode()).isBefore(LocalDateTime.now())) {
                timeoutNotCompleteList.add(instance);
            } else if (instance.getTaskStatus().equals(TaskState.UNTREATED.getState())
                    && taskCreatedOn.plusHours(1).isBefore(LocalDateTime.now())) {
                timeoutNotAcceptList.add(instance);
                timeoutNotAcceptInstanceIds.add(instance.getId());
            }
        });
        if (CollectionUtils.isEmpty(timeoutNotAcceptList) && CollectionUtils.isEmpty(timeoutNotCompleteList)) {
            return;
        }

        // 获取超时未完成的流程实例的定义信息,查询不到则说明数据有问题，返回报错信息
        List<String> timeoutDefinitionIds = timeoutInstanceWithTaskList.stream()
                .map(WorkflowInstanceWithTaskEntity::getDefinitionId).distinct().collect(Collectors.toList());
        List<WorkflowDefinitionEntity> timeoutDefinitionList = workflowDefinitionService.getDefinitionByIds(timeoutDefinitionIds);
        if (CollectionUtils.isEmpty(timeoutDefinitionList)) {
            log.error("无法查询到超时流程的定义信息");
            return;
        }
        // 手动释放内存
        timeoutInstanceWithTaskList = null;

        // 获取发送通知模板及信息列表
        List<LightningTimeoutNoticeInfo> timeoutNoticeInfoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(timeoutNotAcceptList)) {
            // 获取超时通知
            List<NoticeEntity> notAcceptNoticeList = noticeLogService.getNoticesByInstanceIdInAndTypeIn(timeoutNotAcceptInstanceIds,
                    Collections.singletonList(InstanceEvent.TIME_OUT_BEGIN_URGE.getCode()));
            timeoutNoticeInfoList.addAll(notAcceptHandle(timeoutNotAcceptList, notAcceptNoticeList, timeoutDefinitionList));
        }
        if (CollectionUtils.isNotEmpty(timeoutNotCompleteList)) {
            timeoutNoticeInfoList.addAll(notCompleteHandle(timeoutNotCompleteList, timeoutDefinitionList));
        }
        if (CollectionUtils.isEmpty(timeoutNoticeInfoList)) {
            return;
        }

        // 循环获取超时信息 和 businessKey
        List<Integer> businessKeyList = new ArrayList<>();
        Set<Integer> timeoutUserIdList = new HashSet<>();
        timeoutNoticeInfoList.forEach(info -> {
            timeoutUserIdList.add(info.getUserId());
            if (StringUtils.isNotBlank(info.getBusinessKey())) {
                businessKeyList.add(Integer.valueOf(info.getBusinessKey()));
            }
        });

        // feign 获取超时领导信息
        ServerResponse<Map<Integer, List<List<PassportUserInfoDTO>>>> timeoutLeaderResponse = passportFeignClient.getUserAllLeaderInfo(timeoutUserIdList);
        if (timeoutLeaderResponse.getCode() != ResponseCode.SUCCESS.getCode()) {
            log.error("通过权限中心获取领导信息失败");
            return;
        }
        Map<Integer, List<List<PassportUserInfoDTO>>> timeoutLeaderInfo = timeoutLeaderResponse.getData();

        // 获取总经办领导id集合
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("groupId", manageGroupId);
        map.add("orderByColumn", "id");
        ServerResponse<TableDataVo> userListByGroupId = passportFeignClient.getUserListByGroupId(map);
        if (timeoutLeaderResponse.getCode() != ResponseCode.SUCCESS.getCode()) {
            log.error("通过权限中心获取总经办人员信息失败");
            return;
        }
        List<LinkedHashMap<String, Object>> rows = (List<LinkedHashMap<String, Object>>) userListByGroupId.getData().getRows();
        List<Integer> managerLeaderIdList = rows.stream().map(info -> Integer.valueOf(String.valueOf(info.get("id")))).collect(Collectors.toList());

        // 查找问题对应的群信息
        List<LightningIssueGroupEntity> groupInfoList = lightningIssueGroupRepository.findAllByIssueIdIn(businessKeyList);
        Map<String, String> groupInfoMap = new HashMap<>(16);
        groupInfoList.forEach(group -> groupInfoMap.put(group.getIssueId().toString(), group.getGroupId()));

        // 发送通知并加入群组
        sendNoticeAndAddLeader(timeoutNoticeInfoList, timeoutLeaderInfo, managerLeaderIdList, groupInfoMap);
    }


    /**
     * 处理没受理超时通知
     */
    private List<LightningTimeoutNoticeInfo> notAcceptHandle(List<WorkflowInstanceWithTaskEntity> notAcceptList,
                                                             List<NoticeEntity> notAcceptNoticeList,
                                                             List<WorkflowDefinitionEntity> definitionList) {
        List<LightningTimeoutNoticeInfo> noticeInfoList = new ArrayList<>();
        notAcceptList.forEach(instance -> {
            String instanceId = instance.getId();
            // 查询该流程超时次数
            List<NoticeEntity> noticeList = notAcceptNoticeList.stream()
                    .filter(n -> n.getInstanceId().equals(instance.getId()) && n.getTaskId().equals(instance.getTaskId()))
                    .collect(Collectors.toList());
            Integer timeoutCount = noticeList.size();
            if (timeoutCount > 0) {
                return;
            }
            LightningTimeoutNoticeInfo info = new LightningTimeoutNoticeInfo();
            // 设置超时通知人
            Integer timeoutUserId = Integer.valueOf(instance.getCandidateUsers());
            info.setUserId(timeoutUserId);
            info.setTimeout(TimeoutParam.TIME_OUT_FOUR_HOURS.getCode());
            WorkflowDefinitionEntity definition = definitionList.stream()
                    .filter(d -> instance.getDefinitionId().equals(d.getId())).findFirst()
                    .orElse(null);
            if (definition == null) {
                return;
            }
            // 基础任务
            BaseTaskInfoDTO baseTask = new BaseTaskInfoDTO(instanceId, instance.getTaskId(), instance.getTaskName(), instance.getTaskCreatedOn());
            // 流程变量
            Map<String, Object> variables = runtimeService.getVariables(instance.getId());
            // 催办模板
            List<NoticeTemplateEntity> urgeTemplates = noticeConfigService.getNoticeTemplate(NoticeConfigState.NODE,
                    instance.getNodeId(), InstanceEvent.TIME_OUT_BEGIN_URGE.getCode());
            if (CollectionUtils.isEmpty(urgeTemplates)) {
                return;
            }
            NoticeTemplateEntity urgeTemplate = urgeTemplates.get(0);
            // 设置催办模板信息
            info.setUrgeTemplate(urgeTemplate);
            MessageInfoVO urgeMessageInfo = noticeContentUtil.translateNodeTemplate(urgeTemplate, definition, baseTask, variables);
            urgeMessageInfo.setTargets(Collections.singleton(timeoutUserId));
            urgeMessageInfo.setNoticeEventType(InstanceEvent.TIME_OUT_BEGIN_URGE.getCode());
            info.setUrgeMessageInfo(urgeMessageInfo);
            // 设置领导超时
            List<NoticeTemplateEntity> noticeTemplates = noticeConfigService.getNoticeTemplate(NoticeConfigState.DEFINITION,
                    instance.getDefinitionId(), InstanceEvent.TIME_OUT_BEGIN.getCode());
            NoticeTemplateEntity leaderTemplate = noticeTemplates.stream()
                    .filter(t -> t.getChannel() == NoticeType.WEIXIN_CHANNEL.getState()
                            && t.getType() == NoticeType.TASK_CARD.getState()).findFirst()
                    .orElse(null);
            if (leaderTemplate == null) {
                return;
            }
            info.setLeaderTemplate(leaderTemplate);
            MessageInfoVO leaderMessageInfo = noticeContentUtil.translateDefinitionTemplate(leaderTemplate, definition, instanceId, variables);
            leaderMessageInfo.setTaskId(baseTask.getId());
            leaderMessageInfo.setNoticeEventType(InstanceEvent.TIME_OUT_BEGIN.getCode());
            info.setLeaderMessageInfo(leaderMessageInfo);
            info.setInstanceId(instanceId);
            String businessKey = instance.getBusinessKey();
            info.setBusinessKey(businessKey);
            // 新增闪电链超时日志
            LightningTimeoutLogEntity timeoutLog = lightningTimeoutLogManager.returnTimeoutLog(Integer.valueOf(businessKey), LightningTimeoutTypeEnum.NOT_ACCEPTED.getCode(), timeoutUserId);
            timeoutLog.setTimeout(TimeoutParam.TIME_OUT_FOUR_HOURS.getCode());
            timeoutLog.setLevel(TimeoutParam.TIME_OUT_FOUR_HOURS.getCode());
            info.setTimeoutLog(timeoutLog);
            noticeInfoList.add(info);
        });
        return noticeInfoList;
    }

    /**
     * 处理没完成超时通知
     */
    private List<LightningTimeoutNoticeInfo> notCompleteHandle(List<WorkflowInstanceWithTaskEntity> notCompleteList,
                                                               List<WorkflowDefinitionEntity> definitionList) {
        List<LightningOverTimeReq> overTimeReqList = new ArrayList<>();
        List<LightningTimeoutNoticeInfo> noticeInfoList = new ArrayList<>();
        // 获取问题id
        List<Integer> issueList = notCompleteList.stream()
                .map(instance -> Integer.valueOf(instance.getBusinessKey()))
                .collect(Collectors.toList());
        // 查找所有的
        List<LightningIssueLogEntity> logList = lightningIssueLogRepository.findAllByIssueIdInAndActionIn(issueList,
                Arrays.asList(LightningIssueLogActionEnum.TIME_OUT_4.getCode(),
                        LightningIssueLogActionEnum.TIME_OUT_24.getCode(),
                        LightningIssueLogActionEnum.TIME_OUT_48.getCode()));
        // 根据问题id进行分类
        Map<String, List<LightningIssueLogEntity>> logMap = logList.parallelStream()
                .collect(Collectors.groupingBy(log -> String.valueOf(log.getIssueId())));
        // 进行判断
        notCompleteList.forEach(instance -> {
            String instanceId = instance.getId();
            LocalDateTime instanceCreatedOn = TimeUtil.date2LocalDateTime(instance.getCreatedOn());
            Long timeoutHours = Duration.between(instanceCreatedOn, LocalDateTime.now()).toHours();
            List<LightningIssueLogEntity> timeoutLogs = logMap.get(instance.getBusinessKey());
            boolean firstNoticeSendFlag = timeoutHours >= 4 && timeoutHours < 24;
            boolean secondNoticeSendFlag = timeoutHours >= 24 && timeoutHours < 48;
            boolean thirdNoticeFlag = timeoutHours >= 48;
            if (CollectionUtils.isNotEmpty(timeoutLogs)) {
                // 查询当前日志状态
                timeoutLogs.sort(Comparator.comparing(LightningIssueLogEntity::getAction).reversed());
                Integer maxLevel = timeoutLogs.get(0).getAction();
                if (LightningIssueLogActionEnum.TIME_OUT_4.getCode().equals(maxLevel)) {
                    firstNoticeSendFlag = false;
                } else if (LightningIssueLogActionEnum.TIME_OUT_24.getCode().equals(maxLevel)) {
                    firstNoticeSendFlag = false;
                    secondNoticeSendFlag = false;
                } else if (LightningIssueLogActionEnum.TIME_OUT_48.getCode().equals(maxLevel)) {
                    firstNoticeSendFlag = false;
                    secondNoticeSendFlag = false;
                    thirdNoticeFlag = false;
                }
            }
            if (firstNoticeSendFlag || secondNoticeSendFlag || thirdNoticeFlag) {
                LightningTimeoutNoticeInfo info = new LightningTimeoutNoticeInfo();
                info.setInstanceId(instanceId);
                String businessKey = instance.getBusinessKey();
                info.setBusinessKey(businessKey);
                // 设置超时通知人
                Integer timeoutUserId = Integer.valueOf(instance.getCandidateUsers());
                info.setUserId(timeoutUserId);
                // 筛选该实例流程定义信息
                WorkflowDefinitionEntity definition = definitionList.stream()
                        .filter(d -> instance.getDefinitionId().equals(d.getId())).findFirst()
                        .orElse(null);
                if (definition == null) {
                    return;
                }
                // 基础任务
                BaseTaskInfoDTO baseTask = new BaseTaskInfoDTO(instanceId, instance.getTaskId(), instance.getTaskName(), instance.getTaskCreatedOn());
                // 流程变量
                Map<String, Object> variables = runtimeService.getVariables(instance.getId());
                // 催办模板
                List<NoticeTemplateEntity> urgeTemplates = noticeConfigService.getNoticeTemplate(NoticeConfigState.NODE,
                        instance.getNodeId(), InstanceEvent.URGE.getCode());
                if (CollectionUtils.isEmpty(urgeTemplates)) {
                    return;
                }
                NoticeTemplateEntity urgeTemplate = urgeTemplates.get(0);
                // 设置催办模板信息
                info.setUrgeTemplate(urgeTemplate);
                MessageInfoVO urgeMessageInfo = noticeContentUtil.translateNodeTemplate(urgeTemplate, definition, baseTask, variables);
                urgeMessageInfo.setTargets(Collections.singleton(timeoutUserId));
                urgeMessageInfo.setNoticeEventType(InstanceEvent.URGE.getCode());
                info.setUrgeMessageInfo(urgeMessageInfo);
                // 超时领导通知
                List<NoticeTemplateEntity> noticeTemplates = noticeConfigService.getNoticeTemplate(NoticeConfigState.DEFINITION,
                        instance.getDefinitionId(), InstanceEvent.TIME_OUT.getCode());
                NoticeTemplateEntity leaderTemplate = noticeTemplates.stream()
                        .filter(t -> t.getChannel() == NoticeType.WEIXIN_CHANNEL.getState()
                                && t.getType() == NoticeType.TASK_CARD.getState()).findFirst()
                        .orElse(null);
                if (leaderTemplate == null) {
                    return;
                }
                info.setLeaderTemplate(leaderTemplate);
                MessageInfoVO leaderMessageInfo = noticeContentUtil.translateDefinitionTemplate(leaderTemplate, definition, instanceId, variables);
                leaderMessageInfo.setTaskId(baseTask.getId());
                leaderMessageInfo.setNoticeEventType(InstanceEvent.TIME_OUT.getCode());
                info.setLeaderMessageInfo(leaderMessageInfo);
                // 闪电链超时日志请求参数
                LightningOverTimeReq req = new LightningOverTimeReq();
                req.setIssueId(Integer.valueOf(instance.getBusinessKey()));
                req.setCreatedBy(timeoutUserId);
                // 新增闪电链超时日志
                LightningTimeoutLogEntity timeoutLog = lightningTimeoutLogManager.returnTimeoutLog(Integer.valueOf(businessKey), LightningTimeoutTypeEnum.INCOMPLETE.getCode(), timeoutUserId);
                if (firstNoticeSendFlag) { // 没有发送过通知
                    // 发送直属领导
                    info.setTimeout(TimeoutParam.TIME_OUT_FOUR_HOURS.getCode());
                    req.setAction(LightningIssueLogActionEnum.TIME_OUT_4.getCode());
                    timeoutLog.setLevel(TimeoutParam.TIME_OUT_FOUR_HOURS.getCode());
                } else if (secondNoticeSendFlag) { // 4 小时通知已发
                    // 发送总经办领导
                    info.setTimeout(TimeoutParam.TIME_OUT_TWENTY_FOUR_HOURS.getCode());
                    req.setAction(LightningIssueLogActionEnum.TIME_OUT_24.getCode());
                    timeoutLog.setLevel(TimeoutParam.TIME_OUT_TWENTY_FOUR_HOURS.getCode());
                } else { // 24 小时通知已发
                    // 发送 CEO
                    info.setTimeout(TimeoutParam.TIME_OUT_FORTY_EIGHT_HOURS.getCode());
                    req.setAction(LightningIssueLogActionEnum.TIME_OUT_48.getCode());
                    timeoutLog.setLevel(TimeoutParam.TIME_OUT_FORTY_EIGHT_HOURS.getCode());
                }
                timeoutLog.setTimeout(timeoutHours.intValue());
                info.setTimeoutLog(timeoutLog);
                overTimeReqList.add(req);
                noticeInfoList.add(info);
            }
        });
        // 保存日志
        lightningIssueLogManager.saveIssueLogOverTime(overTimeReqList);
        return noticeInfoList;
    }

    /**
     * 发送领导通知，并将领导加入群组
     */
    private void sendNoticeAndAddLeader(List<LightningTimeoutNoticeInfo> timeoutNoticeInfoList,
                                        Map<Integer, List<List<PassportUserInfoDTO>>> timeoutLeaderInfo,
                                        List<Integer> managerLeaderIdList,
                                        Map<String, String> groupInfoMap) {

        timeoutNoticeInfoList.forEach(noticeInfo -> {
            // 超时日志
            LightningTimeoutLogEntity timeoutLog = noticeInfo.getTimeoutLog();
            // 获取领导
            List<PassportUserInfoDTO> leaders = getLeaderByLevel(noticeInfo.getUserId(), noticeInfo.getTimeout(), managerLeaderIdList, timeoutLeaderInfo);
            // 如果有升级的领导
            if (CollectionUtils.isNotEmpty(leaders)) {
                // 加入升级领导信息
                StringBuilder leaderName = new StringBuilder();
                for (PassportUserInfoDTO leader : leaders) {
                    leaderName.append(leader.getNickname()).append(" ");
                }
                if (StringUtils.isNotBlank(leaderName.toString())) {
                    String leaderInfo = "\n升级领导至：" + leaderName.toString();
                    noticeInfo.getUrgeMessageInfo().setDescription(noticeInfo.getUrgeMessageInfo().getDescription() + leaderInfo);
                    noticeInfo.getLeaderMessageInfo().setDescription(noticeInfo.getLeaderMessageInfo().getDescription() + leaderInfo);
                }
                List<Integer> leaderIdList = leaders.stream().map(PassportUserInfoDTO::getId).collect(Collectors.toList());
                // 加入并维护
                String businessKey = noticeInfo.getBusinessKey();
                String groupId = groupInfoMap.get(noticeInfo.getBusinessKey());
                addLeader(businessKey, leaderIdList, groupId);
                // 发送超时通知
                MessageInfoVO leaderMessageInfo = noticeInfo.getLeaderMessageInfo();
                leaderMessageInfo.setTargets(leaderIdList);
                sendNoticeContext.send(noticeInfo.getLeaderTemplate(), leaderMessageInfo);
                // 日志添加数据继
                leaderIdList.forEach(leaderId -> {
                    timeoutLog.setUserId(leaderId);
                    lightningTimeoutLogRepository.save(timeoutLog);
                });
            } else {
                timeoutLog.setUserId(timeoutLog.getCreatedBy());
                lightningTimeoutLogRepository.save(timeoutLog);
            }
            // 催办
            sendNoticeContext.send(noticeInfo.getUrgeTemplate(), noticeInfo.urgeMessageInfo);
        });
    }

    /**
     * 群组添加领导
     */
    private void addLeader(String businessKey, List<Integer> leaderIds, String groupId) {
        // 检测人员表，是否存在
        if (StringUtils.isNotBlank(businessKey) && StringUtils.isNotBlank(groupId)) {
            Integer issueId = Integer.valueOf(businessKey);
            leaderIds.forEach(leaderId -> {
                // 检验是否已存在记录
                LightningIssueRelevantUserEntity record = lightningIssueRelevantUserRepository.findByIssueIdAndUserId(issueId, leaderId);
                if (record == null) {
                    record = new LightningIssueRelevantUserEntity();
                    record.setIssueId(issueId);
                    record.setUserId(leaderId);
                    record.setCreatedBy(0);
                    record.setCreatedOn(new Date());
                    record.setLastUpdatedBy(0);
                    record.setLastUpdatedOn(new Date());
                    try {
                        // 发送入群通知
                        chatWebSocketService.addUserToGroup(issueId, Long.valueOf(groupId), 0, leaderId);
                        // 维护人员表
                        lightningIssueRelevantUserRepository.save(record);
                    } catch (Exception e) {
                        log.error("超时：领导加入群组消息发送失败。 {}", e);
                    }
                }
            });
        }
    }

    private List<PassportUserInfoDTO> getLeaderByLevel(Integer userId, Integer level, List<Integer> managerLeaderIdList, Map<Integer, List<List<PassportUserInfoDTO>>> leaderInfo) {
        if (leaderInfo == null || leaderInfo.isEmpty()) {
            return new ArrayList<>();
        }
        List<List<PassportUserInfoDTO>> leaderList = leaderInfo.get(userId);
        if (CollectionUtils.isEmpty(leaderList)) {
            return new ArrayList<>();
        }
        // 标志位：是否是总经办领导
        boolean isManagerLeader = managerLeaderIdList.stream().anyMatch(id -> id.equals(userId));
        // 排除超时受理人未 ceo
        int size = leaderList.size();
        if (size == 1) {
            List<PassportUserInfoDTO> leader = leaderList.get(0);
            if (leader.stream().anyMatch(user -> user.getId().equals(userId))) {
                return new ArrayList<>();
            }
        }
        // 除ceo外的人都应满足以下关系
        if (TimeoutParam.TIME_OUT_FOUR_HOURS.getCode() == level) {
            // 4小时超时
            // 已排除总经办
            if (!isManagerLeader) {
                if (size < 3) {
                    // size = 1 或者 size = 2 时 说明是 直属ceo或者直属总经办管理
                    return new ArrayList<>();
                } else {
                    // 获取第一级领导
                    List<PassportUserInfoDTO> firstLeader = leaderList.get(0);
                    // 如果本身就是第一级领导 则选择第二级领导，
                    if (firstLeader.stream().anyMatch(user -> user.getId().equals(userId))) {
                        List<PassportUserInfoDTO> secondLeader = leaderList.get(1);
                        // 如果第二级领导不是总经办则返回
                        List<PassportUserInfoDTO> sameId = secondLeader.stream()
                                .filter(user -> managerLeaderIdList.contains(user.getId()))
                                .collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(sameId)) {
                            return secondLeader;
                        }
                        return new ArrayList<>();
                    }
                    return firstLeader;
                }
            }
            return new ArrayList<>();
        } else if (TimeoutParam.TIME_OUT_TWENTY_FOUR_HOURS.getCode() == level) {
            // 24小时超时找总经办
            // 已排除总经办
            if (!isManagerLeader) {
                if (size == 1) {
                    // 如果size=1 又不是总经办领导，说明是直属ceo管辖,直属ceo的24小时需要通知领导
                    return leaderList.get(0);
                } else if (size > 1) {
                    // 即size大于1时,说明是非ceo管辖，有总经办领导的
                    return leaderList.get(size - 2);
                }
            }
            return new ArrayList<>();
        } else {
            // 48小时超时：ceo (最后一个)
            return leaderList.get(size - 1);
        }
    }

    @Data
    private class LightningTimeoutNoticeInfo {
        private NoticeTemplateEntity urgeTemplate;
        private MessageInfoVO urgeMessageInfo;
        private NoticeTemplateEntity leaderTemplate;
        private MessageInfoVO leaderMessageInfo;
        private String instanceId;
        private String businessKey;
        private Integer userId;
        private Integer timeout;
        private LightningTimeoutLogEntity timeoutLog;
    }

    @GetMapping("/timeout/lightning/select/leader")
    public ServerResponse queryLeaderByLevel(@Param("userId") Integer userId, @Param("level") Integer level) {

        // feign 获取超时领导信息
        ServerResponse<Map<Integer, List<List<PassportUserInfoDTO>>>> timeoutLeaderResponse = passportFeignClient.getUserAllLeaderInfo(Collections.singleton(userId));
        if (timeoutLeaderResponse.getCode() != ResponseCode.SUCCESS.getCode()) {
            String errMsg = "通过权限中心获取领导信息失败";
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), errMsg);
        }
        Map<Integer, List<List<PassportUserInfoDTO>>> timeoutLeaderInfo = timeoutLeaderResponse.getData();

        // 获取总经办领导id集合
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("groupId", manageGroupId);
        map.add("orderByColumn", "id");
        ServerResponse<TableDataVo> userListByGroupId = passportFeignClient.getUserListByGroupId(map);
        if (timeoutLeaderResponse.getCode() != ResponseCode.SUCCESS.getCode()) {
            String errMsg = "通过权限中心获取总经办人员信息失败";
            return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(), errMsg);
        }
        List<LinkedHashMap<String, Object>> rows = (List<LinkedHashMap<String, Object>>) userListByGroupId.getData().getRows();
        List<Integer> managerLeaderIdList = rows.stream().map(info -> Integer.valueOf(String.valueOf(info.get("id")))).collect(Collectors.toList());

        List<PassportUserInfoDTO> leaderByLevel = getLeaderByLevel(userId, level, managerLeaderIdList, timeoutLeaderInfo);
        return ServerResponse.ok(leaderByLevel);
    }
}

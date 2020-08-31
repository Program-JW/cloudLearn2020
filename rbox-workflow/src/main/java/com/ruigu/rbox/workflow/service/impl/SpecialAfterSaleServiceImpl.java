package com.ruigu.rbox.workflow.service.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruigu.rbox.cloud.kanai.model.YesOrNoEnum;
import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.cloud.kanai.util.TimeUtil;
import com.ruigu.rbox.workflow.constants.RedisKeyConstants;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.manager.SpecialAfterSaleApplyManager;
import com.ruigu.rbox.workflow.manager.SpecialAfterSaleLogManager;
import com.ruigu.rbox.workflow.manager.UserManager;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.dto.SpecialAfterSaleGroupQuotaDTO;
import com.ruigu.rbox.workflow.model.dto.SpecialAfterSaleReviewPositionDTO;
import com.ruigu.rbox.workflow.model.dto.UserGroupSimpleDTO;
import com.ruigu.rbox.workflow.model.entity.*;
import com.ruigu.rbox.workflow.model.enums.*;
import com.ruigu.rbox.workflow.model.request.*;
import com.ruigu.rbox.workflow.model.request.lightning.AddSpecialAfterSaleApplyRequest;
import com.ruigu.rbox.workflow.model.vo.*;
import com.ruigu.rbox.workflow.repository.*;
import com.ruigu.rbox.workflow.service.*;
import com.ruigu.rbox.workflow.supports.ObjectUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author liqingtian
 * @date 2020/08/11 14:55
 */
@Service
public class SpecialAfterSaleServiceImpl implements SpecialAfterSaleService {

    @Resource
    private WorkflowInstanceService workflowInstanceService;
    @Resource
    private WorkflowTaskService workflowTaskService;
    @Resource
    private SpecialAfterSaleApplyManager specialAfterSaleApplyManager;
    @Resource
    private SpecialAfterSaleApplyRepository specialAfterSaleApplyRepository;
    @Resource
    private SpecialAfterSaleDetailRepository specialAfterSaleDetailRepository;
    @Autowired
    private SpecialAfterSaleApplyRepository repository;
    @Resource
    private SpecialAfterSaleReviewNodeRepository specialAfterSaleReviewNodeRepository;
    @Resource
    private SpecialAfterSaleLogManager specialAfterSaleLogManager;
    @Resource
    private SpecialAfterSaleConfigService specialAfterSaleConfigService;
    @Resource
    private SpecialAfterSaleQuotaService specialAfterSaleQuotaService;
    @Resource
    private PassportFeignManager passportFeignManager;
    @Resource
    private UserManager userManager;
    @Resource
    private QuestNoticeService questNoticeService;
    @Value("${rbox.workflow.definition.special-after-sale}")
    private String sasDefinitionKey;

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private SpecialAfterSaleApplyApproverRepository specialAfterSaleApplyApproverRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final Map<Integer, String> logActionDict;

    private final Map<Integer, String> applyStatusDict;

    @Resource
    private SpecialAfterSaleCcListRepository specialAfterSaleCcListRepository;

    {
        logActionDict = new HashMap<>(16);
        for (SpecialAfterSaleLogActionEnum action : SpecialAfterSaleLogActionEnum.values()) {
            logActionDict.put(action.getCode(), action.getDesc());
        }
        applyStatusDict = new HashMap<>(4);
        for (SpecialAfterSaleApplyStatusEnum action : SpecialAfterSaleApplyStatusEnum.values()) {
            applyStatusDict.put(action.getCode(), action.getDesc());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void apply(AddSpecialAfterSaleApplyRequest request) {
        // 1. 必要校验 是否有可用的
        // 获取当前登录人信息
        Integer userId = UserHelper.getUserId();
        UserGroupSimpleDTO userInfo = userManager.searchUserGroupFromCache(userId);
        String position = userInfo.getPosition();
        if (StringUtils.isBlank(position)) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "当前申请人信息缺失-职位");
        }
        List<UserGroupSimpleDTO.GroupInfoVO> groups = userInfo.getGroups();
        if (CollectionUtils.isEmpty(groups)) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "当前申请人信息缺失-所属部门");
        }
        // 查询匹配规则
        List<SpecialAfterSaleReviewPositionDTO> matchReviewList = specialAfterSaleConfigService.matchConfigs(userInfo);
        if (CollectionUtils.isEmpty(matchReviewList)) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "查询不到相应审批规则，无法申请");
        }
        // 确定使用的 （默认使用第一个）
        SpecialAfterSaleReviewPositionDTO useReview = matchReviewList.get(0);
        // 2. 保存数据
        Integer configId = useReview.getConfigId();
        SpecialAfterSaleApplyEntity apply = specialAfterSaleApplyManager.saveApply(request, configId, userId, userInfo.getNickname());
        Long applyId = apply.getId();
        // 3. 保存日志
        // 保存发起日志
        specialAfterSaleLogManager.createActionLog(applyId, SpecialAfterSaleLogActionEnum.START.getValue(), null,
                SpecialAfterSaleLogActionEnum.START.getCode(), null, YesOrNoEnum.YES.getCode(), userId);
        // 通过申请人职位做相应处理
        if (PositionEnum.DX.getPosition().equals(position)) {
            apply.setApplyUserType(SpecialAfterSaleApplyUserTypeEnum.DX.getCode());
            // 查询电销主管
            List<List<PassportUserInfoDTO>> allLeader = passportFeignManager.getAllLeader(userId);
            PassportUserInfoDTO dxManager = getDxManager(allLeader);
            if (dxManager == null) {
                throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "查询不到相应电销主管信息，无法申请");
            }
            Integer dxManagerId = dxManager.getId();
            // 取第一个配置节点
            SpecialAfterSaleReviewNodeEntity startNode = specialAfterSaleReviewNodeRepository.findTopByConfigIdOrderBySort(configId);
            // 打电销主管转审日志
            specialAfterSaleLogManager.createActionLog(applyId, SpecialAfterSaleLogActionEnum.DX_MANAGER_TRANSFER.getValue(), startNode.getId(),
                    SpecialAfterSaleLogActionEnum.DX_MANAGER_TRANSFER.getCode(), null, YesOrNoEnum.YES.getCode(), dxManagerId);
            // 维护当前申请审批人
            specialAfterSaleApplyManager.saveCurrentApprover(applyId, Collections.singletonList(dxManagerId));
            // 如果是电销，则事务提交后发送通知
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // todo 完善通知发送
                    String definitionName = "特殊售后审批申请";
                    String title = apply.getApplyNickname() + "的" + definitionName;
                    String content = "申请金额：" + apply.getTotalApplyAmount() +
                            "申请理由：" + apply.getApplyReason() +
                            "申请时间：" + apply.getCreatedAt();
                    String url = "http://www.baidu.com";
                    // todo 电销主管
                    questNoticeService.sendTextCardMultipleApp(EnvelopeChannelEnum.SPECIAL_AFTER_SALE, title, content, url, Collections.singleton(dxManagerId));
                }
            });
        } else {
            apply.setApplyUserType(SpecialAfterSaleApplyUserTypeEnum.BD.getCode());
            // 如果是销售，则开启流程
            Map<String, Object> var = new HashMap<>();
            var.put(SpecialAfterSaleUseVariableEnum.APPLY_ID.getCode(), applyId);
            var.put(SpecialAfterSaleUseVariableEnum.APPLY_USER_ID.getCode(), apply.getCreatedBy());
            var.put(SpecialAfterSaleUseVariableEnum.APPLY_USER_TYPE.getCode(), SpecialAfterSaleApplyUserTypeEnum.BD.getCode());
            var.put(SpecialAfterSaleUseVariableEnum.APPLY_REASON.getCode(), apply.getApplyReason());
            var.put(SpecialAfterSaleUseVariableEnum.APPLY_TIME.getCode(), TimeUtil.format(new Date(), TimeUtil.FORMAT_DATE_TIME));
            var.put(SpecialAfterSaleUseVariableEnum.CONFIG_ID.getCode(), configId);
            var.put(SpecialAfterSaleUseVariableEnum.APPLY_TOTAL_AMOUNT.getCode(), apply.getTotalApplyAmount().toString());
            DefinitionAndInstanceIdVO workflowInfo = workflowInstanceService.startExternalCall(buildStartReq(applyId, userId, var), userId);
            apply.setDefinitionId(workflowInfo.getDefinitionId());
            apply.setInstanceId(workflowInfo.getInstanceId());
        }
        specialAfterSaleApplyManager.saveApply(apply);
    }

    @Override
    public Page<SpecialAfterSaleSimpleApplyVO> queryListMyApproved(SpecialAfterSaleSearchRequest request) {
        QueryResults<SpecialAfterSaleApplyEntity> results = specialAfterSaleApplyRepository.queryListMyApproved(request);
        return convertApplySimpleVoPage(PageRequest.of(request.getPage(), request.getSize()), results.getTotal(), results.getResults());
    }

    @Override
    public Page<SpecialAfterSaleSimpleApplyVO> queryListMyPendingApproval(SpecialAfterSaleSearchRequest request) {
        QueryResults<SpecialAfterSaleApplyEntity> results = specialAfterSaleApplyRepository.queryListMyPendingApprove(request);
        return convertApplySimpleVoPage(PageRequest.of(request.getPage(), request.getSize()), results.getTotal(), results.getResults());
    }

    @Override
    public SpecialAfterSaleDetailApplyVO detail(Long applyId) {
        // 查询主体信息
        SpecialAfterSaleApplyEntity apply = specialAfterSaleApplyRepository.findById(applyId)
                .orElseThrow(() -> new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "查询不到该申请详情"));
        SpecialAfterSaleDetailApplyVO result = new SpecialAfterSaleDetailApplyVO();
        // set
        ObjectUtil.extendObject(result, apply, true);
        // 查询申请人信息
        UserGroupSimpleDTO userGroupInfo = userManager.searchUserGroupFromCache(apply.getCreatedBy());
        result.setCreatorId(userGroupInfo.getUserId());
        result.setCreatorName(userGroupInfo.getNickname());
        List<UserGroupSimpleDTO.GroupInfoVO> groups = userGroupInfo.getGroups();
        if (CollectionUtils.isNotEmpty(groups)) {
            result.setCreatorGroupName(groups.get(0).getGroupDecs());
        }
        // 查询详情
        result.setDetails(specialAfterSaleDetailRepository.findAllByApplyId(applyId));
        // 查询流程相关信息
        String instanceId = apply.getInstanceId();
        result.setInstanceId(instanceId);
        TaskEntity currentTask = workflowTaskService.getCurrentTaskByInstanceId(instanceId);
        if (Objects.nonNull(currentTask)) {
            result.setTaskId(currentTask.getId());
        }
        List<Integer> approverIdList = specialAfterSaleApplyManager.queryCurrentApprover(applyId);
        result.setCurrentApproverIdList(approverIdList);
        // 查询日志
        List<SpecialAfterSaleLogVO> actionLogs = specialAfterSaleLogManager.queryListLog(applyId);
        result.setLogs(actionLogs);
        // 判断是否电销节点
        SpecialAfterSaleLogVO lastLog = actionLogs.get(actionLogs.size() - 1);
        final boolean isDxManagerNode = SpecialAfterSaleLogActionEnum.DX_MANAGER_TRANSFER.getCode() == lastLog.getAction();
        if (isDxManagerNode) {
            result.setDxManagerNode(YesOrNoEnum.YES.getCode());
        } else {
            result.setDxManagerNode(YesOrNoEnum.NO.getCode());
        }
        // 节点信息
        Integer reviewNodeId = lastLog.getReviewNodeId();
        if (Objects.nonNull(reviewNodeId)) {
            SpecialAfterSaleReviewNodeEntity reviewNode = specialAfterSaleReviewNodeRepository.findById(reviewNodeId)
                    .orElseThrow(() -> new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "配置节点信息丢失 "));
            result.setReviewNodeInfo(reviewNode);
            if (reviewNode.getUseQuota() == YesOrNoEnum.YES.getCode()) {
                Map<Integer, List<SpecialAfterSaleGroupQuotaDTO>> userQuotaMap = specialAfterSaleQuotaService.queryQuotaByUserId(approverIdList, apply.getApplyUserType());
                result.setApproverQuota(userQuotaMap);
            }
        } else if (isDxManagerNode) {
            SpecialAfterSaleReviewNodeEntity startNode = specialAfterSaleReviewNodeRepository.findTopByConfigIdOrderBySort(apply.getConfigId());
            result.setReviewNodeInfo(startNode);
        }
        // 字典
        result.setDictionaries(logActionDict);
        return result;
    }

    @Override
    public SpecialAfterSaleDetailApplyPcVO pcDetail(Long applyId) {
        // 查询申请信息
        SpecialAfterSaleApplyEntity apply = specialAfterSaleApplyRepository.findById(applyId)
                .orElseThrow(() -> new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "查询不到该申请详情"));
        SpecialAfterSaleDetailApplyPcVO pcVo = new SpecialAfterSaleDetailApplyPcVO();
        ObjectUtil.extendObject(pcVo, apply, true);
        // 查询申请人信息
        UserGroupSimpleDTO userGroupInfo = userManager.searchUserGroupFromCache(apply.getCreatedBy());
        pcVo.setCreatorId(userGroupInfo.getUserId());
        pcVo.setCreatorName(userGroupInfo.getNickname());
        List<UserGroupSimpleDTO.GroupInfoVO> groups = userGroupInfo.getGroups();
        if (CollectionUtils.isNotEmpty(groups)) {
            pcVo.setCreatorGroupName(groups.get(0).getGroupDecs());
        }
        // 查询详情信息
        pcVo.setDetails(specialAfterSaleDetailRepository.findAllByApplyId(applyId));
        return pcVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(SpecialAfterSaleApprovalRequest request) throws Exception {
        Long applyId = request.getApplyId();
        Integer userId = UserHelper.getUserId();
        if (!specialAfterSaleApplyManager.checkIsApprover(applyId, userId)) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "当前操作人无法操作该申请");
        }
        SpecialAfterSaleApplyEntity apply = specialAfterSaleApplyRepository.findById(applyId)
                .orElseThrow(() -> new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "查询不到该申请详情"));
        // 调用推推棒submit
        TaskForm taskForm = new TaskForm();
        String taskId = request.getTaskId();
        taskForm.setId(taskId);
        int action;
        String actionDesc;
        // 审批人
        TaskFormItem item1 = new TaskFormItem();
        item1.setName(SpecialAfterSaleUseVariableEnum.Last_APPROVER.getCode());
        item1.setValue(userId);
        // 使用的额度id
        TaskFormItem item2 = new TaskFormItem();
        item2.setName(SpecialAfterSaleUseVariableEnum.USE_QUOTA_ID.getCode());
        item2.setValue(request.getQuotaId());
        // 审批结果
        TaskFormItem item = new TaskFormItem();
        item.setName(WorkflowStatusFlag.TASK_STATUS.getName());
        if (YesOrNoEnum.YES.getCode() == request.getStatus()) {
            item.setValue(TaskState.APPROVAL.getState());
            action = SpecialAfterSaleLogActionEnum.PASS.getCode();
            actionDesc = SpecialAfterSaleLogActionEnum.PASS.getDesc();
        } else {
            item.setValue(TaskState.REJECT.getState());
            action = SpecialAfterSaleLogActionEnum.REJECT.getCode();
            actionDesc = SpecialAfterSaleLogActionEnum.REJECT.getDesc();
        }
        // 审批结果文案
        TaskFormItem item3 = new TaskFormItem();
        item3.setName(SpecialAfterSaleUseVariableEnum.APPROVAL_STATUS_DESC.getCode());
        item3.setValue(actionDesc);
        taskForm.setFormData(Arrays.asList(item, item1, item2, item3));
        // 隐藏该task所有待审批日志
        List<SpecialAfterSaleLogEntity> logs = specialAfterSaleLogManager.hideOrShowLog(applyId, SpecialAfterSaleLogActionEnum.PENDING_APPROVAL.getValue(),
                SpecialAfterSaleLogActionEnum.PENDING_APPROVAL.getCode(), YesOrNoEnum.NO.getCode());
        // 增加该操作人审批日志
        specialAfterSaleLogManager.createActionLog(applyId, taskId, logs.get(0).getReviewNodeId(), action, request.getOpinions(), YesOrNoEnum.YES.getCode(), userId);
        // 提交任务 （之所以先打日志后提交任务，是为了保证日志的顺序。）
        workflowTaskService.saveTask(taskForm, true, false, true, userId.longValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(SpecialAfterSaleTransferRequest request) throws Exception {
        Long applyId = request.getApplyId();
        Integer operatorId = UserHelper.getUserId();
        if (!specialAfterSaleApplyManager.checkIsApprover(applyId, operatorId)) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "当前操作人无法操作该申请");
        }
        String taskId = request.getTaskId();
        List<Integer> userIds = request.getUserIds();
        // 转审
        workflowTaskService.transfer(taskId, userIds, operatorId);
        // 记录日志
        specialAfterSaleLogManager.createTransferLog(request.getApplyId(), taskId, userIds, operatorId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void routingAndStart(SpecialAfterSaleDxTransferRequest request) {
        Long applyId = request.getApplyId();
        Integer userId = UserHelper.getUserId();
        if (!specialAfterSaleApplyManager.checkIsApprover(applyId, userId)) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "当前操作人无法操作该申请");
        }
        // 电销主管其实并非转审，他的操作只是将CM选中的
        SpecialAfterSaleApplyEntity apply = specialAfterSaleApplyRepository.findById(applyId)
                .orElseThrow(() -> new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "查询不到该申请详情"));
        // 隐藏待转审
        specialAfterSaleLogManager.hideOrShowLog(applyId, SpecialAfterSaleLogActionEnum.DX_MANAGER_TRANSFER.getValue(),
                SpecialAfterSaleLogActionEnum.DX_MANAGER_TRANSFER.getCode(), YesOrNoEnum.NO.getCode(), userId);
        Integer nodeId = request.getNodeId();
        // 记录已转审
        specialAfterSaleLogManager.createActionLog(applyId, SpecialAfterSaleLogActionEnum.DX_MANAGER_TRANSFERRED.getValue(), nodeId,
                SpecialAfterSaleLogActionEnum.DX_MANAGER_TRANSFERRED.getCode(), null, YesOrNoEnum.YES.getCode(), userId);
        // 启动流程
        Map<String, Object> var = new HashMap<>();
        var.put(SpecialAfterSaleUseVariableEnum.APPLY_ID.getCode(), applyId);
        var.put(SpecialAfterSaleUseVariableEnum.APPLY_USER_ID.getCode(), apply.getCreatedBy());
        var.put(SpecialAfterSaleUseVariableEnum.APPLY_USER_TYPE.getCode(), SpecialAfterSaleApplyUserTypeEnum.DX.getCode());
        var.put(SpecialAfterSaleUseVariableEnum.APPLY_REASON.getCode(), apply.getApplyReason());
        Date createdAt = TimeUtil.localDateTime2Date(apply.getCreatedAt());
        var.put(SpecialAfterSaleUseVariableEnum.APPLY_TIME.getCode(), TimeUtil.format(createdAt, TimeUtil.FORMAT_DATE_TIME));
        var.put(SpecialAfterSaleUseVariableEnum.CONFIG_ID.getCode(), apply.getConfigId());
        var.put(SpecialAfterSaleUseVariableEnum.CURRENT_NODE_SPECIFY_USER.getCode() + nodeId, JsonUtil.toJsonString(request.getUserIds()));
        var.put(SpecialAfterSaleUseVariableEnum.APPLY_TOTAL_AMOUNT.getCode(), apply.getTotalApplyAmount().toString());
        DefinitionAndInstanceIdVO workflowInfo = workflowInstanceService.startExternalCall(buildStartReq(applyId, userId, var), userId);
        // 保存流程信息
        apply.setDefinitionId(workflowInfo.getDefinitionId());
        apply.setInstanceId(workflowInfo.getInstanceId());
        specialAfterSaleApplyManager.saveApply(apply);
    }

    private PassportUserInfoDTO getDxManager(List<List<PassportUserInfoDTO>> leaderList) {
        for (List<PassportUserInfoDTO> user : leaderList) {
            PassportUserInfoDTO leader = user.stream()
                    .filter(u -> PositionEnum.DXM.getPosition().equals(u.getPosition()))
                    .findFirst().orElse(null);
            if (leader != null) {
                return leader;
            }
        }
        return null;
    }

    private StartInstanceRequest buildStartReq(Long applyId, Integer userId, Map<String, Object> var) {
        StartInstanceRequest startReq = new StartInstanceRequest();
        startReq.setKey(sasDefinitionKey);
        startReq.setBusinessKey(applyId.toString());
        startReq.setCreatorId(userId.longValue());
        startReq.setVariables(var);
        return startReq;
    }

    @Override
    public Page<SpecialAfterSaleSimpleApplyVO> queryMyCcApplyList(SpecialAfterSaleSearchRequest req) {
        Integer userId = UserHelper.getUserId();
        QueryResults<SpecialAfterSaleApplyEntity> results = specialAfterSaleCcListRepository.queryMyCcApplyList(req, userId);
        return convertApplySimpleVoPage(PageRequest.of(req.getPage(), req.getSize()), results.getTotal(), results.getResults());
    }

    @Override
    public com.ruigu.rbox.cloud.kanai.web.page.PageImpl<SpecialAfterSaleApplyRecordVO> queryAfterSaleList(SpecialAfterSaleApplyRequest req) {
        QueryResults<SpecialAfterSaleApplyEntity> results = specialAfterSaleApplyRepository.queryAfterSaleListByPage(req);
        List<SpecialAfterSaleApplyRecordVO> applyList = queryUserIdByGroup(results.getResults());
        return com.ruigu.rbox.cloud.kanai.web.page.PageImpl.of(applyList, PageRequest.of(req.getPage(), req.getSize()), (int) results.getTotal());
    }

    /**
     * 组合特殊售后列表
     *
     * @param applyLists 特殊售后列表
     */
    private List<SpecialAfterSaleApplyRecordVO> queryUserIdByGroup(List<SpecialAfterSaleApplyEntity> applyLists) {
        List<SpecialAfterSaleApplyRecordVO> applyRecords = applyLists.stream().map(apply -> SpecialAfterSaleApplyRecordVO.builder()
                .nickName(apply.getApplyNickname())
                .userId(apply.getCreatedBy())
                .customerName(apply.getCustomerName())
                .applyDate(apply.getCreatedAt())
                .applyReason(apply.getApplyReason())
                .customerRating(apply.getCustomerRating())
                .status(applyStatusDict.get(apply.getStatus()))
                .id(apply.getId())
                .totalApplyAmount(apply.getTotalApplyAmount())
                .applyCode(apply.getCode())
                .build()).collect(Collectors.toList());
        Set<Integer> userSet = applyLists.stream().map(SpecialAfterSaleApplyEntity::getCreatedBy).collect(Collectors.toSet());
        Map<Integer, UserGroupSimpleDTO> userGroupFromCache = userManager.searchUserGroupFromCache(userSet);
        for (SpecialAfterSaleApplyRecordVO apply : applyRecords) {
            UserGroupSimpleDTO userGroup = userGroupFromCache.get(apply.getUserId());
            if (userGroup == null) {
                throw new GlobalRuntimeException(ResponseCode.ERROR.getCode(), "申请人编号" + apply.getUserId() + "无法找到");
            } else {
                apply.setDeptName(userGroup.getGroups().get(0).getGroupName());
                apply.setDeptNo(userGroup.getGroups().get(0).getGroupId());
            }
        }
        return applyRecords;
    }

    @Override
    public List<SpecialAfterSaleApplyRecordVO> exportAfterSaleApplyList(SpecialAfterSaleApplyExportRequest req) {
        List<SpecialAfterSaleApplyEntity> results = specialAfterSaleApplyRepository.queryAllAfterSaleList(req);
        return queryUserIdByGroup(results);
    }

    /**
     * 查询我提交的特殊售后申请
     *
     * @return
     */
    @Override
    public Page<SpecialAfterSaleSimpleApplyVO> findAllByCreatedBy(SpecialAfterSaleSearchRequest request) {
        PageRequest pageable = PageRequest.of(request.getPage(), request.getSize());
        Integer userId = UserHelper.getUserId();
        Page<SpecialAfterSaleApplyEntity> queryResult = repository.findAllByCreatedBy(userId, pageable);
        return convertApplySimpleVoPage(pageable, queryResult.getTotalElements(), queryResult.getContent());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void urgeSpecialSaleApply(Long applyId) {
        // 查询工单信息
        SpecialAfterSaleApplyEntity apply = specialAfterSaleApplyRepository.findById(applyId)
                .orElseThrow(() -> new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "申请id" + applyId + "对应的特殊售后审批不存在"));
        Integer status = apply.getStatus();
        if (status == null) {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(),
                    "异常，催办特殊售后审批状态缺失，无法催办");
        }
        // 查询当前审批人
        List<SpecialAfterSaleApplyApproverEntity> currentApprovers = specialAfterSaleApplyApproverRepository.findAllByApplyId(applyId);
        if (currentApprovers.isEmpty()) {
            throw new GlobalRuntimeException(ResponseCode.REQUEST_ERROR.getCode(), "该特殊售后审批申请没有当前审批人");
        }
        ArrayList<Integer> approverIds = new ArrayList<>(8);
        for (SpecialAfterSaleApplyApproverEntity approver : currentApprovers) {
            approverIds.add(approver.getApplyId().intValue());
        }
        // 催办人信息存redis防止重复催办
        String urgeRedisKey = RedisKeyConstants.SPECIAL_SALE_URGE_RESTRICT + "approverIds:" + StringUtils.join(approverIds, Symbol.COMMA.getValue()) + ":applyId:" + applyId;
        Boolean result = redisTemplate.hasKey(urgeRedisKey);
        if (result != null && result) {
            throw new GlobalRuntimeException(ResponseCode.REFUSE_EXECUTE.getCode(), "半小时内只能催办一次哟！");
        }
        redisTemplate.opsForValue().set(urgeRedisKey, new Date(), 30, TimeUnit.MINUTES);
        // 发送消息催办
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                String definitionName = "特殊售后审批申请";
                String title = "催办来自" + apply.getApplyNickname() + "的" + definitionName;
                Map<String, Object> body = new HashMap<>(8);
                body.put(NoticeParam.TITLE.getDesc(), title);
                body.put(NoticeParam.CONTENT.getDesc(), title);
                questNoticeService.sendTextCardMultipleApp(EnvelopeChannelEnum.SPECIAL_AFTER_SALE, body, approverIds);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelSpecialSaleApply(Long applyId) {
        Integer userId = UserHelper.getUserId();
        // 更新审批表状态为取消
        SpecialAfterSaleApplyEntity entity = specialAfterSaleApplyRepository.findById(applyId)
                .orElseThrow(() -> new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "查询不到该申请详情"));
        specialAfterSaleApplyRepository.updateAfterSaleApplyStatus(SpecialAfterSaleApplyStatusEnum.UNDO.getCode(),applyId);
        // 插入日志信息
        specialAfterSaleLogManager.createActionLog(applyId, SpecialAfterSaleLogActionEnum.CANCEL.getValue(), null,
                SpecialAfterSaleLogActionEnum.CANCEL.getCode(), null, YesOrNoEnum.YES.getCode(), userId);
        // 更新workflow工作表信息
        workflowInstanceService.revokeInstanceById(entity.getInstanceId());
    }

    private Page<SpecialAfterSaleSimpleApplyVO> convertApplySimpleVoPage(Pageable pageable, long total, List<SpecialAfterSaleApplyEntity> entities) {
        List<Integer> applyUserIds = entities.stream().map(SpecialAfterSaleApplyEntity::getCreatedBy).collect(Collectors.toList());
        Map<Integer, PassportUserInfoDTO> applyUserInfoMap = passportFeignManager.getUserInfoMapFromRedis(applyUserIds);
        List<SpecialAfterSaleSimpleApplyVO> vos = new ArrayList<>();
        entities.forEach(e -> {
            SpecialAfterSaleSimpleApplyVO vo = new SpecialAfterSaleSimpleApplyVO();
            vo.setApplyId(e.getId());
            vo.setCode(e.getCode());
            vo.setApplyTime(e.getCreatedAt());
            Integer applyUserId = e.getCreatedBy();
            vo.setApplyUserId(applyUserId);
            PassportUserInfoDTO info = applyUserInfoMap.getOrDefault(applyUserId, null);
            if (Objects.nonNull(info)) {
                vo.setApplyUserName(info.getNickname());
            } else {
                vo.setApplyUserName(e.getApplyNickname());
            }
            vo.setApplyAmount(e.getTotalApplyAmount());
            vo.setStatus(e.getStatus());
            vo.setApprovalTime(e.getApprovalTime());
            vos.add(vo);
        });
        return new PageImpl(vos, pageable, total);
    }
}

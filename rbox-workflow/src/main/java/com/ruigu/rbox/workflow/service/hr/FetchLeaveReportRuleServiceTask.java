package com.ruigu.rbox.workflow.service.hr;

import com.ruigu.rbox.cloud.kanai.model.ResponseCode;
import com.ruigu.rbox.cloud.kanai.security.UserHelper;
import com.ruigu.rbox.cloud.kanai.web.exception.GlobalRuntimeException;
import com.ruigu.rbox.workflow.feign.handler.HrFeignHandler;
import com.ruigu.rbox.workflow.model.request.GetOneReviewConfigReq;
import com.ruigu.rbox.workflow.model.vo.CcVO;
import com.ruigu.rbox.workflow.model.vo.ReviewConfigDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取审批人和抄送人
 *
 * @author alan.zhao
 */
@Slf4j
@Service("fetchLeaveReportRuleServiceTask")
public class FetchLeaveReportRuleServiceTask implements JavaDelegate {

    @Autowired
    private HrFeignHandler hrFeignHandler;

    @Override
    public void execute(DelegateExecution delegateExecution) {

        Integer typeId = (Integer) delegateExecution.getVariable("type");
        Double duration = Double.parseDouble(delegateExecution.getVariable("duration").toString());
        Integer reviewConfigId = (Integer) delegateExecution.getVariable("reviewConfigId");
        Integer applyUserId = (Integer) delegateExecution.getVariable("applyUserId");
        if (applyUserId == null) {
            applyUserId = UserHelper.getUserId();
        }
        GetOneReviewConfigReq req = new GetOneReviewConfigReq();
        req.setApplyUserId(applyUserId);
        req.setLeaveReportTypeId(typeId);
        req.setDuration(duration);

        ReviewConfigDetailVO reviewConfigDetail = hrFeignHandler.getReviewConfigDetail(reviewConfigId, applyUserId);
        try {
            //1 获取审批人
            List<ReviewConfigDetailVO.ReviewUserConfigVO> reviewUserConfigList = reviewConfigDetail.getReviewUserConfigList();
            Map<Integer, List<Integer>> reviewUserConfigMap = reviewUserConfigList.stream().collect(Collectors.toMap(ReviewConfigDetailVO.ReviewUserConfigVO::getReviewOrder, ReviewConfigDetailVO.ReviewUserConfigVO::getUserIdList));
            //2 获取抄送人
            List<CcVO> ccList = reviewConfigDetail.getCcList();
            List<Integer> ccUsers = ccList != null ? ccList.stream().map(CcVO::getUserId).collect(Collectors.toList()) : new ArrayList<>();
            //3 当前审批节点
            Integer currentOrder = 1;
            Integer max = reviewUserConfigMap.size();
            Map<Integer, List<Integer>> approvers = new HashMap<>(16);
            int c = 0;
            for (int i = currentOrder; i <= max; i++) {
                List<Integer> current = reviewUserConfigMap.get(i);
                if (CollectionUtils.isEmpty(current)) {
                    continue;
                }
                c++;
                approvers.put(Integer.valueOf(c), current);
            }
            if (c == 0) {
                throw new GlobalRuntimeException(ResponseCode.ERROR.getCode(), "没有设置审批人");
            }
            //4 设置流程参数
            delegateExecution.setVariable("approvers", approvers);
            delegateExecution.setVariable("currentOrder", currentOrder);
            delegateExecution.setVariable("approver", approvers.get(currentOrder));
            delegateExecution.setVariable("ccUsers", ccUsers);
            delegateExecution.setVariable("hasNext", 1);
            delegateExecution.setVariable("applyUserId", applyUserId);
        } finally {
            log.info("获取请假报备规则");
        }
    }
}

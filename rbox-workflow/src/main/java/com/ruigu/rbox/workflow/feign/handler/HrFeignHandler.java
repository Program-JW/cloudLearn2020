package com.ruigu.rbox.workflow.feign.handler;

import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.feign.HrFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.request.*;
import com.ruigu.rbox.workflow.model.vo.ReviewConfigDetailVO;
import com.ruigu.rbox.workflow.model.vo.ReviewConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author chenzhenya
 * @date 2020/5/26 13:45
 */
@Slf4j
@Component
public class HrFeignHandler {

    @Autowired
    private HrFeignClient hrFeignClient;

    public ReviewConfigVO getReviewConfigInfo(GetOneReviewConfigReq req) {
        ResponseEntity<ServerResponse<ReviewConfigVO>> responseEntity = hrFeignClient.getReviewConfigInfo(req);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            ServerResponse<ReviewConfigVO> response = responseEntity.getBody();
            if (response.isSuccess()) {
                return response.getData();
            }
            throw new GlobalRuntimeException(response.getCode(), response.getMessage());
        }
        throw new GlobalRuntimeException(ResponseCode.ERROR.getCode(), "rbox-hr 获取审核配置调用失败");
    }

    public Integer recordLeaveReportApply(HrLeaveReportApplyReq req) {
        ResponseEntity<ServerResponse<Integer>> responseEntity = hrFeignClient.applyReport(req);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            ServerResponse<Integer> response = responseEntity.getBody();
            if (response.isSuccess()) {
                return response.getData();
            }
            throw new GlobalRuntimeException(response.getCode(), response.getMessage());
        }
        throw new GlobalRuntimeException(ResponseCode.ERROR.getCode(), "rbox-hr 保存请假报备申请失败");
    }

    public void updateApplyTask(HrLeaveReportApplyReq req) {
        ResponseEntity<ServerResponse> responseEntity = hrFeignClient.updateApplyReport(req);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            ServerResponse response = responseEntity.getBody();
            if (response.isSuccess()) {
                return;
            }
            throw new GlobalRuntimeException(response.getCode(), response.getMessage());
        }
        throw new GlobalRuntimeException(ResponseCode.ERROR.getCode(), "rbox-hr 更新请假报备申请失败");
    }

    public ReviewConfigDetailVO getReviewConfigDetail(Integer reviewConfigId, Integer applyUserId) {
        ResponseEntity<ServerResponse<ReviewConfigDetailVO>> responseEntity = hrFeignClient.getReviewConfigDetail(reviewConfigId, applyUserId);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            ServerResponse<ReviewConfigDetailVO> response = responseEntity.getBody();
            if (response.isSuccess()) {
                return response.getData();
            }
            throw new GlobalRuntimeException(response.getCode(), response.getMessage());
        }
        throw new GlobalRuntimeException(ResponseCode.ERROR.getCode(), "rbox-hr 获取审核配置详情调用失败");
    }

    public void applyTask(LeaveReportTaskReq leaveReportTaskReq) {
        ResponseEntity<ServerResponse> responseEntity = hrFeignClient.applyTask(leaveReportTaskReq);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            ServerResponse response = responseEntity.getBody();
            if (response.isSuccess()) {
                return;
            }
            throw new GlobalRuntimeException(response.getCode(), response.getMessage());
        }
        throw new GlobalRuntimeException(ResponseCode.ERROR.getCode(), "rbox-hr 审批接口调用失败");
    }

    public void updateApplyTask(LeaveReportTaskUpdateReq req) {
        ResponseEntity<ServerResponse> responseEntity = hrFeignClient.updateApplyTask(req);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            ServerResponse response = responseEntity.getBody();
            if (response.isSuccess()) {
                return;
            }
            throw new GlobalRuntimeException(response.getCode(), response.getMessage());
        }
        throw new GlobalRuntimeException(ResponseCode.ERROR.getCode(), "rbox-hr 修改审批任务接口调用失败");
    }

    public void sendLeaveApplyCcNotice(LeaveReportCcReq req) {

        hrFeignClient.sendCcNotice(req);
    }

}

package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.request.*;
import com.ruigu.rbox.workflow.model.vo.ApprovalNodeVO;
import com.ruigu.rbox.workflow.model.vo.ReviewConfigDetailVO;
import com.ruigu.rbox.workflow.model.vo.ReviewConfigVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenzhenya
 * @date 2020/5/25 17:01
 */
@FeignClient(name = "rbox-hr")
public interface HrFeignClient {

    @GetMapping("/reviewConfig-one")
    ResponseEntity<ServerResponse<ReviewConfigVO>> getReviewConfigInfo(@SpringQueryMap GetOneReviewConfigReq req);

    @PostMapping("/leaveApply/save")
    ResponseEntity<ServerResponse<Integer>> applyReport(@RequestBody HrLeaveReportApplyReq hrLeaveReportApplyReq);

    @GetMapping("/{reviewConfigId}/reviewConfig-detail/{applyUserId}")
    ResponseEntity<ServerResponse<ReviewConfigDetailVO>> getReviewConfigDetail(@PathVariable("reviewConfigId") Integer reviewConfigId, @PathVariable("applyUserId") Integer applyUserId);

    @PostMapping("/leaveTask/save")
    ResponseEntity<ServerResponse> applyTask(@RequestBody LeaveReportTaskReq leaveReportTaskReq);

    @PutMapping("/leaveApply/update")
    ResponseEntity<ServerResponse> updateApplyReport(@RequestBody HrLeaveReportApplyReq leaveReportTaskReq);

    @PutMapping("/leaveTask/update")
    ResponseEntity<ServerResponse> updateApplyTask(@RequestBody LeaveReportTaskUpdateReq leaveReportTaskReq);

    @PostMapping("/leaveApply/send/ccNotice")
    ServerResponse sendCcNotice(@RequestBody LeaveReportCcReq req);

    @GetMapping("{applyId}/next-node")
    ServerResponse<ApprovalNodeVO> queryNextNode(@PathVariable("applyId") Integer applyId, @RequestParam("nodeId") Integer nodeId);
}

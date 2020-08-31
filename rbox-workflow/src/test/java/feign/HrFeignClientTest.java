package feign;
import java.util.Collections;

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.WorkflowApplication;
import com.ruigu.rbox.workflow.feign.HrFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.request.GetOneReviewConfigReq;
import com.ruigu.rbox.workflow.model.request.HrLeaveReportApplyReq;
import com.ruigu.rbox.workflow.model.request.LeaveReportTaskReq;
import com.ruigu.rbox.workflow.model.vo.ReviewConfigDetailVO;
import com.ruigu.rbox.workflow.model.vo.ReviewConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author chenzhenya
 * @date 2020/5/26 13:22
 */
@Slf4j
@ActiveProfiles("local")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkflowApplication.class)
public class HrFeignClientTest {
    @Autowired
    private HrFeignClient hrFeignClient;

    @Test
    public void testGetReviewConfigInfo() {
        GetOneReviewConfigReq req = new GetOneReviewConfigReq();
        req.setApplyUserId(1);
        req.setLeaveReportTypeId(1);
        req.setDuration(17.0D);

        ResponseEntity<ServerResponse<ReviewConfigVO>> responseEntity = hrFeignClient.getReviewConfigInfo(req);
        Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        ServerResponse<ReviewConfigVO> response = responseEntity.getBody();
        log.info(JsonUtil.toJsonString(response));
        Assert.assertTrue(response.isSuccess());
        ReviewConfigVO data = response.getData();
        log.info(JsonUtil.toJsonString(data));
    }

    @Test
    public void testApplyReport() {
        HrLeaveReportApplyReq hrLeaveReportApplyReq = new HrLeaveReportApplyReq();
        hrLeaveReportApplyReq.setApplyUserId(1383);
        hrLeaveReportApplyReq.setLeaveReportTypeId(1);
        hrLeaveReportApplyReq.setStartTime("2020-05-24 09:00:00");
        hrLeaveReportApplyReq.setEndTime("2020-05-24 18:00:00");
        hrLeaveReportApplyReq.setDuration(8D);
        hrLeaveReportApplyReq.setDefinitionId("11111111");
        hrLeaveReportApplyReq.setInstanceId("22222222");
        hrLeaveReportApplyReq.setStatus(0);
        hrLeaveReportApplyReq.setCreatedBy(1383);
        hrLeaveReportApplyReq.setLastUpdateBy(1383);
        hrLeaveReportApplyReq.setApplyReason("test");
        ResponseEntity<ServerResponse<Integer>> responseEntity = hrFeignClient.applyReport(hrLeaveReportApplyReq);
        Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        ServerResponse<Integer> response = responseEntity.getBody();
        log.info(JsonUtil.toJsonString(response));
        Assert.assertTrue(response.isSuccess());
        Integer data = response.getData();
        log.info(JsonUtil.toJsonString(data));
    }

    @Test
    public void testGetReviewConfigDetail() {
        ResponseEntity<ServerResponse<ReviewConfigDetailVO>> responseEntity = hrFeignClient.getReviewConfigDetail(2, 1383);
        Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        ServerResponse<ReviewConfigDetailVO> response = responseEntity.getBody();
        log.info(JsonUtil.toJsonString(response));
        Assert.assertTrue(response.isSuccess());
        ReviewConfigDetailVO data = response.getData();
        log.info(JsonUtil.toJsonString(data));
    }

    @Test
    public void testApplyTask() {
        LeaveReportTaskReq req = new LeaveReportTaskReq();
        req.setApplyId(1);
        req.setTaskId("test");
        req.setUserId(Collections.singletonList(1383));
        req.setStatus(0);
        req.setCreatedBy(0);

        ResponseEntity<ServerResponse> responseEntity = hrFeignClient.applyTask(req);
        Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        ServerResponse response = responseEntity.getBody();
        log.info(JsonUtil.toJsonString(response));
        Assert.assertTrue(response.isSuccess());
//        ReviewConfigDetailVO data = response.getData();
//        log.info(JsonUtil.toJsonString(data));
    }
}

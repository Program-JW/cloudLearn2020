import com.ruigu.rbox.workflow.WorkflowApplication;
import com.ruigu.rbox.workflow.feign.ScmFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.StockChangeSkuNotEmptyDTO;
import com.ruigu.rbox.workflow.model.entity.StockChangeApplyEntity;
import com.ruigu.rbox.workflow.model.entity.StockLockApplyEntity;
import com.ruigu.rbox.workflow.model.request.StockChangeLastApplyRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author chenzhenya
 * @date 2019/11/19 14:59
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WorkflowApplication.class)
@Slf4j
public class ScmFeignClientTest {
    @Autowired
    private ScmFeignClient scmFeignClient;

    @Test
    public void run1() {
        ServerResponse response = scmFeignClient.getStockLockApplyListByIds(Arrays.asList(248, 249));
        if (response.getCode() == 200) {
            List<StockLockApplyEntity> stockLockApplyEntityList = (List<StockLockApplyEntity>) response.getData();
            System.out.println("===================================");
            System.out.println(stockLockApplyEntityList);
            System.out.println("===================================");
        }
    }

    @Test
    public void t1() {
        ServerResponse<List<StockChangeSkuNotEmptyDTO>> thisMonthSkuNotEmpty = scmFeignClient.getThisMonthSkuNotEmpty();
        System.out.println(thisMonthSkuNotEmpty);
    }

    @Test
    public void t2() {
        StockChangeLastApplyRequest lastApplyRequest = new StockChangeLastApplyRequest();
        lastApplyRequest.setSkuCode(100150426);
        lastApplyRequest.setStorageId(455);
        ServerResponse<List<StockChangeApplyEntity>> listServerResponse = scmFeignClient.batchGetLastRecords(Collections.singletonList(lastApplyRequest));
        System.out.println(listServerResponse);
    }
}

import com.ruigu.rbox.workflow.WorkflowApplication;
import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.manager.impl.PassportFeignManagerImpl;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import io.lettuce.core.output.ScanOutput;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/01/09 10:40
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WorkflowApplication.class)
@Slf4j
public class LightningTimeoutTest {

    // 查询领导

    @Resource
    private PassportFeignManagerImpl passportFeignManager;

    @Resource
    private PassportFeignClient client;

    @Test
    public void t1() {
        List<PassportUserInfoDTO> superiorLeader = passportFeignManager.getSuperiorLeader(958);
        List<PassportUserInfoDTO> superiorLeader5 = passportFeignManager.getSuperiorLeader(914);
        List<PassportUserInfoDTO> superiorLeader1 = passportFeignManager.getSuperiorLeader(938);
        List<PassportUserInfoDTO> superiorLeader2 = passportFeignManager.getSuperiorLeader(937);
        List<PassportUserInfoDTO> superiorLeader3 = passportFeignManager.getSuperiorLeader(751);
        List<PassportUserInfoDTO> superiorLeader4 = passportFeignManager.getSuperiorLeader(868);
        System.out.println(superiorLeader);
    }

    @Test
    public void t2() {
        ServerResponse<List<PassportUserInfoDTO>> userMsgByIds = client.getUserMsgByIds(Collections.singleton(1227));
        System.out.println(userMsgByIds);
    }
}

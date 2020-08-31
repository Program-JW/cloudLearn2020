import com.ruigu.rbox.workflow.WorkflowApplication;
import com.ruigu.rbox.workflow.service.timer.ReplaceDutyUserTimer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author liqingtian
 * @date 2020/07/13 14:30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WorkflowApplication.class)
@Slf4j
@ActiveProfiles("local")
public class ReplaceDutyUserTest {

    @Resource
    private ReplaceDutyUserTimer replaceDutyUserTimer;

    @Test
    public void test1() {


        // 1. check 技术部值班人 自动排班
        try {
            replaceDutyUserTimer.checkAndResetTechnicalDutyUser();
        } catch (Exception e) {
            log.error("技术部自动排班失败，e:{}", e);
        }
        // 2. 更换当天闪电链值班人
        replaceDutyUserTimer.replace();


    }
}

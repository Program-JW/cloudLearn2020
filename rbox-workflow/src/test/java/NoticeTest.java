import com.ruigu.rbox.workflow.WorkflowApplication;
import com.ruigu.rbox.workflow.service.QuestNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author liqingtian
 * @date 2020/08/01 10:22
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WorkflowApplication.class)
@Slf4j
@ActiveProfiles("dev")
public class NoticeTest {

    @Resource
    private QuestNoticeService questNoticeService;

    @Test
    public void t1() throws Exception {

        questNoticeService.sendEmailNotice(null, "HELLO WORD", "SSSS", Arrays.asList(1227), null);
    }
}

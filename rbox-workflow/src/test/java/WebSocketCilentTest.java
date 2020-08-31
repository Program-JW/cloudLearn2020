import com.ruigu.rbox.workflow.WorkflowApplication;
import com.ruigu.rbox.workflow.model.dto.BuildGroupDTO;
import com.ruigu.rbox.workflow.service.ChatWebSocketService;
import jdk.nashorn.internal.ir.annotations.Reference;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/01/08 21:35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WorkflowApplication.class)
@Slf4j
public class WebSocketCilentTest {

    @Resource
    private ChatWebSocketService chatWebSocketService;


    @Test
    public void testLogin() {
        chatWebSocketService.login("7311");
    }

    @Test
    public void testBuildGroup() throws InterruptedException {
        BuildGroupDTO buildGroupDTO = new BuildGroupDTO();
        buildGroupDTO.setIssueId(21);
        buildGroupDTO.setMasterId(7311);
        chatWebSocketService.buildGroup((Collections.singletonList(buildGroupDTO)));
        Thread.sleep(10000);
    }

    @Test
    public void testAddUserToGroup() throws InterruptedException {
        chatWebSocketService.addUserToGroup(21, 532540758354460672L, 7311, 7477);
        Thread.sleep(20000);
    }
}

import com.ruigu.rbox.cloud.kanai.util.JsonUtil;
import com.ruigu.rbox.workflow.WorkflowApplication;
import com.ruigu.rbox.workflow.model.dto.BaseNotifyDTO;
import com.ruigu.rbox.workflow.model.dto.MsgNotifyDTO;
import com.ruigu.rbox.workflow.model.entity.RabbitmqMsgLogEntity;
import com.ruigu.rbox.workflow.service.RabbitmqMsgService;
import com.ruigu.rbox.workflow.strategy.context.ChatMqMsgHandleContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author liqingtian
 * @date 2020/02/04 13:55
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WorkflowApplication.class)
@Slf4j
public class ChatMqMsgTest {

    @Resource
    private RabbitmqMsgService rabbitmqMsgService;

    @Resource
    private ChatMqMsgHandleContext chatMqMsgHandleContext;

    @Test
    public void t1() {
        String messageString = "{\"msgId\":123,\"content\":\"{\\\"msgId\\\":123,\\\"groupId\\\":535010445140992000,\\\"fromConnName\\\":\\\"0-111\\\",\\\"content\\\":\\\"测试测试\\\",\\\"offUserList\\\":[\\\"liqingtian\\\"],\\\"sendTime\\\":\\\"2020-02-03T18:07:15.74\\\"}\",\"action\":1}";

        BaseNotifyDTO baseNotifyDTO = null;
        MsgNotifyDTO msgNotifyDTO = null;
        try {
            baseNotifyDTO = JsonUtil.parseObject(messageString, BaseNotifyDTO.class);
            String content = baseNotifyDTO.getContent();
            if (StringUtils.isBlank(content)) {
                log.error("| - [ 聊天室 rabbit mq 监听 ] 消息体content为空");
                return;
            }
            msgNotifyDTO = JsonUtil.parseObject(content, MsgNotifyDTO.class);
        } catch (Exception e) {
            log.error("| - [ 聊天室 rabbit mq 监听 ] 消息类型转换失败");
            return;
        }
        // 保存
        RabbitmqMsgLogEntity rabbitmqMsg = new RabbitmqMsgLogEntity();
        rabbitmqMsg.setMessage(messageString);
        rabbitmqMsg.setTaskIdWeixin(msgNotifyDTO.getMsgId().toString());
        rabbitmqMsgService.saveMsg(rabbitmqMsg);
        // 处理
        chatMqMsgHandleContext.handle(baseNotifyDTO.getAction(), msgNotifyDTO);

    }
}

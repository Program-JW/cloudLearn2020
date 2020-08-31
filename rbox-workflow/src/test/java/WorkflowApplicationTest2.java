import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.WorkflowApplication;
import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.entity.WorkDayEntity;
import com.ruigu.rbox.workflow.supports.FormDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author alan.zhao
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WorkflowApplication.class)
@Slf4j
public class WorkflowApplicationTest2 {


    @Autowired
    FormDataUtil formDataUtil;

    @Autowired
    private PassportFeignClient passportFeignClient;

    @Test
    public void contextLoads() {

        List<WorkDayEntity> days = new ArrayList<>();
        for (int i = 1; i <= 365; i++) {
            WorkDayEntity day = new WorkDayEntity();
            LocalDate localDate = LocalDate.ofYearDay(2019, i);
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
            day.setDayOfYear(i);
            day.setDescription("2019年测试工作日历");
            day.setReason("1");
            if (localDate.getDayOfWeek().getValue() > 5) {
                day.setIsWorkingDay(0);
            } else {
                day.setIsWorkingDay(1);
            }
            day.setYear(2019);
            days.add(day);
        }
    }

    @Test
    public void testFormContent() {

        String json = "{\"list\":[{\"type\":\"group-container\",\"name\":\"分组\",\"icon\":\"el-icon-document\",\"options\":{\"title\":\"分组\",\"remoteFunc\":\"func_1567655794000_5384\"},\"optionDefine\":[{\"label\":\"容器宽度(单位:格)\",\"name\":\"span\",\"type\":\"number\",\"required\":false,\"width\":\"100%\"},{\"label\":\"左边的偏移(单位:格)\",\"name\":\"offset\",\"type\":\"number\",\"required\":false,\"width\":\"100%\"}],\"key\":\"1567655794000_5384\",\"list\":[{\"type\":\"input\",\"name\":\"单行文本\",\"icon\":\"el-icon-document\",\"options\":{\"title\":\"单行文本\",\"code\":\"name\",\"placeholder\":\"\",\"width\":\"100%\",\"labelWidth\":0,\"dataType\":\"string\",\"required\":0},\"optionDefine\":[{\"label\":\"标题\",\"name\":\"title\",\"type\":\"input\",\"rules\":{\"type\":\"string\",\"required\":true,\"message\":\"不能为空\"}},{\"label\":\"变量名\",\"name\":\"code\",\"type\":\"input\",\"rules\":{\"type\":\"string\",\"required\":true,\"pattern\":\"^[a-zA-Z]+[a-zA-Z0-9_]+$\",\"message\":\"不能为空,且必须以字母开头,由字母数字下划线组成\"}},{\"label\":\"输入提示\",\"name\":\"placeholder\",\"type\":\"input\",\"required\":false},{\"label\":\"宽度\",\"name\":\"width\",\"type\":\"input\",\"required\":false},{\"label\":\"标题宽度\",\"name\":\"labelWidth\",\"type\":\"number\",\"required\":false},{\"label\":\"数据类型\",\"name\":\"dataType\",\"type\":\"select\",\"dataType\":\"string\",\"inline\":true,\"data\":[{\"label\":\"文本\",\"value\":\"string\"},{\"label\":\"数字\",\"value\":\"number\"},{\"label\":\"邮箱\",\"value\":\"email\"}]},{\"label\":\"是否必填\",\"name\":\"required\",\"type\":\"radio\",\"dataType\":\"boolean\",\"inline\":true,\"data\":[{\"label\":\"是\",\"value\":1},{\"label\":\"否\",\"value\":0}]}],\"key\":\"1567655797000_86761\",\"list\":[],\"model\":\"input_1567655797000_86761\",\"rules\":[]},{\"type\":\"textarea\",\"name\":\"多行文本\",\"icon\":\"el-icon-document\",\"options\":{\"title\":\"多行文本\",\"code\":\"age\",\"placeholder\":\"\",\"width\":\"100%\",\"labelWidth\":0,\"dataType\":\"string\",\"required\":0},\"optionDefine\":[{\"label\":\"标题\",\"name\":\"title\",\"type\":\"input\",\"rules\":{\"type\":\"string\",\"required\":true,\"message\":\"不能为空\"}},{\"label\":\"变量名\",\"name\":\"code\",\"type\":\"input\",\"rules\":{\"type\":\"string\",\"required\":true,\"pattern\":\"^[a-zA-Z]+[a-zA-Z0-9_]+$\",\"message\":\"不能为空,且必须以字母开头,由字母数字下划线组成\"}},{\"label\":\"输入提示\",\"name\":\"placeholder\",\"type\":\"input\",\"required\":false},{\"label\":\"宽度\",\"name\":\"width\",\"type\":\"input\",\"required\":false},{\"label\":\"标题宽度\",\"name\":\"labelWidth\",\"type\":\"number\",\"required\":false},{\"label\":\"数据类型\",\"name\":\"dataType\",\"type\":\"select\",\"dataType\":\"string\",\"inline\":true,\"data\":[{\"label\":\"文本\",\"value\":\"string\"},{\"label\":\"数字\",\"value\":\"number\"},{\"label\":\"邮箱\",\"value\":\"email\"}]},{\"label\":\"是否必填\",\"name\":\"required\",\"type\":\"radio\",\"dataType\":\"boolean\",\"inline\":true,\"data\":[{\"label\":\"是\",\"value\":1},{\"label\":\"否\",\"value\":0}]}],\"key\":\"1567655798000_8805\",\"list\":[],\"model\":\"textarea_1567655798000_8805\",\"rules\":[]},{\"type\":\"container\",\"name\":\"无边框\",\"icon\":\"el-icon-document\",\"propertyDialog\":{\"labelWidth\":120},\"options\":{\"title\":\"无边框\",\"remoteFunc\":\"func_1567655819000_16500\"},\"optionDefine\":[{\"label\":\"容器宽度(单位:格)\",\"name\":\"span\",\"type\":\"number\",\"required\":false,\"width\":\"100%\"},{\"label\":\"左边的偏移(单位:格)\",\"name\":\"offset\",\"type\":\"number\",\"required\":false,\"width\":\"100%\"}],\"key\":\"1567655819000_16500\",\"list\":[{\"type\":\"input\",\"name\":\"单行文本\",\"icon\":\"el-icon-document\",\"options\":{\"title\":\"单行文本\",\"code\":\"aa\",\"placeholder\":\"\",\"width\":\"100%\",\"labelWidth\":0,\"dataType\":\"string\",\"required\":0},\"optionDefine\":[{\"label\":\"标题\",\"name\":\"title\",\"type\":\"input\",\"rules\":{\"type\":\"string\",\"required\":true,\"message\":\"不能为空\"}},{\"label\":\"变量名\",\"name\":\"code\",\"type\":\"input\",\"rules\":{\"type\":\"string\",\"required\":true,\"pattern\":\"^[a-zA-Z]+[a-zA-Z0-9_]+$\",\"message\":\"不能为空,且必须以字母开头,由字母数字下划线组成\"}},{\"label\":\"输入提示\",\"name\":\"placeholder\",\"type\":\"input\",\"required\":false},{\"label\":\"宽度\",\"name\":\"width\",\"type\":\"input\",\"required\":false},{\"label\":\"标题宽度\",\"name\":\"labelWidth\",\"type\":\"number\",\"required\":false},{\"label\":\"数据类型\",\"name\":\"dataType\",\"type\":\"select\",\"dataType\":\"string\",\"inline\":true,\"data\":[{\"label\":\"文本\",\"value\":\"string\"},{\"label\":\"数字\",\"value\":\"number\"},{\"label\":\"邮箱\",\"value\":\"email\"}]},{\"label\":\"是否必填\",\"name\":\"required\",\"type\":\"radio\",\"dataType\":\"boolean\",\"inline\":true,\"data\":[{\"label\":\"是\",\"value\":1},{\"label\":\"否\",\"value\":0}]}],\"key\":\"1567655821000_330\",\"list\":[],\"model\":\"input_1567655821000_330\",\"rules\":[]},{\"type\":\"select\",\"name\":\"下拉单选\",\"icon\":\"el-icon-document\",\"options\":{\"title\":\"下拉单选\",\"code\":\"www\",\"datasource\":null,\"labelWidth\":0,\"placeholder\":\"\",\"required\":0,\"clearable\":0,\"multiple\":0},\"optionDefine\":[{\"label\":\"标题\",\"name\":\"title\",\"type\":\"input\",\"rules\":{\"type\":\"string\",\"required\":true,\"message\":\"不能为空\"}},{\"label\":\"变量名\",\"name\":\"code\",\"type\":\"input\",\"rules\":{\"type\":\"string\",\"required\":true,\"pattern\":\"^[a-zA-Z]+[a-zA-Z0-9_]+$\",\"message\":\"不能为空,且必须以字母开头,由字母数字下划线组成\"}},{\"label\":\"数据源\",\"name\":\"datasource\",\"type\":\"select-datasource\",\"sourceType\":null,\"dataType\":\"number\",\"rules\":{\"required\":true,\"message\":\"数据源不能为空\"}},{\"label\":\"标题宽度\",\"name\":\"labelWidth\",\"type\":\"number\",\"width\":\"100%\"},{\"label\":\"选择提示\",\"name\":\"placeholder\",\"type\":\"input\"},{\"label\":\"是否必填\",\"name\":\"required\",\"type\":\"radio\",\"dataType\":\"boolean\",\"inline\":true,\"data\":[{\"label\":\"是\",\"value\":1},{\"label\":\"否\",\"value\":0}]},{\"label\":\"是否可清空\",\"name\":\"clearable\",\"type\":\"radio\",\"dataType\":\"boolean\",\"inline\":true,\"data\":[{\"label\":\"是\",\"value\":1},{\"label\":\"否\",\"value\":0}]},{\"label\":\"是否多选\",\"name\":\"multiple\",\"type\":\"radio\",\"dataType\":\"boolean\",\"inline\":true,\"data\":[{\"label\":\"是\",\"value\":1},{\"label\":\"否\",\"value\":0}]}],\"key\":\"1567655823000_56058\",\"list\":[],\"model\":\"select_1567655823000_56058\",\"rules\":[]}],\"model\":\"container_1567655819000_16500\",\"rules\":[]}],\"model\":\"group-container_1567655794000_5384\",\"rules\":[]}],\"config\":{\"labelWidth\":\"150px\",\"labelPosition\":\"left\"}}";
        JSONObject content = JSON.parseObject(json);
        JSONArray list = JSONArray.parseArray(content.getString("list"));
        Set<String> codes = new HashSet<>();
        forList(list, codes);
        System.out.println(list);
        System.out.println(codes);
    }

    private void forList(JSONArray list, Set<String> codes) {
        for (int i = 0; i < list.size(); i++) {
            JSONObject data = list.getJSONObject(i);
            if (data.getString("type").contains("container")) {
                if (data.containsKey("list")) {
                    forList(data.getJSONArray("list"), codes);
                }
            } else {
                codes.add(data.getJSONObject("options").getString("code"));
            }
        }
    }

    @Test
    public void test() {
        ServerResponse<List<PassportUserInfoDTO>> userMsgByIds = passportFeignClient.getUserMsgByIds(Arrays.asList(7311, 1));
        System.out.println(userMsgByIds);
    }
}

import com.ruigu.rbox.workflow.WorkflowApplication;
import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.manager.PassportFeignManager;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.vo.GroupAndUserVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author liqingtian
 * @date 2020/01/10 15:01
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WorkflowApplication.class)
@Slf4j
@ActiveProfiles("local")
public class GetGroupAndUserInfoTest {

    @Resource
    private PassportFeignClient passportFeignClient;

    @Resource
    private PassportFeignManager passportFeignManager;

    @Test
    public void v1() {
        ServerResponse<List<GroupAndUserVO>> groupAndUserInfoResponse = passportFeignClient.getGroupAndUserInfo(null, null);
        if (groupAndUserInfoResponse.getCode() != ResponseCode.SUCCESS.getCode()) {
            log.error("通过权限中心获取部门信息失败");
            return;
        }
        List<GroupAndUserVO> groupAndUserInfo = groupAndUserInfoResponse.getData();
        // 二级部门id map
        Map<String, GroupInfo> map = new HashMap<>(32);
        for (GroupAndUserVO info : groupAndUserInfo) {
            if (CollectionUtils.isEmpty(info.getChildren())) {
                continue;
            }
            for (GroupAndUserVO subInfo : info.getChildren()) {
                if (subInfo.getValue() != null) {
                    GroupInfo groupInfo = new GroupInfo();
                    groupInfo.setFirstLevelGroup(info.getLabel());
                    groupInfo.setSecondLevelGroup(subInfo.getLabel());
                    map.put(subInfo.getValue().toString(), groupInfo);
                }
            }
        }
        System.out.println(map);
    }

    @Test
    public void t2() {
        Map<Integer, PassportUserInfoDTO> u1 = passportFeignManager.getUserInfoMapFromRedis(Arrays.asList(1, 2, 3));
        System.out.println(u1);

        Map<Integer, PassportUserInfoDTO> u2 = passportFeignManager.getUserInfoMapFromRedis(Collections.singletonList(1));
        System.out.println(u2);


        Map<Integer, PassportUserInfoDTO> u3 = passportFeignManager.getUserInfoMapFromRedis(Arrays.asList(1227, 1, 3));
        System.out.println(u3);

        Map<Integer, PassportUserInfoDTO> u4 = passportFeignManager.getUserInfoMapFromRedis(Collections.singletonList(1227));
        System.out.println(u4);


    }

    @Data
    private class GroupInfo {
        private String firstLevelGroup;
        private String secondLevelGroup;
    }
}

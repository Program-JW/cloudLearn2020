package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.UserGroupSimpleDTO;
import com.ruigu.rbox.workflow.model.request.UserGroupSearchReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author liqingtian
 * @date 2020/08/05 22:19
 */
@Component
@FeignClient(value = "user", url = "${rbox.user.feign.url:}")
public interface UserFeignClient {

    /**
     * 查询用户部门信息
     *
     * @param request 用户id
     * @return 用户部门信息
     */
    @RequestMapping(value = "/user/group-info", method = RequestMethod.GET)
    ResponseEntity<ServerResponse<Map<Integer, UserGroupSimpleDTO>>> getUserDeptInfo(@SpringQueryMap UserGroupSearchReq request);

}

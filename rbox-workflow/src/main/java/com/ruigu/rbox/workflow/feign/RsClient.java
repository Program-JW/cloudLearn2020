package com.ruigu.rbox.workflow.feign;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.RsAreaDTO;
import com.ruigu.rbox.workflow.model.dto.RsCustomInfoDTO;
import com.ruigu.rbox.workflow.model.dto.RsGroupInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author liqingtian
 * @date 2020/08/12 19:02
 */
@FeignClient(value = "rs", url = "${rbox.rs.feign.url}")
public interface RsClient {

    /**
     * 获取用户管理的部门
     *
     * @param rsUserId rsUserId
     * @return 部门id
     */
    @GetMapping("/salespush/getGroupInfo")
    ServerResponse<List<RsGroupInfoDTO>> queryManagerGroupByRsId(@RequestParam("rsUserId") Integer rsUserId);

    /**
     * 获取销售区域
     *
     * @param getSalesGroupAreaParams 获取销售区域参数(传空Map就行，虽然不需要任何参数但是不传会报错)
     * @return 销售区域组织架构 {@link RsAreaDTO}
     */
    @PostMapping("/salespush/getAllGroupArea")
    ServerResponse<List<RsAreaDTO>> getSalesGroupArea(Map<String, Object> getSalesGroupAreaParams);

    /**
     * 根据手机号精确匹配客户信息
     *
     * @param mobile 手机号
     * @return 客户信息
     */
    @GetMapping("/salespush/getCustomerInfo")
    ServerResponse<RsCustomInfoDTO> queryCustomInfo(@RequestParam("mobile") String mobile);

}

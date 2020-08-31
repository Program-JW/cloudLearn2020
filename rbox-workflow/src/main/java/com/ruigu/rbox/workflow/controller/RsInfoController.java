package com.ruigu.rbox.workflow.controller;

import com.ruigu.rbox.cloud.kanai.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.RsCustomInfoDTO;
import com.ruigu.rbox.workflow.model.vo.CityVO;
import com.ruigu.rbox.workflow.service.RsInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenzhenya
 * @date 2020/6/12 11:56
 */
@Api(value = "rs接口", tags = "rs接口")
@RestController
@RequestMapping("/rs")
public class RsInfoController {

    @Resource
    private RsInfoService rsInfoService;

    @GetMapping("/cities")
    @ApiOperation(value = "查询城市组织架构")
    public ServerResponse<List<CityVO>> getCities() {
        List<CityVO> cities = rsInfoService.getCities();
        return ServerResponse.ok(cities);
    }

    @GetMapping("/custom")
    @ApiOperation(value = "查询客户信息通过手机号")
    public ServerResponse<RsCustomInfoDTO> getCustomInfo(@RequestParam String mobile) {
        return ServerResponse.ok(rsInfoService.getCustomInfo(mobile));
    }
}

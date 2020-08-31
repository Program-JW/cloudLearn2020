package com.ruigu.rbox.workflow.feign.handler;

import com.google.common.collect.Maps;
import com.ruigu.rbox.workflow.exceptions.GlobalRuntimeException;
import com.ruigu.rbox.workflow.feign.RsClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.RsAreaDTO;
import com.ruigu.rbox.workflow.model.dto.RsCustomInfoDTO;
import com.ruigu.rbox.workflow.model.dto.RsGroupInfoDTO;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liqingtian
 * @date 2020/08/13 10:12
 */
@Service
public class RsClientHandler {

    @Resource
    private RsClient rsClient;

    public List<RsGroupInfoDTO> queryManageGroupInfo(Integer rsUserId) {

        ServerResponse<List<RsGroupInfoDTO>> serverResponse = rsClient.queryManagerGroupByRsId(rsUserId);
        if (!serverResponse.isSuccess()) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "获取管理区域失败");
        }
        return serverResponse.getData();

    }

    public List<RsAreaDTO> getCities() {
        ServerResponse<List<RsAreaDTO>> serverResponse = rsClient.getSalesGroupArea(Maps.newLinkedHashMapWithExpectedSize(0));
        if (!serverResponse.isSuccess()) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "获取区域信息失败");
        }
        return serverResponse.getData();
    }

    public RsCustomInfoDTO getCustomInfo(String mobile) {
        ServerResponse<RsCustomInfoDTO> serverResponse = rsClient.queryCustomInfo(mobile);
        if (!serverResponse.isSuccess()) {
            throw new GlobalRuntimeException(ResponseCode.CONDITION_EXECUTE_ERROR.getCode(), "获取客户信息失败");
        }
        return serverResponse.getData();
    }
}

package com.ruigu.rbox.workflow.service.impl;

import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.PassportUserInfoDTO;
import com.ruigu.rbox.workflow.model.entity.OperationLogEntity;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.request.OperationLogRequest;
import com.ruigu.rbox.workflow.model.vo.OperationLogVO;
import com.ruigu.rbox.workflow.repository.OperationLogRepository;
import com.ruigu.rbox.workflow.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liqingtian
 * @date 2019/09/02 19:52
 */
@Slf4j
@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Resource
    private OperationLogRepository operationLogRepository;

    @Resource
    private PassportFeignClient passportFeignClient;

    @Override
    public void log(OperationLogEntity logEntity) {
        try {
            logEntity.setStatus(1);
            operationLogRepository.save(logEntity);
        } catch (Exception e) {
            log.error("异常。日志记录失败。--> ", e);
            log.debug("--> log createdBy :" + logEntity.getCreatedBy());
            log.debug("--> log content :" + logEntity.getContent());
            log.debug("--> log createdOn :" + new Date());
        }
    }

    @Override
    public Page<OperationLogVO> getAllLogsPage(OperationLogRequest logRequest) {
        Pageable pageable = PageRequest.of(logRequest.getPageNum(), logRequest.getPageSize());
        Page<OperationLogVO> logs = operationLogRepository.selectAllLogs(logRequest, pageable);
        setCreator(logs);
        return logs;
    }

    @Override
    public List<OperationLogEntity> getInstanceLog(String instanceId) {
        return operationLogRepository.findAllByInstanceId(instanceId);
    }

    @Override
    public List<OperationLogEntity> getLogByInstanceIdAndEvent(String instanceId, String event) {
        return operationLogRepository.findAllByInstanceIdAndEvent(instanceId, event);
    }

    private void setCreator(Page<OperationLogVO> logs) {
        Set<Integer> createIds = logs.stream().distinct().map(OperationLogVO::getCreatedBy).collect(Collectors.toSet());
        ServerResponse<List<PassportUserInfoDTO>> userInfoResponse = passportFeignClient.getUserMsgByIds(createIds);
        if (userInfoResponse.getCode() == ResponseCode.SUCCESS.getCode()) {
            List<PassportUserInfoDTO> userInfoList = userInfoResponse.getData();
            if (CollectionUtils.isNotEmpty(userInfoList)) {
                logs.forEach(l -> {
                    userInfoList.stream()
                            .filter(info -> l.getCreatedBy().equals(info.getId()))
                            .findFirst().ifPresent(user -> l.setCreator(user.getNickname()));
                });
            }
        }
    }
}

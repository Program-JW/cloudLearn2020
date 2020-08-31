package com.ruigu.rbox.workflow.model;

import com.ruigu.rbox.workflow.model.entity.LightningIssueGroupEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * @author liqingtian
 * @date 2020/01/19 11:28
 */
@Slf4j
@Component
public class LightningGroupInfoMap implements InitializingBean {

    /**
     * 用于保存群组信息的map
     * key：问题id
     * value：群组信息
     * <p>
     * 用于异步消息转为同步阻塞
     */
    private ConcurrentHashMap<String, GroupInfoWithPutTimeDTO> groupInfoMap = new ConcurrentHashMap<>();

    /**
     * 添加问题群组信息
     */
    public boolean addGroupInfo(LightningIssueGroupEntity groupInfo) {
        Integer issueId = groupInfo.getIssueId();
        if (issueId == null || StringUtils.isBlank(groupInfo.getGroupId())) {
            return false;
        }
        GroupInfoWithPutTimeDTO groupInfoWithPutTimeDTO = new GroupInfoWithPutTimeDTO();
        groupInfoWithPutTimeDTO.setGroupInfo(groupInfo);
        groupInfoWithPutTimeDTO.setPutTime(LocalDateTime.now());
        groupInfoMap.put(issueId.toString(), groupInfoWithPutTimeDTO);
        return true;
    }

    /**
     * 获取群组信息
     * 超时或异常则返回null
     */
    public LightningIssueGroupEntity getGroupInfo(Integer issueId) {
        if (issueId == null) {
            return null;
        }
        String key = issueId.toString();
        // 记录进入循环之前的时间戳
        long time = System.currentTimeMillis();
        // 设置超时时间
        long timeout = 10000L;
        // 循环读取map,知道异步数据存入map
        for (; ; ) {
            // 判断是否超时，如果超时则返回null
            if (time + timeout < System.currentTimeMillis()) {
                return null;
            }
            // 如果信息已被添加入map,就可以查询到
            if (groupInfoMap.containsKey(key)) {
                // 删除并返回
                return groupInfoMap.remove(key).getGroupInfo();
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 开启定时线程 30s 刷一次 去除过期key(防止无效数据太多，oom)
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1,
                new CustomizableThreadFactory("lightningGroupMapClean"));
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                groupInfoMap.entrySet().removeIf(entry ->
                        entry.getValue().getPutTime().plusSeconds(60).isBefore(LocalDateTime.now()));
            }
        }, 1L, 60L, TimeUnit.SECONDS);
    }

    @Data
    private class GroupInfoWithPutTimeDTO {
        private LightningIssueGroupEntity groupInfo;
        private LocalDateTime putTime;
    }
}

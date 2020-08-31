package com.ruigu.rbox.workflow.repository;

import com.ruigu.rbox.workflow.model.entity.RabbitmqMsgLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author liqingtian
 * @date 2019/09/10 18:30
 */
@Repository
public interface RabbitmqMsgRepository extends JpaRepository<RabbitmqMsgLogEntity, Integer> {
    /**
     * 更新rabbitmq消费状态
     *
     * @param id rabbitmq记录id
     */
    @Modifying
    @Query(" update RabbitmqMsgLogEntity set status = 1 where id = :id ")
    void updateStatus(@Param("id") Integer id);

    /**
     * 通过微信任务id获取rabbitmq消息
     *
     * @param taskIdWeixin 微信任务id
     * @return mq消息实体
     */
    RabbitmqMsgLogEntity findByTaskIdWeixin(String taskIdWeixin);
}

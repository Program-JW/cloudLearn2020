package com.ruigu.rbox.workflow.controller.timer;

import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.service.timer.ReplaceDutyUserTimer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author liqingtian
 * @date 2020/05/13 14:45
 */
@Slf4j
@RestController
public class ReplaceDutyUserTimerController {

    @Resource
    private ReplaceDutyUserTimer replaceDutyUserTimer;

    @GetMapping("/timer/replace/duty-user")
    public ServerResponse replaceDutyUser() {
        // 1. check 技术部值班人 自动排班
        try {
            replaceDutyUserTimer.checkAndResetTechnicalDutyUser();
        } catch (Exception e) {
            log.error("技术部自动排班失败，e:{}", e);
        }
        // 2. 更换当天闪电链值班人
        replaceDutyUserTimer.replace();
        return ServerResponse.ok();
    }

    /**
     * 手动执行
     */
    @GetMapping("/timer/check-reset/technical-duty-user")
    public ServerResponse checkAndResetTechnicalDuty() {
        replaceDutyUserTimer.checkAndResetTechnicalDutyUser();
        return ServerResponse.ok();
    }

    /**
     * 新增提醒
     * （鉴于大家吐槽，开发该提醒需求，每天晚上6点，提醒一下第二天需要值班的人）
     */
    @GetMapping("/timer/duty-warm-reminder")
    public ServerResponse dutyWarmReminder() {
        replaceDutyUserTimer.warmReminder();
        return ServerResponse.ok();
    }

}

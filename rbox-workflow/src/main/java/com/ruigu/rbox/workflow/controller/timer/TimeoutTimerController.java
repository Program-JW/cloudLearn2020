package com.ruigu.rbox.workflow.controller.timer;

import com.ruigu.rbox.workflow.service.timer.TimeoutNoticeTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liqingtian
 * @date 2019/10/12 9:58
 */
@Controller
public class TimeoutTimerController {

    @Autowired
    TimeoutNoticeTimer timer;

    @RequestMapping("/timeout/timer")
    public void timeoutTimer() {
        timer.sendTimeoutNotice();
    }
}

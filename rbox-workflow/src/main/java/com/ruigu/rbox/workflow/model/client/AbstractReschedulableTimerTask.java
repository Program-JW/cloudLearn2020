package com.ruigu.rbox.workflow.model.client;

import com.ruigu.rbox.cloud.kanai.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author liqingtian
 * @date 2020/01/10 13:35
 */
@Slf4j
public abstract class AbstractReschedulableTimerTask extends TimerTask {

    public int i = 0;

    public void schedule(Timer timer, int checkInterval) {
        timer.scheduleAtFixedRate(this, 0, checkInterval);
    }

    public void reSchedule2(long newCheckInterval) {
        Date now = new Date();
        log.info("re_schedule2:{}", TimeUtil.format(now, TimeUtil.FORMAT_DATE_TIME));
        long nextExecutionTime = now.getTime() + newCheckInterval;
        setDeclaredField(TimerTask.class, this, "nextExecutionTime", nextExecutionTime);
        setDeclaredField(TimerTask.class, this, "period", newCheckInterval);

    }

    private static boolean setDeclaredField(Class<?> clazz, Object obj, String name, Object value) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}

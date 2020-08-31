import com.ruigu.rbox.workflow.WorkflowApplication;
import com.ruigu.rbox.workflow.model.dto.WholeReportDTO;
import com.ruigu.rbox.workflow.repository.LightningIssueApplyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @author caojinghong
 * @date 2020/01/10 10:43
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WorkflowApplication.class)
@Slf4j
public class WholeDataTest {
    @Autowired
    private LightningIssueApplyRepository applyRepository;

    @Test
    public void run1() {
        int count = 0;
        for (int i = 0; i < 10; i++) {
            count = count++;
        }
        System.out.println(count);
        /*LocalDate lastMonth = LocalDate.now().minusMonths(1);
        int year = lastMonth.getYear();
        int monthValue = lastMonth.getMonthValue();
        LocalDateTime start = LocalDateTime.of(lastMonth.with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(lastMonth.with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX);
        Date startTime = Date.from( start.atZone( ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from( end.atZone( ZoneId.systemDefault()).toInstant());*/
        // 查询整体的数据
        //WholeReportDTO wholeReportDTO = applyRepository.queryWholeData(startTime, endTime);
        //System.out.println(wholeReportDTO);
    }

    @Test
    public void t2() {
        Integer num = 1;
        if (null == num) {

        }


        Integer num2 = null;
//        if (num2 == 1) {
//
//        }

        if (num == num2) {

        }

        if (num2 == num) {

        }

        if (1 == num2) {

        }

        if (num2 == null) {

        }
    }
}

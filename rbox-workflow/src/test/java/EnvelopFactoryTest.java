import com.ruigu.rbox.cloud.kanai.util.TimeUtil;
import com.ruigu.rbox.workflow.WorkflowApplication;
import com.ruigu.rbox.workflow.factory.EnvelopeFactory;
import com.ruigu.rbox.workflow.factory.TtbEnvelopeChannel;
import com.ruigu.rbox.workflow.model.enums.EnvelopeChannelEnum;
import com.ruigu.rbox.workflow.model.enums.EnvelopeTypeEnum;
import com.ruigu.rbox.workflow.model.request.EnvelopeReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @author liqingtian
 * @date 2020/07/29 15:43
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WorkflowApplication.class)
@Slf4j
@ActiveProfiles("local")
public class EnvelopFactoryTest {

    @Resource
    private EnvelopeFactory envelopeFactory;

    @Test
    public void t1() {
        EnvelopeReq envelopeReq = envelopeFactory.create(EnvelopeChannelEnum.WORKFLOW, EnvelopeTypeEnum.EMAIL);
    }


    @Test
    public void t2() throws ParseException {

        String dateString = "2020-06";
        String formatString = "yyyy-MM";
        Date date = FastDateFormat.getInstance(formatString).parse(dateString);
        LocalDate localDate = TimeUtil.date2LocalDate(date);
        System.out.println(localDate);
        LocalDate with = localDate.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println(with);

    }
}

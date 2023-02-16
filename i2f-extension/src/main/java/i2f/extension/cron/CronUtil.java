package i2f.extension.cron;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/2/16 13:43
 * @desc 支持将cron解析为具体的执行时间
 * -------------------------
 * 需要依赖
 * <dependency>
 * <groupId>com.cronutils</groupId>
 * <artifactId>cron-utils</artifactId>
 * <version>9.1.5</version>
 * </dependency>
 */
public class CronUtil {
    public static void main(String[] args) {
        List<Date> list = nextQuartzCronTimes("0 0/7 * * * ?", 10);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Date date : list) {
            String str = fmt.format(date);
            System.out.println(str);
        }
    }

    public static List<Date> nextUnixCronTimes(String cron, int count) {
        return nextCronTimes(CronType.UNIX, cron, count);
    }

    public static List<Date> nextSpringCronTimes(String cron, int count) {
        return nextCronTimes(CronType.SPRING, cron, count);
    }

    public static List<Date> nextQuartzCronTimes(String cron, int count) {
        return nextCronTimes(CronType.QUARTZ, cron, count);
    }

    public static Cron getCron(CronType type, String cron) {
        CronDefinition definition = CronDefinitionBuilder.instanceDefinitionFor(type);
        CronParser parser = new CronParser(definition);
        Cron expression = parser.parse(cron);
        return expression;
    }

    public static List<Date> nextCronTimes(CronType type, String cron, int count) {
        Cron expression = getCron(type, cron);
        return nextTimes(expression, count);
    }

    public static List<Date> nextTimes(Cron cron, int count) {
        ExecutionTime executionTime = ExecutionTime.forCron(cron);
        ZonedDateTime time = null;
        List<Date> ret = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (time == null) {
                time = ZonedDateTime.now();
            }
            time = executionTime.nextExecution(time).get();
            Date date = Date.from(time.toInstant());
            ret.add(date);
        }
        return ret;
    }
}

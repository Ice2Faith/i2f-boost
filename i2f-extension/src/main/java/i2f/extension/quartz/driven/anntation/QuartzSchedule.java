package i2f.extension.quartz.driven.anntation;

import i2f.extension.quartz.driven.enums.ScheduleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuartzSchedule {

    boolean value() default true;

    ScheduleType type() default ScheduleType.Interval;

    long intervalTime() default 1000;

    TimeUnit intervalTimeUnit() default TimeUnit.MILLISECONDS;

    int intervalCount() default -1;

    String cron() default "* * * * * ? *";

    String name() ;

    String group() ;
}

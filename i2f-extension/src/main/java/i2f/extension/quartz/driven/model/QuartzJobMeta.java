package i2f.extension.quartz.driven.model;

import i2f.core.annotations.remark.Remark;
import i2f.extension.quartz.driven.anntation.QuartzSchedule;
import i2f.extension.quartz.driven.enums.ScheduleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/4/18 14:41
 * @desc
 */
@Data
@NoArgsConstructor
public class QuartzJobMeta {
    private String name;

    private String group;

    private ScheduleType type=ScheduleType.Interval;

    private long intervalTime=1000;

    private TimeUnit intervalTimeUnit=TimeUnit.MILLISECONDS;

    private int intervalCount=-1;

    private String cron="* * * * * ? *";

    private String runClassName;

    private String runMethodName;

    private Method runMethod;

    private Class runClass;

    @Remark("it will be use to invoke target method which not is static,if is null,direct instance a new object,design for spring context bean set.")
    private Object invokeObj;

    public static QuartzJobMeta build(){
        return new QuartzJobMeta();
    }

    public QuartzJobMeta buildByAnnotation(QuartzSchedule ann){
        if(ann==null || !ann.value()){
            return this;
        }
        this.name=ann.name();
        this.group=ann.group();
        this.type=ann.type();
        this.intervalTime=ann.intervalTime();
        this.intervalTimeUnit=ann.intervalTimeUnit();
        this.intervalCount=ann.intervalCount();
        this.cron=ann.cron();

        return this;
    }

    public QuartzJobMeta buildByMethod(Method method){
        if(method==null){
            return this;
        }
        this.runMethod=method;
        this.runClass=method.getDeclaringClass();
        this.runMethodName=method.getName();
        this.runClassName=method.getDeclaringClass().getName();
        return this;
    }

    public QuartzJobMeta buildOnInvokeObject(Object obj){
        this.invokeObj=obj;
        return this;
    }
}

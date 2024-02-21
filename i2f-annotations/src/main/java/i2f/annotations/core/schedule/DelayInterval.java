package i2f.annotations.core.schedule;

import i2f.annotations.doc.base.Comment;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "延迟定时",
        "用于在value指定的以unit为单位的时长的延时中，进行定时的调度，延迟就是等待上次完成之后，延迟指定时间后在此调度",
        "可通过count指定最大执行次数，-1为不限制"
})
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.LOCAL_VARIABLE,
        ElementType.METHOD,

        ElementType.TYPE_PARAMETER,
        ElementType.TYPE,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PACKAGE,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DelayInterval {
    long value();
    TimeUnit unit() default TimeUnit.MILLISECONDS;
    int count() default -1;
}

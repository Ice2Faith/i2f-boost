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
        "定时执行",
        "用于进行定时的调度任务制定，以时间间隔每固定时间进行调度，时间为value指定的以unit为单位的时长",
        "可通过count来指定最大的执行次数，-1为不限制"
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
public @interface Interval {
    long value();
    TimeUnit unit() default TimeUnit.MILLISECONDS;
    int count() default -1;
}

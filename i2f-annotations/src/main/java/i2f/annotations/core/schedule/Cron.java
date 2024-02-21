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
        "cron表达式",
        "用于指定一个cron表达式，已提供周期性的调度，可使用count来限制最大调用次数，-1则不限制"
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
public @interface Cron {
    String value();
    int count() default -1;
}

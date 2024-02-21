package i2f.annotations.core.call;

import i2f.annotations.doc.base.Comment;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "重试",
        "value指定重试次数",
        "delay指定失败后等待的时长",
        "multiplier指定失败后时长的增长系数",
        "unit指定时长的单位",
        "breakOn指定在发生哪些异常后不再进行重试",
        "逻辑：",
        "重试value次，失败后等待时长=上次等待时长*multiplier，初始等待时长=delay，当发生breakOn的异常后，不再重试",
        "以delay=4为例，multiplier=1，则每次等待时长一致，等待时长为：4,4,4,4",
        "multiplier=2，则每次等待时长递增，等待时长为：4,8,16,32",
        "multiplier=0.5，则每次等待时长递减，等待时长为：4,2,1,0"
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
public @interface Retry {
    int value() default 1;
    long delay() default 3;
    double multiplier() default 1;
    TimeUnit unit() default TimeUnit.SECONDS;
    Class<? extends Throwable>[] breakOn() default {};
}

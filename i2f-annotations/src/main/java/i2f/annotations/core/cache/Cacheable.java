package i2f.annotations.core.cache;

import i2f.annotations.doc.base.Comment;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "缓存的",
        "指定为可缓存的，或者来自缓存的对象",
        "使用value指定缓存的键前缀",
        "clazz和method表示前缀是否需要携带类名和方法名",
        "前缀=clazz+method+value",
        "缓存键=前缀+key",
        "当key为空时，表示使用全部入参作为key，不为空时，可根据需要构建自己的表达式，例如SpEL或者OGNL等的表达式",
        "使用timeout以unit单位为缓存过期时长"
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
public @interface Cacheable {
    String value() default "";
    boolean clazz() default true;
    boolean method() default true;
    long timeout() default 30;
    TimeUnit unit() default TimeUnit.SECONDS;
    String key() default "";
}

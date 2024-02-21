package i2f.annotations.core.lock;

import i2f.annotations.doc.base.Comment;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "互斥锁",
        "指定为互斥的，表示过程调用为互斥过程",
        "在使用分布式锁或者托管锁时，可以使用锁键",
        "使用value指定锁的键前缀",
        "clazz和method表示前缀是否需要携带类名和方法名",
        "锁键=clazz+method+value",
        "特别的：",
        "当inner为true时，表示锁定的是调用发生的对象上的，也就是调用对象或者对象内的成员变量",
        "此时，如果value为空，则表示锁的对象是调用对象",
        "如果value不为空，value表示的就是调用对象中的某个成员变量名",
        "当成员变量时java.util.concurrent.locks.Lock类型时，自动调用对应的lock与unlock方法实现",
        "否则使用synchronized进行对象锁",
        "同时，如果clazz为false，则表示不使用对象锁，则使用类锁"
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
public @interface Lock {
    String value() default "";
    boolean clazz() default true;
    boolean method() default true;
    boolean inner() default false;
}

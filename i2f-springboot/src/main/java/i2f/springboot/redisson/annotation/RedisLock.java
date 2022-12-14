package i2f.springboot.redisson.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {
    /**
     * redisson lock 的 lockName
     */
    String value() default "";

    /**
     * redisson lock 的 lockName 是函数的第几个入参
     */
    int keyIdx() default -1;

    /**
     * redisson lock 的 lockClass
     */
    Class<?> clazz() default Object.class;

    /**
     * redisson lock 的 lockClass 是否为方法的声明类
     */
    boolean classify() default false;

    /**
     * redisson lock 的 超时时间
     */
    long timeout() default -1;

    TimeUnit unit() default TimeUnit.SECONDS;
}

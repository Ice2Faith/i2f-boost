package i2f.core.zplugin.cache.annotations;

import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;
import i2f.core.zplugin.databind.annotations.DataBind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/6/6 9:29
 * @desc 缓存控制块
 */
@Author("i2f")
@Target({
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cached {
    @Remark("whether enable cache")
    boolean value() default true;
    @Remark("cloud be -1,mean's forever not expire")
    long expire() default 300;
    @Remark("expire time unit")
    TimeUnit unit() default TimeUnit.MILLISECONDS;
    @Remark("cache key with parameters")
    DataBind[] binds() default {};
    @Remark("basic key,while is empty will be method signature instead.")
    String key() default "";
    @Remark("when this operation end,which cache will be clean")
    String[] cleans() default {};
}

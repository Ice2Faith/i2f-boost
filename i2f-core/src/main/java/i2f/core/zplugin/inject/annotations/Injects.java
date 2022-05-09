package i2f.core.zplugin.inject.annotations;

import i2f.core.annotations.remark.Author;
import i2f.core.zplugin.inject.IInjectFieldProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2022/3/28 8:57
 * @desc 为指定的字段注入数据
 * 在Inject
 */
@Author("i2f")
@Target({
        ElementType.TYPE,
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Injects {
    String[] providerNames() default {};
    Class<IInjectFieldProvider>[] providerClasses() default {};
    String[] fields();
}

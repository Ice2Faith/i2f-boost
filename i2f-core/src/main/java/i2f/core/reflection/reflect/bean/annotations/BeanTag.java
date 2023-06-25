package i2f.core.reflection.reflect.bean.annotations;

import i2f.core.annotations.remark.Author;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2022/3/25 10:23
 * @desc
 */
@Author("i2f")
@Target({
        ElementType.TYPE,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanTag {
    String[] value() default {};
}

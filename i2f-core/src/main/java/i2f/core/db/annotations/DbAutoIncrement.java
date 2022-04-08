package i2f.core.db.annotations;

import i2f.core.annotations.remark.Author;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2022/3/23 21:26
 * @desc
 */
@Author("i2f")
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DbAutoIncrement {
    boolean value() default true;
}

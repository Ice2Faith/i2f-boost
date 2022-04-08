package i2f.core.annotations.notice;

import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;

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
@Remark("remark argument/parameter etc. name")
@Target({
        ElementType.TYPE,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
    String value() default "";
}

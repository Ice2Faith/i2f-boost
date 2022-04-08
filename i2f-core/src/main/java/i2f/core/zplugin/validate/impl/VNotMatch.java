package i2f.core.zplugin.validate.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.zplugin.validate.annotations.Validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2022/3/28 8:57
 * @desc
 */
@Author("i2f")
@Target({
        ElementType.TYPE,
        ElementType.FIELD,
        ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
@Validate(value = false,validators = RegexMatchValidator.class)
public @interface VNotMatch {
    String value() default "";
}

package i2f.core.dict.annotations;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2023/2/21 11:34
 * @desc
 */
@Target({
        ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Dicts.class)
public @interface Dict {
    String code() default "";

    String text() default "";

    String desc() default "";
}

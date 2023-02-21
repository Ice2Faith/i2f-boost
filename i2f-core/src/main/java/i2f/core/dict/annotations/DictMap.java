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
public @interface DictMap {
    String group() default "";

    String type() default "";
}

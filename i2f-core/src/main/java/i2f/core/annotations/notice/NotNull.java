package i2f.core.annotations.notice;

import java.lang.annotation.*;


@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.LOCAL_VARIABLE
})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
}

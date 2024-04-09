package i2f.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ice2Faith
 * @date 2024/4/8 17:23
 * @desc
 */
@Target({
        ElementType.FIELD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface DbPrimary {

}

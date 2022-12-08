package i2f.core.zplugin.lock.annotations;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/11/16 15:22
 * @desc
 */
@Target({
        ElementType.TYPE,
        ElementType.METHOD
})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WriteLock {
}

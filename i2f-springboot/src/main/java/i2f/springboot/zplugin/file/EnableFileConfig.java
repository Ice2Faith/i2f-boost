package i2f.springboot.zplugin.file;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/06/06 13:28
 * @desc
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        FileConfig.class,
        FileController.class,
        FileManager.class
})
public @interface EnableFileConfig {

}


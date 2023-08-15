package i2f.springboot.verifycode;

import i2f.springboot.verifycode.core.VerifyCodeContext;
import i2f.springboot.verifycode.core.VerifyCodeController;
import i2f.springboot.verifycode.core.VerifyCodeImplConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/3/27 13:28
 * @desc
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        VerifyCodeConfig.class,
        VerifyCodeContext.class,
        VerifyCodeImplConfig.class,
        VerifyCodeController.class
})
public @interface EnableVerifyCodeConfig {

}


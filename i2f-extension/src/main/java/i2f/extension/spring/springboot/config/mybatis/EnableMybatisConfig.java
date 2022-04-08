package i2f.extension.spring.springboot.config.mybatis;

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
@Import({MybatisConfig.class})
public @interface EnableMybatisConfig {

}


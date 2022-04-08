package i2f.extension.spring.springboot.config.datasource;

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
        DynamicDataSourceConfig.class,
        DataSourceAspect.class,
        DynamicDataSourceProperty.class
})
public @interface EnableDynamicDatasourceConfig {

}


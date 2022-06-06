package i2f.springboot.zplugin.config;

import i2f.springboot.zplugin.config.controller.ConfigController;
import i2f.springboot.zplugin.config.mapper.ConfigMapper;
import i2f.springboot.zplugin.config.service.ConfigService;
import i2f.springboot.zplugin.config.service.impl.ConfigServiceImpl;
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
        ConfigConfig.class,
        ConfigController.class,
        ConfigService.class,
        ConfigServiceImpl.class,
        ConfigMapper.class
})
public @interface EnableConfigConfig {

}


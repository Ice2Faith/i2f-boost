package i2f.extension.spring.springboot.config.refresh;

import i2f.core.api.ApiResp;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ltb
 * @date 2022/4/11 8:56
 * @desc
 */
@ConditionalOnBean(RefreshConfig.class)
@ConditionalOnExpression("${i2f.springboot.config.refresh.api-refresh.enable:false}")
@Slf4j
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.refresh.api-refresh")
@RestController
@RequestMapping("refresh")
public class RefreshController implements InitializingBean {

    @Autowired
    private RefreshConfig refreshConfig;

    @RequestMapping("trigger")
    public ApiResp doRefresh(){
        log.info("RefreshController trigger refresh ...");
        refreshConfig.refresh();
        log.info("RefreshController trigger refresh done.");
        return ApiResp.success("ok");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("RefreshController trigger config done.");
    }
}

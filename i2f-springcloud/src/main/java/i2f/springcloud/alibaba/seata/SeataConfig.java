package i2f.springcloud.alibaba.seata;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/5/28 19:41
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@ConditionalOnExpression("${i2f.springcloud.config.seate.enable:true}")
@ConfigurationProperties(prefix = "i2f.springcloud.config.seate")
@Configuration
public class SeataConfig implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("SeataConfig config done");
    }
}

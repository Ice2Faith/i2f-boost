package i2f.springboot.perf;

import i2f.springboot.perf.context.PerfStorage;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/3/27 13:22
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.perf.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.config.perf")
@Configuration
public class PerfConfig implements InitializingBean {
    public static PerfStorage STORE = new PerfStorage();

    private int cleanSeconds=60;

    private int maxRecords=300;

    @Override
    public void afterPropertiesSet() throws Exception {
        STORE.setCleanSeconds(cleanSeconds);
        STORE.setMaxRecords(maxRecords);
        STORE.initCleanSchedule();
        log.info("PerfConfig config done.");
    }

}

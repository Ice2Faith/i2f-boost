package i2f.springboot.trace.spy;

import i2f.springboot.trace.spy.core.InvokeTrace;
import i2f.springboot.trace.spy.core.InvokeTraceLogger;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2023/5/23 11:09
 * @desc
 */
@ConditionalOnExpression("${invoke.trace.enable:false}")
@Data
@NoArgsConstructor
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "invoke.trace")
public class SpringTraceLogger implements InitializingBean {
    public static InvokeTraceLogger LOGGER = new InvokeTraceLogger();

    public long slowMs = 300;
    public boolean showStackTrace = true;

    public boolean showArgs = false;
    public boolean showReturn = false;

    public boolean typeOnly = true;
    public boolean jsonPrint = true;
    public boolean jsonFormat = true;

    public boolean stackTraceEnable = false;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.slowMs = this.slowMs;
        LOGGER.showStackTrace = this.showStackTrace;

        LOGGER.showArgs = this.showArgs;
        LOGGER.showReturn = this.showReturn;

        LOGGER.typeOnly = this.typeOnly;
        LOGGER.jsonPrint = this.jsonPrint;
        LOGGER.jsonFormat = this.jsonFormat;

        InvokeTrace.stackTraceEnable.set(this.stackTraceEnable);
    }
}

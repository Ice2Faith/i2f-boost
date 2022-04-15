package i2f.springboot.advice;

import i2f.core.digest.AESUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ltb
 * @date 2022/4/15 10:34
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.advice.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.config.advice")
@Configuration
public class RequestResponseAdviceConfig implements InitializingBean {
    private String aesKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(aesKey==null || "".equals(aesKey)){
            aesKey="123456";
        }
        aesKey= AESUtil.genKey(aesKey);
        log.info("RequestResponseAdviceConfig aesKey config done.");
    }
}

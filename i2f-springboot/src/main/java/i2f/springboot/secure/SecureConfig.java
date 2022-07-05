package i2f.springboot.secure;


import i2f.spring.jackson.JacksonJsonProcessor;
import i2f.spring.mapping.MappingUtil;
import i2f.springboot.secure.advice.RequestBodyDecryptAdvice;
import i2f.springboot.secure.advice.StandardApiResponseAdvice;
import i2f.springboot.secure.core.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ltb
 * @date 2022/6/29 15:50
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.secure.enable:true}")
@Data
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.secure")
@Import({
        JacksonJsonProcessor.class,
        SecureTransferFilter.class,
        SecureTransferAop.class,
        SecureTransfer.class,
        MappingUtil.class,
        SecureController.class,
        EncodeRequestForwardController.class,
        RequestBodyDecryptAdvice.class,
        StandardApiResponseAdvice.class
})
public class SecureConfig {
    private String rsaStorePath="../";

    private String whiteAntPaths;

    private String responseCharset;

    private int rsaKeySize=1024;

    private boolean enableDynamicRsaKey=true;

    private long dynamicRefreshDelaySeconds=6*60;

    private int dynamicMaxHistoriesCount=5;
}

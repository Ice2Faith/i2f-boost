package i2f.springboot.secure;


import i2f.springboot.secure.consts.SecureConsts;
import i2f.springboot.secure.data.SecureCtrl;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ltb
 * @date 2022/6/29 15:50
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.secure.enable:true}")
@Data
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.secure")
public class SecureConfig {
    private String rsaStorePath="../";

    private String responseCharset="UTF-8";

    private int rsaKeySize=1024;

    private int randomKeyBound=8192;

    private int nonceTimeoutSeconds=6*60;

    private boolean enableDynamicRsaKey=true;

    private long dynamicRefreshDelaySeconds=6*60;

    private int dynamicMaxHistoriesCount=5;

    private String headerName= SecureConsts.DEFAULT_SECURE_HEADER_NAME;

    private String headerSeparator=SecureConsts.DEFAULT_HEADER_SEPARATOR;

    private String dynamicKeyHeaderName=SecureConsts.SECURE_DYNAMIC_KEY_HEADER;

    private String encUrlPath=SecureConsts.DEFAULT_ENC_URL_PATH;

    private SecureCtrl defaultControl=new SecureCtrl(true,true);

    private SecureWhiteListConfig whiteList;


    @Data
    @NoArgsConstructor
    public static class SecureWhiteListConfig{
        private Set<String> bothPattens;
        private Set<String> inPattens;
        private Set<String> outPattens;
    }
}

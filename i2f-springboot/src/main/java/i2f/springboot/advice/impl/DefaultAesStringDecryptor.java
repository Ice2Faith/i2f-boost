package i2f.springboot.advice.impl;

import i2f.core.digest.AESUtil;
import i2f.springboot.advice.IStringDecryptor;
import i2f.springboot.advice.RequestResponseAdviceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author ltb
 * @date 2022/4/15 10:11
 * @desc
 */
@ConditionalOnMissingBean(IStringDecryptor.class)
@Slf4j
@Component
public class DefaultAesStringDecryptor implements IStringDecryptor, InitializingBean {

    @Autowired
    private RequestResponseAdviceConfig adviceConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DefaultAesStringDecryptor config done.");
    }

    @Override
    public String decrypt(String text) {
        return AESUtil.decryptBeforeBase64(text,adviceConfig.getAesKey());
    }
}

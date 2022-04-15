package i2f.springboot.advice.impl;

import i2f.core.digest.AESUtil;
import i2f.spring.jackson.JacksonJsonProcessor;
import i2f.springboot.advice.IObjectEncryptor;
import i2f.springboot.advice.RequestResponseAdviceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author ltb
 * @date 2022/4/15 10:14
 * @desc
 */
@ConditionalOnMissingBean(IObjectEncryptor.class)
@Slf4j
@Component
public class DefaultAesObjectJsonEncryptor implements IObjectEncryptor, InitializingBean {

    @Autowired
    private JacksonJsonProcessor processor;

    @Autowired
    private RequestResponseAdviceConfig adviceConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DefaultAesObjectJsonEncryptor config done.");
    }

    @Override
    public String encrypt(Object obj) {
        return AESUtil.encryptJson(processor.toText(obj),adviceConfig.getAesKey());
    }
}

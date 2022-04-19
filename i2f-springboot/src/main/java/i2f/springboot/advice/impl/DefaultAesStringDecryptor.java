package i2f.springboot.advice.impl;

import i2f.core.digest.AESUtil;
import i2f.spring.jackson.JacksonJsonProcessor;
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
    private JacksonJsonProcessor processor;

    @Autowired
    private RequestResponseAdviceConfig adviceConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DefaultAesStringDecryptor config done.");
    }

    @Override
    public String decrypt(String text) {
        String json=AESUtil.decryptJsonBeforeBase64(text,adviceConfig.getAesKey());
        // 当js直接使用JSON.stringify对string进行转换时，转换结果是带有双引号的，因此此处检测后去除
        if(json.startsWith("\"")){
            json=processor.parseText(json,String.class);
        }
        return json;
    }
}

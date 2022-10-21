package i2f.spring.secret.web;

import i2f.core.secret.api.key.IKeyPair;
import i2f.core.secret.core.SecretProvider;
import i2f.core.secret.impl.ram.RamSecretProvider;
import i2f.core.secret.util.SecretUtil;
import i2f.spring.mapping.MappingUtil;
import i2f.spring.secret.web.aop.SecretAop;
import i2f.spring.secret.web.core.SecretWebCore;
import i2f.spring.secret.web.exception.SecretFilterExceptionHandler;
import i2f.spring.secret.web.filter.SecretFilter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/10/20 18:05
 * @desc
 */
@Slf4j
@Data
@Configuration
public class SecretWebConfig {
    @Value("${secret.web.white-list:}")
    private List<String> whiteList = new ArrayList<>();

    @Value("${secret.ewb.rsa-key-path:../rsa.key}")
    private String rsaKeyPath = "../rsa.key";

    private MappingUtil mappingUtil;

    private SecretFilterExceptionHandler exceptionHandler;

    public SecretWebConfig(MappingUtil mappingUtil,
                           @Autowired(required = false) SecretFilterExceptionHandler exceptionHandler) {
        this.mappingUtil = mappingUtil;
        this.exceptionHandler = exceptionHandler;
    }

    public void restoreRsaKey(SecretProvider provider) {
        File file = new File(rsaKeyPath);
        log.info("RSA秘钥文件路径：" + file.getAbsolutePath());
        try {
            if (file.exists()) {
                InputStream is = new FileInputStream(file);
                provider.mineKey = SecretUtil.loadKeyPair(is);
                log.info("RSA秘钥已重新载入");
            }
        } catch (Exception e) {
            log.warn("查找RSA秘钥文件解析失败", e);
        }

        try {
            IKeyPair key = provider.mineKey;
            OutputStream os = new FileOutputStream(file);
            SecretUtil.saveKeyPair(os, key);
        } catch (Exception e) {
            log.warn("保存RSA秘钥文件失败", e);
        }
    }

    @Bean
    public SecretProvider secretProvider() {
        SecretProvider ret = RamSecretProvider.getPkcs1Instance(true);
        restoreRsaKey(ret);
        return ret;
    }

    @Bean
    public SecretWebCore secretWebCore(SecretProvider secretProvider) {
        SecretWebCore ret = new SecretWebCore(mappingUtil, whiteList);
        ret.exceptionHandler = exceptionHandler;
        ret.secretProvider = secretProvider;
        log.info("SecretWebCore config done.");
        return ret;
    }

    @Bean
    public SecretFilter secretFilter(SecretWebCore secretWebCore) {
        SecretFilter ret = new SecretFilter(secretWebCore);
        log.info("SecretFilter config done.");
        return ret;
    }

    @Bean
    public SecretAop secretAop(SecretWebCore secretWebCore) {
        SecretAop ret = new SecretAop(secretWebCore);
        log.info("SecretAop config done.");
        return ret;
    }
}

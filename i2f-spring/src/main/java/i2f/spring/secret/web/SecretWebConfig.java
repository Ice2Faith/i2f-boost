package i2f.spring.secret.web;

import i2f.spring.mapping.MappingUtil;
import i2f.spring.secret.web.core.SecretWebCore;
import i2f.spring.secret.web.filter.SecretFilter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    private MappingUtil mappingUtil;

    public SecretWebConfig(MappingUtil mappingUtil) {
        this.mappingUtil = mappingUtil;
    }

    @Bean
    public SecretWebCore secretWebCore() {
        SecretWebCore ret = new SecretWebCore(mappingUtil, whiteList);
        log.info("SecretWebCore config done.");
        return ret;
    }

    @Bean
    public SecretFilter secretFilter(SecretWebCore secretWebCore) {
        SecretFilter ret = new SecretFilter(secretWebCore);
        log.info("SecretFilter config done.");
        return ret;
    }
}

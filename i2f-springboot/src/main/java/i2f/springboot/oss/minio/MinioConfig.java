package i2f.springboot.oss.minio;

import i2f.extension.oss.minio.MinioMeta;
import i2f.extension.oss.minio.MinioUtil;
import io.minio.MinioClient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ltb
 * @date 2022/7/8 9:54
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.minio.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@Configuration
@Import(RefreshScope.class)
@ConfigurationProperties(prefix = "i2f.springboot.config.minio")
public class MinioConfig implements InitializingBean {
    private MinioMeta meta;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("MinioConfig config done.");
    }

    @Bean
    public MinioClient minioClient(){
        return MinioUtil.getClient(meta);
    }

    @Bean
    public MinioUtil minioUtil(){
        return new MinioUtil(minioClient());
    }
}

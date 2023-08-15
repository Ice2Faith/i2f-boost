package i2f.springboot.verifycode.core;

import i2f.core.verifycode.impl.ArtTextVerifyCodeGenerator;
import i2f.core.verifycode.impl.PointNumberArthmVerifyCodeGenerator;
import i2f.core.verifycode.impl.PolarArthmVerifyCodeGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2023/8/15 17:40
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.verifycode.impl.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@Configuration
public class VerifyCodeImplConfig {

    @ConditionalOnExpression("${i2f.springboot.config.verifycode.impl.art-text.enable:true}")
    @Bean
    public ArtTextVerifyCodeGenerator artTextVerifyCodeGenerator() {
        return new ArtTextVerifyCodeGenerator();
    }

    @ConditionalOnExpression("${i2f.springboot.config.verifycode.impl.polar-arthm.enable:true}")
    @Bean
    public PolarArthmVerifyCodeGenerator polarArthmVerifyCodeGenerator() {
        return new PolarArthmVerifyCodeGenerator();
    }

    @ConditionalOnExpression("${i2f.springboot.config.verifycode.impl.point-number-arthm.enable:true}")
    @Bean
    public PointNumberArthmVerifyCodeGenerator pointNumberArthmVerifyCodeGenerator() {
        return new PointNumberArthmVerifyCodeGenerator();
    }
}

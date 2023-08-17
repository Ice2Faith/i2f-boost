package i2f.springboot.verifycode.core;

import i2f.core.verifycode.impl.*;
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

    @ConditionalOnExpression("${i2f.springboot.config.verifycode.impl.liner-marker.enable:true}")
    @Bean
    public LinerMarkerVerifyCodeGenerator linerMarkerVerifyCodeGenerator() {
        return new LinerMarkerVerifyCodeGenerator();
    }

    @ConditionalOnExpression("${i2f.springboot.config.verifycode.impl.matrix-marker.enable:true}")
    @Bean
    public MatrixMarkerVerifyCodeGenerator matrixMarkerVerifyCodeGenerator() {
        return new MatrixMarkerVerifyCodeGenerator();
    }

    @ConditionalOnExpression("${i2f.springboot.config.verifycode.impl.multi-liner-marker.enable:true}")
    @Bean
    public MultiLinerMarkerVerifyCodeGenerator multiLinerMarkerVerifyCodeGenerator() {
        return new MultiLinerMarkerVerifyCodeGenerator();
    }

    @ConditionalOnExpression("${i2f.springboot.config.verifycode.impl.multi-matrix-marker.enable:true}")
    @Bean
    public MultiMatrixMarkerVerifyCodeGenerator multiMatrixMarkerVerifyCodeGenerator() {
        return new MultiMatrixMarkerVerifyCodeGenerator();
    }
}

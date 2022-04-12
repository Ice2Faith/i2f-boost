package i2f.springboot.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ConditionalOnExpression("${i2f.springboot.config.swagger.enable:true}")
@Slf4j
@Configuration
@EnableSwagger2
@ConfigurationProperties(prefix = "i2f.springboot.config.swagger")
public class SwaggerConfig {


    private String title="Micro-Service Project Api";
    private String description="Micro-Service project.";
    private String license="Apache 2.0";
    private String licenseUrl="http://www.apache.org/licenses/LICENSE-2.0.html";
    private String version="1.0.0";


    @ConditionalOnMissingBean(ApiInfo.class)
    @Bean
    public ApiInfo apiEndPointsInfo() {
        log.info("SwaggerConfig api info config.");
        return new ApiInfoBuilder().title(title)
                .description(description)
                .contact(new Contact(null, null, null))
                .license(license)
                .licenseUrl(licenseUrl)
                .version(version)
                .build();
    }
}

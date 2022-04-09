package i2f.extension.spring.springboot.config.swagger;

import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author ltb
 * @date 2022/4/9 15:10
 * @desc
 */
@ConditionalOnBean(SwaggerConfig.class)
@Slf4j
@Configuration
@ConditionalOnExpression("${i2f.springboot.config.swagger.defaults.enable:true}")
public class DefaultSwaggerApisConfig {

    @Autowired
    private ApiInfo apiInfo;

    @ConditionalOnExpression("${i2f.springboot.config.swagger.defaults.all.enable:true}")
    @Bean
    public Docket allApi() {
        log.info("SwaggerConfig all api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("20-all")
                .select()
                .apis(RequestHandlerSelectors
                        .any())
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.defaults.normal.enable:true}")
    @Bean
    public Docket normalApi() {
        log.info("SwaggerConfig normal api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("10-normal")
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.defaults.rest-all.enable:true}")
    @Bean
    public Docket restfulApi() {
        log.info("SwaggerConfig restful api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("30-rest-all")
                .select()
                .apis(Predicates.or(RequestHandlerSelectors
                                .withClassAnnotation(RestController.class),
                        RequestHandlerSelectors
                                .withClassAnnotation(ResponseBody.class),
                        RequestHandlerSelectors
                                .withMethodAnnotation(ResponseBody.class),
                        RequestHandlerSelectors
                                .withMethodAnnotation(GetMapping.class),
                        RequestHandlerSelectors
                                .withMethodAnnotation(PostMapping.class),
                        RequestHandlerSelectors
                                .withMethodAnnotation(PutMapping.class),
                        RequestHandlerSelectors
                                .withMethodAnnotation(DeleteMapping.class)
                        )
                )
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.defaults.web.enable:true}")
    @Bean
    public Docket webApi() {
        log.info("SwaggerConfig web api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("40-web")
                .select()
                .apis(RequestHandlerSelectors
                        .withClassAnnotation(Controller.class))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.defaults.rest-get.enable:true}")
    @Bean
    public Docket getApi() {
        log.info("SwaggerConfig get api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("50-rest-get")
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors
                                .withMethodAnnotation(GetMapping.class)
                ))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.defaults.rest-post.enable:true}")
    @Bean
    public Docket postApi() {
        log.info("SwaggerConfig post api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("60-rest-post")
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors
                                .withMethodAnnotation(PostMapping.class)
                ))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.defaults.rest-put.enable:true}")
    @Bean
    public Docket putApi() {
        log.info("SwaggerConfig put api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("70-rest-put")
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors
                                .withMethodAnnotation(PutMapping.class)
                ))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }

    @ConditionalOnExpression("${i2f.springboot.config.swagger.defaults.rest-delete.enable:true}")
    @Bean
    public Docket deleteApi() {
        log.info("SwaggerConfig delete api config.");
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("80-rest-delete")
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors
                                .withMethodAnnotation(DeleteMapping.class)
                ))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo);
    }
}

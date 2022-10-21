package com.i2f.demo;


import i2f.spring.mapping.MappingUtil;
import i2f.spring.secret.web.SecretWebConfig;
import i2f.spring.secret.web.TestWeb;
import i2f.springboot.activity.EnableActivityConfig;
import i2f.springboot.application.WarBootApplication;
import i2f.springboot.asyn.EnableAsyncConfig;
import i2f.springboot.cors.EnableCorsConfig;
import i2f.springboot.datasource.EnableDynamicDatasourceConfig;
import i2f.springboot.mvc.EnableMvcConfig;
import i2f.springboot.mybatis.EnableMybatisConfig;
import i2f.springboot.redis.EnableRedisConfig;
import i2f.springboot.refresh.EnableRefreshConfig;
import i2f.springboot.restful.EnableRestTemplateConfig;
import i2f.springboot.schedule.EnableScheduleConfig;
import i2f.springboot.swagger.EnableSwaggerConfig;
import i2f.springcloud.netflix.feign.EnableFeignConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableFeignConfig
@EnableAsyncConfig
@EnableMybatisConfig
@EnableScheduleConfig
@EnableCorsConfig
@EnableDynamicDatasourceConfig
@EnableRestTemplateConfig
@EnableMvcConfig
@SpringBootApplication
@EnableFeignClients
//@EnableWebsocketConfig
//@EnableSecurityConfig
@EnableSwaggerConfig
@EnableSwagger2
@EnableRedisConfig
@EnableRefreshConfig
//@EnableQuartzConfig
//@EnableShiroConfig
@EnableActivityConfig
@EnableAutoConfiguration(exclude = {
        MongoAutoConfiguration.class,
        SecurityAutoConfiguration.class
})
@Import({
        MappingUtil.class,
        SecretWebConfig.class,
        TestWeb.class
})
public class I2fTestApplication extends WarBootApplication {

	public static void main(String[] args) {
		String log= startup(I2fTestApplication.class, args);
		System.out.println(log);
	}

}

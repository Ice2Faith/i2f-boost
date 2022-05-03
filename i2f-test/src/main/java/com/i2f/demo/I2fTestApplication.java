package com.i2f.demo;


import i2f.springboot.activity.EnableActivityConfig;
import i2f.springboot.advice.EnableHttpWebAdviceConfig;
import i2f.springboot.application.WarBootApplication;
import i2f.springboot.asyn.EnableAsyncConfig;
import i2f.springboot.cors.EnableCorsConfig;
import i2f.springboot.datasource.EnableDynamicDatasourceConfig;
import i2f.springboot.feign.EnableFeignConfig;
import i2f.springboot.mvc.EnableMvcConfig;
import i2f.springboot.mybatis.EnableMybatisConfig;
import i2f.springboot.quartz.EnableQuartzConfig;
import i2f.springboot.redis.EnableRedisConfig;
import i2f.springboot.refresh.EnableRefreshConfig;
import i2f.springboot.restful.EnableRestTemplateConfig;
import i2f.springboot.schedule.EnableScheduleConfig;
import i2f.springboot.security.EnableSecurityConfig;
import i2f.springboot.shiro.EnableShiroConfig;
import i2f.springboot.swagger.EnableSwaggerConfig;
import i2f.springboot.websocket.EnableWebsocketConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
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
@EnableWebsocketConfig
@EnableSecurityConfig
@EnableSwaggerConfig
@EnableSwagger2
@EnableRedisConfig
@EnableRefreshConfig
@EnableHttpWebAdviceConfig
@EnableQuartzConfig
@EnableShiroConfig
@EnableActivityConfig
public class I2fTestApplication extends WarBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(I2fTestApplication.class, args);
	}

}

package com.i2f;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
@Slf4j
public class AppSvcApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(AppSvcApplication.class, args);
    }

}

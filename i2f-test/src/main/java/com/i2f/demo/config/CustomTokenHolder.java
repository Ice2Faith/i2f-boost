package com.i2f.demo.config;

import i2f.springboot.security.def.token.RedisTokenHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ltb
 * @date 2022/4/7 11:05
 * @desc
 */
@Slf4j
@Component
public class CustomTokenHolder extends RedisTokenHolder {

}

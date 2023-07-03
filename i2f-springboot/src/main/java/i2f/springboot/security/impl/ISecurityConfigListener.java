package i2f.springboot.security.impl;

import i2f.springboot.security.SecurityConfig;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

/**
 * @author Ice2Faith
 * @date 2023/7/3 17:49
 * @desc
 */
public interface ISecurityConfigListener {
    default boolean onBeforeWebConfig(WebSecurity web, SecurityConfig config) {
        return true;
    }

    default void onAfterWebConfig(WebSecurity web, SecurityConfig config){

    }

    default boolean onBeforeHttpConfig(HttpSecurity http, SecurityConfig config) {
        return true;
    }

    default void onAfterHttpConfig(HttpSecurity http, SecurityConfig config){

    }
}

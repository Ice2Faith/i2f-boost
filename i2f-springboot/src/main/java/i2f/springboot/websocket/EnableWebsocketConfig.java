package i2f.springboot.websocket;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/3/27 13:28
 * @desc
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({WebSocketConfig.class,DefaultWebsocketEndpointHandler.class})
public @interface EnableWebsocketConfig {

}


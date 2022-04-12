package i2f.springboot.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;

/**
 * @author ltb
 * @date 2022/4/6 17:38
 * @desc
 */
@Slf4j
@ConditionalOnExpression("${i2f.springboot.config.websocket.default-endpoint.enable:true}")
@ServerEndpoint(value="/default/broadcast")
@Component
public class DefaultWebsocketEndpointHandler extends BasicWebsocketEndpointHandler{

}

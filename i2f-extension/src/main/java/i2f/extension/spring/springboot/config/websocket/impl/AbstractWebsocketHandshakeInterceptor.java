package i2f.extension.spring.springboot.config.websocket.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author ltb
 * @date 2022/4/6 18:26
 * @desc
 */
@Slf4j
public class AbstractWebsocketHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Map<String, Object> attributes) throws Exception {
        // 在此处可以在map中设置属性，以达到绑定用户信息
        String username="user_"+System.currentTimeMillis();
        attributes.put("username",username);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Exception e) {
        log.info("websocket handshake done.");
        if(e!=null){
            log.error("websocket handshake error:"+e.getMessage());
            e.printStackTrace();
        }
    }
}

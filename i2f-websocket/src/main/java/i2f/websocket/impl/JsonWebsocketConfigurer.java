package i2f.websocket.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.websocket.AbsWebSocketConfigurer;
import i2f.websocket.AbsWebsocketAuthenticationHandshakeInterceptor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @author Ice2Faith
 * @date 2024/3/26 13:46
 * @desc
 */
@Data
@Configuration
public class JsonWebsocketConfigurer extends AbsWebSocketConfigurer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public String path() {
        return "handler";
    }

    @Override
    public String[] getAllowedOrigins() {
        return new String[]{"*"};
    }

    @Override
    public WebSocketHandler webSocketHandler() {
        return new i2f.websocket.impl.JsonWebsocketSessionHandler(objectMapper,applicationContext,null);
    }

    @Override
    public HandshakeInterceptor websocketHandshakeInterceptor() {
        return new AbsWebsocketAuthenticationHandshakeInterceptor();
    }
}

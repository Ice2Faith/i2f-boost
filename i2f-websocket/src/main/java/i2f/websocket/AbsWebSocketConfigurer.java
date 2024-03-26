package i2f.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @author Ice2Faith
 * @date 2024/3/26 8:49
 * @desc
 */
@EnableWebSocket
public class AbsWebSocketConfigurer implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(webSocketHandler(),path())
                .setAllowedOrigins(getAllowedOrigins())
                .addInterceptors(websocketHandshakeInterceptor());
    }

    public String path(){
        return "handler";
    }

    public String[] getAllowedOrigins(){
        return new String[]{"*"};
    }

    public WebSocketHandler webSocketHandler(){
        return new AbsWebSocketSessionHandler();
    }

    public HandshakeInterceptor websocketHandshakeInterceptor(){
        return new AbsWebsocketAuthenticationHandshakeInterceptor();
    }
}

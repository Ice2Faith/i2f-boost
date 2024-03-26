package i2f.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/3/26 9:17
 * @desc
 * 通过token或者protocols进行认证
 * 重写：
 * getUidByXXX 系列方法实现获取用户ID
 * 成功返回非空值，则认为认证成功
 * 否则认证失败
 */
@Slf4j
public class AbsWebsocketAuthenticationHandshakeInterceptor implements HandshakeInterceptor {

    public static final String TOKEN_PARAMETER_KEY = "token";
    public static final String WEBSOCKET_PROTOCOL_HEADER_NAME="sec-websocket-protocol";


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
        HttpServletRequest servletRequest = serverHttpRequest.getServletRequest();
        String header = servletRequest.getHeader(WEBSOCKET_PROTOCOL_HEADER_NAME);
        String[] protocols = new String[0];
        if(header!=null && !"".equals(header)){
            protocols=header.split("\\s*,\\s*");
        }

        String uid=null;

        if(uid==null || "".equals(uid)){
            uid=getUidByRequest(request,protocols);
        }

        if(uid==null || "".equals(uid)){
            String token = servletRequest.getParameter(TOKEN_PARAMETER_KEY);
            if(token!=null && !"".equals(token)){
                uid = getUidByToken(token);
            }
        }

        if(uid==null || "".equals(uid)) {
            if (protocols != null && protocols.length > 0) {
                uid = getUidByProtocols(protocols);
            }
        }

        if (uid == null || "".equals(uid)) {
            log.error("websocket: authenticate error, bad uid found.");
            return false;
        }
        attributes.put(AbsWebSocketSessionHandler.UID_ATTR_KEY, uid);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        if(exception!=null){
            log.error("websocket: authenticated error, "+exception.getMessage(),exception);
        }
    }

    public String getUidByRequest(ServerHttpRequest request, String[] protocols) {
        return null;
    }

    public String getUidByProtocols(String[] protocols){
        return null;
    }

    public String getUidByToken(String token) {
        return token;
    }
}

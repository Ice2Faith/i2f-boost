package i2f.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/3/26 8:51
 * @desc
 */
@Slf4j
public class AbsWebSocketSessionHandler extends TextWebSocketHandler {
    protected ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    public static final String UID_ATTR_KEY = "uid";

    public String getUid(WebSocketSession session) {
        return (String) session.getAttributes().get(UID_ATTR_KEY);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String uid = getUid(session);
        String payload = message.getPayload();
        onMessage(uid, payload, message, session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String uid = getUid(session);
        sessionMap.put(uid, session);
        onAccepted(uid, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String uid = getUid(session);
        if (uid != null) {
            sessionMap.remove(uid);
        }
        onClosed(uid, status, session);
    }

    public void onAccepted(String uid, WebSocketSession session) throws IOException {
        log.info(String.format("websocket: client [%s] accepted!", uid));
    }

    public void onClosed(String uid, CloseStatus status, WebSocketSession session) throws IOException {
        log.info(String.format("websocket: client [%s] closed.", uid));
    }

    public void onMessage(String uid, String payload, TextMessage message, WebSocketSession session) throws IOException {
        log.info(String.format("websocket: client [%s] message: %s", uid, payload));
    }

    public Map<String, WebSocketSession> getSessionMap() {
        return sessionMap;
    }

    public void sendMessage(String uid, String payload) throws IOException {
        WebSocketSession session = sessionMap.get(uid);
        if (session == null) {
            throw new UnsupportedOperationException(String.format("websocket: client [%s] not connected.", uid));
        }
        session.sendMessage(new TextMessage(payload));
    }

    public void broadcastMessage(String payload) throws IOException {
        IOException ex = null;
        for (Map.Entry<String, WebSocketSession> entry : sessionMap.entrySet()) {
            try {
                entry.getValue().sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                ex = e;
            }
        }
        if (ex != null) {
            throw ex;
        }
    }

}

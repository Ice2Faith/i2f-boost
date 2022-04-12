package i2f.springboot.websocket.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ltb
 * @date 2022/4/6 18:22
 * @desc
 */
@Slf4j
public class AbstractWebSocketHandler implements WebSocketHandler {

    protected AtomicInteger onlineCount=new AtomicInteger();
    protected ConcurrentHashMap<String,WebSocketSession> clients=new ConcurrentHashMap<>();

    public String getUsername(WebSocketSession session){
        String username=(String)session.getAttributes().get("username");
        return username;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username=getUsername(session);
        onlineCount.incrementAndGet();
        clients.put(username,session);
        log.info("websocket accept client:"+username+" online:"+onlineCount);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("websocket client:"+getUsername(session)+" message:"+message.getPayload().toString());
        sendMessage("echo:"+message.getPayload().toString(),session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable error) throws Exception {
        log.error("websocket client:"+getUsername(session)+" error:"+error.getMessage());
        error.printStackTrace();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        onlineCount.decrementAndGet();
        String username=getUsername(session);
        clients.remove(username);
        log.info("websocket client close:"+username);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    public void sendMessage(String message, WebSocketSession session) throws IOException {
        session.sendMessage(new TextMessage(message));
    }

    public void broadcastMessage(String message) throws IOException {
        for(WebSocketSession item : clients.values()){
            sendMessage(message,item);
        }
    }

    public void broadcastMessageToOthers(String message,WebSocketSession send) throws IOException {
        for(WebSocketSession item : clients.values()){
            if(item.getId().equals(send.getId())){
                continue;
            }
            sendMessage(message,item);
        }
    }
}

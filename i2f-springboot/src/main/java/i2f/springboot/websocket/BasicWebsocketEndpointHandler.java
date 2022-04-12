package i2f.springboot.websocket;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ltb
 * @date 2022/4/6 17:38
 * @desc
 */
@Slf4j
//@ServerEndpoint(value = "${i2f.springboot.config.websocket.default-endpoint.path:/default/broadcast}")
//@Component
public class BasicWebsocketEndpointHandler {

    protected AtomicInteger onlineCount=new AtomicInteger();

    protected ConcurrentHashMap<String, Session> clients=new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session){
        onlineCount.incrementAndGet();
        clients.put(session.getId(), session);
        log.info("websocket client accept:"+session.getId()+" count:"+onlineCount.get());
    }

    @OnClose
    public void onClose(Session session){
        onlineCount.decrementAndGet();
        clients.remove(session.getId());
        log.info("websocket client close:"+session.getId()+" count:"+onlineCount.get());
    }

    @OnMessage
    public void onMessage(String message,Session session){
        log.info("websocket client:"+session.getId()+" message:"+message);
        sendMessage("echo:"+message,session);
    }

    @OnError
    public void onError(Session session,Throwable error){
        log.error("websocket client:"+session.getId()+" error:"+error.getMessage());
        error.printStackTrace();
    }

    public void sendMessage(String message,Session session){
        session.getAsyncRemote().sendText(message);
    }

    public void broadcastMessage(String message){
        for(Session item : clients.values()){
            sendMessage(message,item);
        }
    }

    public void broadcastMessageToOthers(String message,Session send){
        for(Session item : clients.values()){
            if(item.getId().equals(send.getId())){
                continue;
            }
            sendMessage(message,item);
        }
    }
}

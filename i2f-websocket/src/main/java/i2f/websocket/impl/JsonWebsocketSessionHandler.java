package i2f.websocket.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.websocket.AbsWebSocketSessionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/3/26 11:09
 * @desc
 */
@Slf4j
public class JsonWebsocketSessionHandler extends AbsWebSocketSessionHandler {

    protected ObjectMapper objectMapper;
    protected ApplicationContext context;
    protected JsonWebsocketExceptionHandler exceptionHandler;

    public JsonWebsocketSessionHandler(ObjectMapper objectMapper, ApplicationContext context, JsonWebsocketExceptionHandler exceptionHandler) {
        this.objectMapper = objectMapper;
        this.context = context;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void onMessage(String uid, String payload, TextMessage message, WebSocketSession session) throws IOException {
        JsonWebsocketMessage msg = parseMessage(payload);
        dispatchMessage(uid,msg,message,session);
    }

    public Map<String,JsonWebsocketMessageHandler> getHandlers(){
        String[] names = this.context.getBeanNamesForType(JsonWebsocketMessageHandler.class);
        Map<String,JsonWebsocketMessageHandler> ret=new HashMap<>();
        for (String name : names) {
            JsonWebsocketMessageHandler handler = (JsonWebsocketMessageHandler) this.context.getBean(name);
            ret.put(name,handler);
        }
        return ret;
    }

    public void dispatchMessage(String uid, JsonWebsocketMessage msg, TextMessage message, WebSocketSession session) throws IOException {
        String action = msg.getAction();
        Map<String, JsonWebsocketMessageHandler> handlers = getHandlers();
        for (Map.Entry<String, JsonWebsocketMessageHandler> entry : handlers.entrySet()) {
            JsonWebsocketMessageHandler handler = entry.getValue();
            if(handler.acceptAction(action)){
                Class<?> clazz = handler.paramClass();
                if(clazz!=null){
                    unpackMessage(msg,clazz);
                }else{
                    TypeReference<?> type = handler.paramType();
                    if(type!=null){
                        unpackMessage(msg,type);
                    }
                }
                try{
                    JsonWebsocketMessage ret = handler.handle(msg,uid,this);
                    if(ret!=null){
                        ret.setAction(msg.getAction());
                        ret.setMsgId(msg.getMsgId());
                        sendMessage(uid,ret);
                    }
                }catch(Exception e){
                    log.error(e.getMessage(), e);
                    if(exceptionHandler!=null){
                        JsonWebsocketMessage ret = exceptionHandler.handle(e);
                        ret.setAction(msg.getAction());
                        ret.setMsgId(msg.getMsgId());
                        sendMessage(uid,ret);
                    }else {
                        JsonWebsocketMessage ret = JsonWebsocketMessage.error("websocket handle error!");
                        ret.setAction(msg.getAction());
                        ret.setMsgId(msg.getMsgId());
                        sendMessage(uid,ret);
                    }
                }
            }
        }
    }

    public String encodeMessage(JsonWebsocketMessage msg) throws IOException {
        String content = objectMapper.writeValueAsString(msg.getData());
        msg.setData(content);
        String json = objectMapper.writeValueAsString(msg);
        return json;
    }

    public JsonWebsocketMessage parseMessage(String json) throws IOException {
        JsonWebsocketMessage msg = objectMapper.readValue(json, JsonWebsocketMessage.class);
        return msg;
    }

    public <T> JsonWebsocketMessage unpackMessage(JsonWebsocketMessage msg, Class<T> clazz) throws IOException {
        if (msg.getData() != null) {
            T data = objectMapper.readValue((String) msg.getData(), clazz);
            msg.setData(data);
        }
        return msg;
    }

    public <T> JsonWebsocketMessage unpackMessage(JsonWebsocketMessage msg, TypeReference<T> clazz) throws IOException {
        if (msg.getData() != null) {
            T data = objectMapper.readValue((String) msg.getData(), clazz);
            msg.setData(data);
        }
        return msg;
    }

    public <T> JsonWebsocketMessage decodeMessage(String json, Class<T> clazz) throws IOException {
        JsonWebsocketMessage msg = parseMessage(json);
        return unpackMessage(msg, clazz);
    }

    public <T> JsonWebsocketMessage decodeMessage(String json, TypeReference<T> clazz) throws IOException {
        JsonWebsocketMessage msg = parseMessage(json);
        return unpackMessage(msg, clazz);
    }

    public void sendMessage(String uid, JsonWebsocketMessage msg) throws IOException {
        super.sendMessage(uid, encodeMessage(msg));
    }

    public void broadcastMessage(JsonWebsocketMessage msg) throws IOException {
        super.broadcastMessage(encodeMessage(msg));
    }
}

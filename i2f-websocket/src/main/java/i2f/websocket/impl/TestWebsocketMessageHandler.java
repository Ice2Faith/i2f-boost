package i2f.websocket.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Ice2Faith
 * @date 2024/3/26 13:52
 * @desc
 */
@Slf4j
@Component
public class TestWebsocketMessageHandler implements JsonWebsocketMessageHandler {
    @Override
    public Class<?> paramClass() {
        return String.class;
    }

    @Override
    public TypeReference<?> paramType() {
        return null;
    }

    @Override
    public boolean acceptAction(String action) {
        return true;
    }

    @Override
    public JsonWebsocketMessage handle(JsonWebsocketMessage msg, String uid, JsonWebsocketSessionHandler handler) throws Exception {
       log.info(String.format("websocket:test client [%s] message: %s, action:%s msgId: %s",uid,msg.getData(),msg.getAction(),msg.getMsgId()));
        return JsonWebsocketMessage.success("echo:"+msg.getData());
    }
}

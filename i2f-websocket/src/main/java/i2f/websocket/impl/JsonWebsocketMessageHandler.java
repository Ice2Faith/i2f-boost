package i2f.websocket.impl;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author Ice2Faith
 * @date 2024/3/26 11:29
 * @desc
 */
public interface JsonWebsocketMessageHandler {
    Class<?> paramClass();
    TypeReference<?> paramType();
    boolean acceptAction(String action);
    JsonWebsocketMessage handle(JsonWebsocketMessage msg, String uid, JsonWebsocketSessionHandler handler) throws Exception;
}

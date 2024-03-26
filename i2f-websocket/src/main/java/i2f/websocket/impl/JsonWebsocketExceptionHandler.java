package i2f.websocket.impl;


/**
 * @author Ice2Faith
 * @date 2024/3/26 11:42
 * @desc
 */
public interface JsonWebsocketExceptionHandler {
    JsonWebsocketMessage handle(Exception e);
}

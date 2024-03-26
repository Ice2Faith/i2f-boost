package i2f.websocket.impl;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/3/26 10:51
 * @desc websocket 消息传输定义
 * 通过action区分消息类型，可以对标http的url
 * 通过msgId区分每次请求，可以实现消息的请求与响应对应
 * 其他的code,msg,data则是常规请求的内容
 */
@Data
@NoArgsConstructor
public class JsonWebsocketMessage {
    /**
     * 对标：url
     */
    protected String action;
    /**
     * 对标每次请求
     */
    protected String msgId;

    /**
     * 标准请求响应
     */
    protected int code;
    protected String msg;
    protected Object data;

    public JsonWebsocketMessage(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonWebsocketMessage(String action, String msgId, int code, String msg, Object data) {
        this.action = action;
        this.msgId = msgId;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static JsonWebsocketMessage success(Object data){
        return new JsonWebsocketMessage(200,null,data);
    }

    public static JsonWebsocketMessage error(String msg){
        return new JsonWebsocketMessage(500,msg,null);
    }
}

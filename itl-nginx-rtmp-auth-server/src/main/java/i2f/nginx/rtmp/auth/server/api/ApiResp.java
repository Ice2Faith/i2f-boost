package i2f.nginx.rtmp.auth.server.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/1/14 20:48
 * @desc
 */
@Data
@NoArgsConstructor
public class ApiResp<T> {
    private int code;
    private String msg;
    private T data;
    private Map<String, Object> kvs;

    public ApiResp(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ApiResp<T> add(String key, Object val) {
        if (kvs == null) {
            kvs = new HashMap<>();
        }
        kvs.put(key, val);
        return this;
    }

    public ApiResp<T> code(int code) {
        this.code = code;
        return this;
    }

    public ApiResp<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public ApiResp<T> data(T data) {
        this.data = data;
        return this;
    }

    public static <T> ApiResp<T> success(T data) {
        return new ApiResp<>(ApiCode.SUCCESS, "success", data);
    }

    public static <T> ApiResp<T> success(T data, String msg) {
        return new ApiResp<>(ApiCode.SUCCESS, msg, data);
    }

    public static <T> ApiResp<T> error(String msg) {
        return new ApiResp<>(ApiCode.ERROR, msg, null);
    }

    public static <T> ApiResp<T> error(int code, String msg) {
        return new ApiResp<>(code, msg, null);
    }

    public static <T> ApiResp<T> resp(int code, String msg, T data) {
        return new ApiResp<>(code, msg, data);
    }

}
package i2f.core.network.net.http.interfaces;

import i2f.core.annotations.remark.Author;
import i2f.core.network.net.http.data.HttpRequest;

import java.io.IOException;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/24 14:21
 * @desc
 */
@Author("i2f")
public interface IHttpRequestBodyHandler {
    void writeBody(Map<String,Object> data, HttpRequest request,Object output, Object ... args) throws IOException;
}

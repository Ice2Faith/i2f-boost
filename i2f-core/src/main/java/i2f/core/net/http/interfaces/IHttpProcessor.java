package i2f.core.net.http.interfaces;

import i2f.core.net.http.data.HttpRequest;
import i2f.core.net.http.data.HttpResponse;

import java.io.IOException;

/**
 * @author ltb
 * @date 2022/3/26 20:05
 * @desc
 */
public interface IHttpProcessor {
    HttpResponse doHttp(HttpRequest request) throws IOException;
}

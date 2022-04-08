package i2f.extension.httpclient;

import i2f.core.annotations.remark.Author;

/**
 * @author ltb
 * @date 2022/3/24 8:30
 * @desc
 */
@Author("i2f")
public class HttpClientUtil {
    public static volatile HttpClientProvider httpProvider = new HttpClientProvider();

    public static HttpClientProvider http(){
        return httpProvider;
    }

}

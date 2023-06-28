package i2f.extension.httpclient;

import i2f.core.annotations.remark.Author;
import i2f.core.network.net.http.impl.BasicHttpProcessorProvider;
import i2f.core.network.net.http.interfaces.IHttpProcessor;
import i2f.core.serialize.str.json.impl.Json2Serializer;
import i2f.extension.httpclient.impl.HttpClientHttpProcessor;
import i2f.extension.httpclient.impl.HttpClientRequestFormDataHandler;
import i2f.extension.httpclient.impl.HttpClientRequestJsonDataHandler;
import i2f.extension.httpclient.impl.HttpClientRequestMultipartFormDataHandler;

/**
 * @author ltb
 * @date 2022/3/26 21:07
 * @desc
 */
@Author("i2f")
public class HttpClientProvider extends BasicHttpProcessorProvider {
    public HttpClientProvider(){
        super(new HttpClientHttpProcessor());
        formRequestBodyHandler = new HttpClientRequestFormDataHandler();
        jsonRequestBodyHandler = new HttpClientRequestJsonDataHandler(new Json2Serializer());
        multipartFormDataRequestBodyHandler = new HttpClientRequestMultipartFormDataHandler();
    }
    public HttpClientProvider(IHttpProcessor processor) {
        super(processor);
        formRequestBodyHandler = new HttpClientRequestFormDataHandler();
        jsonRequestBodyHandler = new HttpClientRequestJsonDataHandler(new Json2Serializer());
        multipartFormDataRequestBodyHandler = new HttpClientRequestMultipartFormDataHandler();
    }
}

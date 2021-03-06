package i2f.extension.httpclient.impl;

import i2f.core.json.IJsonProcessor;
import i2f.core.net.http.data.HttpRequest;
import i2f.core.net.http.interfaces.IHttpRequestBodyHandler;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/26 20:42
 * @desc
 */
public class HttpClientRequestJsonDataHandler implements IHttpRequestBodyHandler {
    private IJsonProcessor jsonProcessor;
    public HttpClientRequestJsonDataHandler(IJsonProcessor jsonProcessor){
        this.jsonProcessor=jsonProcessor;
    }
    @Override
    public void writeBody(Map<String, Object> data, HttpRequest request, Object output, Object... args) throws IOException {
        HttpEntityEnclosingRequestBase httpContext=(HttpEntityEnclosingRequestBase)output;

        String content= jsonProcessor.toText(data);

        httpContext.setEntity(new StringEntity(content,"application/json","utf-8"));
    }
}

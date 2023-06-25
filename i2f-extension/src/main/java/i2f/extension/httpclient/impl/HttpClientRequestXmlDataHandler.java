package i2f.extension.httpclient.impl;

import i2f.core.network.net.http.data.HttpRequest;
import i2f.core.network.net.http.interfaces.IHttpRequestBodyHandler;
import i2f.core.serialize.xml.IXmlProcessor;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/26 20:42
 * @desc
 */
public class HttpClientRequestXmlDataHandler implements IHttpRequestBodyHandler {
    private IXmlProcessor xmlProcessor;
    public HttpClientRequestXmlDataHandler(IXmlProcessor xmlProcessor){
        this.xmlProcessor=xmlProcessor;
    }
    @Override
    public void writeBody(Map<String, Object> data, HttpRequest request, Object output, Object... args) throws IOException {
        HttpEntityEnclosingRequestBase httpContext=(HttpEntityEnclosingRequestBase)output;

        String content = xmlProcessor.serialize(data);

        httpContext.setEntity(new StringEntity(content,"text/xml","utf-8"));
    }
}

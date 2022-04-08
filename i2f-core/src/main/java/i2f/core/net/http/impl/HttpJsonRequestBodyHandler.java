package i2f.core.net.http.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.json.IJsonProcessor;
import i2f.core.json.Json2Processor;
import i2f.core.net.http.data.HttpRequest;
import i2f.core.net.http.interfaces.IHttpRequestBodyHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/24 14:26
 * @desc
 */
@Author("i2f")
public class HttpJsonRequestBodyHandler implements IHttpRequestBodyHandler {
    protected IJsonProcessor processor;
    public HttpJsonRequestBodyHandler() {
        processor=new Json2Processor();
    }
    public HttpJsonRequestBodyHandler(IJsonProcessor processor) {
        this.processor=processor;
    }
    @Override
    public void writeBody(Map<String, Object> data, HttpRequest request,Object output, Object ... args) throws IOException {
        OutputStream tos=(OutputStream)output;
        String json=processor.toText(data);
        tos.write(json.getBytes());
        tos.flush();
    }
}

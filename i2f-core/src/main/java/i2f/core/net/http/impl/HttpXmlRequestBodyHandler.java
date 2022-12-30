package i2f.core.net.http.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.net.http.data.HttpRequest;
import i2f.core.net.http.interfaces.IHttpRequestBodyHandler;
import i2f.core.xml.IXmlProcessor;
import i2f.core.xml.Xml2Processor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/24 14:26
 * @desc
 */
@Author("i2f")
public class HttpXmlRequestBodyHandler implements IHttpRequestBodyHandler {
    protected IXmlProcessor processor;
    public HttpXmlRequestBodyHandler() {
        processor=new Xml2Processor();
    }
    public HttpXmlRequestBodyHandler(IXmlProcessor processor) {
        this.processor=processor;
    }
    @Override
    public void writeBody(Map<String, Object> data, HttpRequest request,Object output, Object ... args) throws IOException {
        OutputStream tos = (OutputStream) output;
        String json = processor.serialize(data);
        tos.write(json.getBytes());
        tos.flush();
    }
}

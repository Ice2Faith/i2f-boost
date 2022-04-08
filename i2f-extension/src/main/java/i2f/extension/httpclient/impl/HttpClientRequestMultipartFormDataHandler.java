package i2f.extension.httpclient.impl;

import i2f.core.net.http.data.HttpRequest;
import i2f.core.net.http.data.MultipartFile;
import i2f.core.net.http.interfaces.IHttpRequestBodyHandler;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/26 20:42
 * @desc
 */
public class HttpClientRequestMultipartFormDataHandler implements IHttpRequestBodyHandler {
    @Override
    public void writeBody(Map<String, Object> data, HttpRequest request, Object output, Object... args) throws IOException {
        List<MultipartFile> files= request.getFiles();
        if(files==null || files.size()==0){
            new HttpClientRequestFormDataHandler().writeBody(data,request,output,args);
            return;
        }

        HttpEntityEnclosingRequestBase httpContext=(HttpEntityEnclosingRequestBase)output;

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(java.nio.charset.Charset.forName("UTF-8"));
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        // 普通表单字段
        ContentType contentType = ContentType.create("text/plain", "UTF-8");
        for(Map.Entry<String,Object> item : data.entrySet()){
            String val=item.getValue()==null?"":String.valueOf(item.getValue());
            builder.addTextBody(item.getKey(), val, contentType);
        }

        // 文件字段
        for(MultipartFile item : files){
            builder.addBinaryBody(item.getName(),item.getInputStream(),ContentType.MULTIPART_FORM_DATA,item.getFileName());
        }

        HttpEntity entity = builder.build();
        httpContext.setEntity(entity);
    }
}

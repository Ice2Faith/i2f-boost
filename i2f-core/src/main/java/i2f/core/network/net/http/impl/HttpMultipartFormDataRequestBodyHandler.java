package i2f.core.network.net.http.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.digest.Base64Util;
import i2f.core.digest.Md5Util;
import i2f.core.io.stream.StreamUtil;
import i2f.core.network.net.http.data.HttpRequest;
import i2f.core.network.net.http.data.MultipartFile;
import i2f.core.network.net.http.interfaces.IHttpRequestBodyHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/24 14:25
 * @desc
 */
@Author("i2f")
public class HttpMultipartFormDataRequestBodyHandler implements IHttpRequestBodyHandler {
    @Override
    public void writeBody(Map<String, Object> data, HttpRequest request,Object output,Object ... args) throws IOException {
        List<MultipartFile> files= request.getFiles();
        if(files==null || files.size()==0){
            new HttpFormUrlEncodedRequestBodyHandler().writeBody(data,request,output,args);
            return;
        }
        String boundary=getBoundary();
        String boundaryLine="--"+boundary+"\r\n";
        String boundaryEndLine="--"+boundary+"--\r\n";
        OutputStream os=(OutputStream)output;
        HttpURLConnection conn=(HttpURLConnection) args[0];

        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        // 表单参数
        StringBuilder builder=new StringBuilder();
        for(Map.Entry<String,Object> item : data.entrySet()){
            builder.append(boundaryLine);
            builder.append("Content-Disposition:form-data;name=\"").append(item.getKey()).append("\"");
            builder.append("\r\n\r\n");
            builder.append(item.getValue());
            builder.append("\r\n");
        }
        os.write(builder.toString().getBytes());

        //文件参数
        for(MultipartFile item : files){
            builder.setLength(0);
            builder.append(boundaryLine);
            builder.append("Content-Disposition:form-data;Content-Type:application/octet-stream;name=\"").append(item.getName()).append("\";filename=\"").append(item.getFileName()).append("\"");
            builder.append("\r\n\r\n");
            os.write(builder.toString().getBytes());

            StreamUtil.streamCopy(item.getInputStream(),os,false,true);

            os.write("\r\n\r\n".getBytes());
        }

        os.write(boundaryEndLine.getBytes());
        os.flush();

    }
    public static String getBoundary(){
        String str=Base64Util.encode("Ice2FaithHttpMultipartFormDataRequestBodyHandler2022".getBytes());
        String boundary=Md5Util.makeMD5(str);
        return boundary;
    }
}

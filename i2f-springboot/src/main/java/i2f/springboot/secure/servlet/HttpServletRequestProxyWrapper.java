package i2f.springboot.secure.servlet;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/6/29 14:06
 * @desc
 */
public class HttpServletRequestProxyWrapper extends HttpServletRequestWrapper {
    protected ByteArrayOutputStream body=new ByteArrayOutputStream();
    protected Map<String,String> headers=new ConcurrentHashMap<>();
    public HttpServletRequestProxyWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream is= request.getInputStream();
        byte[] buf=new byte[4096];
        int len=0;
        while((len=is.read(buf))>0){
            body.write(buf,0,len);
        }
        body.flush();
    }

    public HttpServletRequestProxyWrapper(HttpServletRequest request,ByteArrayServletOutputStream bos){
        super(request);
        this.body=body;
    }

    public HttpServletRequestProxyWrapper(HttpServletRequest request,byte[] body) throws IOException {
        super(request);
        this.body.write(body);
        this.body.flush();
    }

    public ByteArrayOutputStream getBody() throws IOException {
        return body;
    }


    public byte[] getBodyBytes() throws IOException {
        return body.toByteArray();
    }

    public void setAttachHeader(String name, String val){
        headers.put(name,val);
    }

    public String getAttachHeader(String name){
        return headers.get(name);
    }

    public Map<String, String> getAttachHeaders(){
        return headers;
    }


    @Override
    public String getHeader(String name) {
        String ret= super.getHeader(name);
        if(ret==null){
            ret=headers.get(name);
        }
        return ret;
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> ret=new ArrayList<>();
        Enumeration<String> enums=super.getHeaders(name);
        while(enums.hasMoreElements()){
            ret.add(enums.nextElement());
        }
        if(headers.containsKey(name)){
            ret.add(headers.get(name));
        }
        return Collections.enumeration(ret);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> ret=new ArrayList<>();
        Enumeration<String> enums=super.getHeaderNames();
        while(enums.hasMoreElements()){
            ret.add(enums.nextElement());
        }
        for(String item : headers.keySet()){
            ret.add(item);
        }
        return Collections.enumeration(ret);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return  new BufferedReader(new InputStreamReader(new ByteArrayInputStream(body.toByteArray()),getCharacterEncoding()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream bis= new ByteArrayInputStream(body.toByteArray());
        return new ByteArrayServletInputStream(bis);
    }
}

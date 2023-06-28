package i2f.core.j2ee.wrapper;

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
    protected ByteArrayOutputStream body = new ByteArrayOutputStream();
    protected Map<String, String> headers = new ConcurrentHashMap<>();
    protected String queryString;
    protected Map<String, List<String>> parameterMap;

    public HttpServletRequestProxyWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream is = request.getInputStream();
        byte[] buf = new byte[4096];
        int len = 0;
        while ((len = is.read(buf)) > 0) {
            body.write(buf, 0, len);
        }
        body.flush();
    }

    public HttpServletRequestProxyWrapper(HttpServletRequest request, ByteArrayServletOutputStream bos) {
        super(request);
        this.body = body;
    }

    public HttpServletRequestProxyWrapper(HttpServletRequest request, byte[] body) throws IOException {
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

    public void setAttachHeader(String name, String val) {
        headers.put(name, val);
    }

    public String getAttachHeader(String name) {
        return headers.get(name);
    }

    public Map<String, String> getAttachHeaders() {
        return headers;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setParameterMap(Map<String, List<String>> parameterMap) {
        this.parameterMap = parameterMap;
    }

    @Override
    public String getQueryString() {
        if (this.queryString != null) {
            return this.queryString;
        }
        return super.getQueryString();
    }

    @Override
    public String getParameter(String name) {
        if (this.parameterMap != null) {
            if (parameterMap.containsKey(name)) {
                List<String> items = this.parameterMap.get(name);
                Iterator<String> iter = items.iterator();
                if (iter.hasNext()) {
                    return iter.next();
                }
            }
        }
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (this.parameterMap != null) {
            Map<String, String[]> ret = new HashMap<>();
            for (Map.Entry<String, List<String>> entry : this.parameterMap.entrySet()) {
                String key = entry.getKey();
                String[] val = entry.getValue().toArray(new String[0]);
                ret.put(key, val);
            }
            return ret;
        }
        return super.getParameterMap();
    }

    @Override
    public Enumeration<String> getParameterNames() {
        if (this.parameterMap != null) {
            List<String> ret = new ArrayList<>();
            for (String key : this.parameterMap.keySet()) {
                ret.add(key);
            }
            Iterator<String> iter = ret.iterator();
            return new Enumeration<String>() {
                @Override
                public boolean hasMoreElements() {
                    return iter.hasNext();
                }

                @Override
                public String nextElement() {
                    return iter.next();
                }
            };
        }
        return super.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name) {
        if (this.parameterMap != null) {
            if (this.parameterMap.containsKey(name)) {
                List<String> vals = this.parameterMap.get(name);
                return vals.toArray(new String[0]);
            }
        }
        return super.getParameterValues(name);
    }

    @Override
    public String getHeader(String name) {
        String ret = headers.get(name);
        if (ret == null) {
            ret = super.getHeader(name);
        }
        return ret;
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> ret = new ArrayList<>();
        Enumeration<String> enums = super.getHeaders(name);
        while (enums.hasMoreElements()) {
            ret.add(enums.nextElement());
        }
        if (headers.containsKey(name)) {
            ret.add(headers.get(name));
        }
        return Collections.enumeration(ret);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> ret = new ArrayList<>();
        Enumeration<String> enums = super.getHeaderNames();
        while (enums.hasMoreElements()) {
            ret.add(enums.nextElement());
        }
        for (String item : headers.keySet()) {
            ret.add(item);
        }
        return Collections.enumeration(ret);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(body.toByteArray()), getCharacterEncoding()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(body.toByteArray());
        return new ByteArrayServletInputStream(bis);
    }
}

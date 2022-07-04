package i2f.core.j2ee.web;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * @author ltb
 * @date 2022/6/29 14:08
 * @desc
 */
public class HttpServletResponseProxyWrapper extends HttpServletResponseWrapper {
    protected ByteArrayOutputStream body=new ByteArrayOutputStream();
    protected ServletOutputStream soc;
    protected PrintWriter pw;
    public HttpServletResponseProxyWrapper(HttpServletResponse response) throws UnsupportedEncodingException {
        super(response);
        soc=new ByteArrayServletOutputStream(body);
        pw=new PrintWriter(new OutputStreamWriter(body,getCharacterEncoding()));
    }

    public ByteArrayOutputStream getBody() throws IOException {
        flushBuffer();
        return body;
    }

    public byte[] getBodyBytes() throws IOException {
        flushBuffer();
        return body.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return soc;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return pw;
    }

    @Override
    public void flushBuffer() throws IOException {
        pw.flush();
        soc.flush();
        body.flush();
    }

    @Override
    public void reset() {
        body.reset();
    }

    @Override
    public void resetBuffer() {
        reset();
    }
}

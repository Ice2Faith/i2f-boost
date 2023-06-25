package i2f.core.network.net.http.data;

import i2f.core.annotations.remark.Author;
import i2f.core.io.stream.StreamUtil;
import i2f.core.serialize.std.IStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/24 8:41
 * @desc
 */
@Author("i2f")
@Data
@NoArgsConstructor
public class HttpResponse implements Closeable {
    private int responseCode;
    private String responseMessage;
    private long contentLength;
    private Map<String, List<String>> header;

    private boolean parsedContentBytes;
    private InputStream inputStream;
    private byte[] contentBytes;

    private byte[] errorBytes;
    private InputStream errorStream;


    @Override
    public void close() throws IOException {
        if(inputStream!=null){
            inputStream.close();
        }
        if(errorStream!=null){
            errorStream.close();
        }
    }

    public String getContentAsString(String charset) throws UnsupportedEncodingException {
        if(parsedContentBytes){
            return new String(contentBytes,charset);
        }else{
            throw new NegativeArraySizeException("content out of size for type String.");
        }
    }

    public <T> T getContentAsObject(IStringSerializer processor, Class<T> clazz, String charset) throws IOException {
        String json = getContentAsString(charset);
        return processor.deserialize(json, clazz);
    }

    public <T> T getContentAsRef(IStringSerializer processor, Object refToken, String charset) throws IOException {
        String json = getContentAsString(charset);
        return processor.deserialize(json, refToken);
    }

    public File saveAsFile(File file) throws IOException {
        FileOutputStream fos=new FileOutputStream(file);
        StreamUtil.streamCopy(inputStream,fos,true);
        return file;
    }
}

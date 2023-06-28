package i2f.core.codec.str.url;

import i2f.core.codec.exception.CodecException;
import i2f.core.codec.str.IStringStringCodec;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:59
 * @desc
 */
public class UrlStringStringCodec implements IStringStringCodec {
    public static UrlStringStringCodec INSTANCE = new UrlStringStringCodec("UTF-8");
    private String charset = "UTF-8";

    public UrlStringStringCodec() {
    }

    public UrlStringStringCodec(String charset) {
        this.charset = charset;
    }

    @Override
    public String encode(String data) {
        try {
            return URLEncoder.encode(data, charset);
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    @Override
    public String decode(String enc) {
        try {
            return URLDecoder.decode(enc, charset);
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }
}

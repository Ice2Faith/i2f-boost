package i2f.core.serialize.bytes;

import i2f.core.codec.CodecUtil;
import i2f.core.serialize.type.ITypeSerializer;

/**
 * @author Ice2Faith
 * @date 2023/6/27 17:12
 * @desc
 */
public interface IBytesTypeSerializer<T> extends ITypeSerializer<byte[], T> {

    default String serializeAsBase64(T data) {
        byte[] enc = encode(data);
        if (enc == null) {
            return null;
        }
        return CodecUtil.toBase64(enc);
    }

    default T deserializeByBase64(String enc) {
        if (enc == null) {
            return null;
        }
        byte[] data = CodecUtil.ofBase64(enc);
        return decode(data);
    }
}

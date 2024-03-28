package i2f.jce.std.digest;

import i2f.jce.std.signature.ISignatureSigner;
import i2f.jce.std.util.ByteUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2024/3/27 8:39
 * @desc
 */
public interface IMessageDigester extends ISignatureSigner {
    byte[] digest(InputStream is) throws Exception;

    default byte[] digest(byte[] data) throws Exception {
        return digest(new ByteArrayInputStream(data));
    }

    @Override
    default byte[] sign(byte[] data) throws Exception {
        return digest(data);
    }

    default boolean verify( byte[] sign,InputStream is) throws Exception {
        byte[] data = digest(is);
        return ByteUtil.compare(data, sign);
    }

}

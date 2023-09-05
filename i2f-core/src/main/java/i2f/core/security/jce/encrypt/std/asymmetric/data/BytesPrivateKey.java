package i2f.core.security.jce.encrypt.std.asymmetric.data;

import i2f.core.codec.CodecUtil;

import java.security.PrivateKey;

/**
 * @author Ice2Faith
 * @date 2023/9/4 21:43
 * @desc
 */
public class BytesPrivateKey implements PrivateKey {
    private byte[] key;

    public BytesPrivateKey(byte[] key) {
        this.key = key;
    }

    public BytesPrivateKey(String hexKey) {
        this.key = CodecUtil.ofHexString(hexKey);
    }

    @Override
    public String getAlgorithm() {
        return "bytes";
    }

    @Override
    public String getFormat() {
        return "bytes";
    }

    @Override
    public byte[] getEncoded() {
        return key;
    }

}

package i2f.core.security.jce.encrypt.std.asymmetric.data;

import i2f.core.codec.CodecUtil;

import java.security.PublicKey;

/**
 * @author Ice2Faith
 * @date 2023/9/4 21:40
 * @desc
 */
public class BytesPublicKey implements PublicKey {
    private byte[] key;

    public BytesPublicKey(byte[] key) {
        this.key = key;
    }

    public BytesPublicKey(String hexKey) {
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

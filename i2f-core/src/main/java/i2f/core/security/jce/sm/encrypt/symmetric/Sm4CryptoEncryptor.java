package i2f.core.security.jce.sm.encrypt.symmetric;

import com.antherd.smcrypto.sm4.Sm4;
import i2f.core.codec.CodecUtil;
import i2f.core.security.jce.encrypt.std.symmetric.SymmetricEncryptor;
import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2023/9/6 13:27
 * @desc
 */
@Data
public class Sm4CryptoEncryptor implements SymmetricEncryptor {
    protected byte[] secretBytes;
    protected byte[] vectorBytes;

    public Sm4CryptoEncryptor() {

    }

    public Sm4CryptoEncryptor(byte[] secretBytes) {
        this.secretBytes = secretBytes;
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        String str = CodecUtil.ofUtf8(data);
        String key = CodecUtil.toHexString(this.secretBytes);
        String enc = Sm4.encrypt(str, key);
        return CodecUtil.toUtf8(enc);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        String str = CodecUtil.ofUtf8(data);
        String key = CodecUtil.toHexString(this.secretBytes);
        String enc = Sm4.decrypt(str, key);
        return CodecUtil.toUtf8(enc);
    }
}

package i2f.core.jce.encrypt.std.symmetric.basic;

import i2f.core.jce.encrypt.CipherUtil;
import i2f.core.jce.encrypt.IEncryptType;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.Cipher;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc 基本加解密器，使用KeyGenerator作为秘钥生成器，密码可以随机长度，内部其实是作为种子使用
 */
@Getter
@Setter
public class BasicKgenSymmetricEncryptor extends BasicSymmetricEncryptor {
    protected String secureRandomAlgotithm = "SHA1PRNG";
    protected int secretBytesLen = -1;
    protected int vectorBytesLen = -1;

    public BasicKgenSymmetricEncryptor(IEncryptType type, byte[] secretBytes) {
        super(type, secretBytes);
    }

    public BasicKgenSymmetricEncryptor(IEncryptType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type, secretBytes, vectorBytes);
    }

    @Override
    public Cipher getCipher(boolean encryptMode) throws Exception {
        Cipher cipher = CipherUtil.getCipher(type, providerName, encryptMode, secretBytes, vectorBytes,
                true, getSecureRandomAlgorithm(), secretBytesLen(), vectorBytesLen());
        return cipher;
    }


    public String getSecureRandomAlgorithm() {
        return secureRandomAlgotithm;
    }

    public int secretBytesLen() {
        return secretBytesLen < 0 ? type.secretBytesLen()[0] : secretBytesLen;
    }

    public int vectorBytesLen() {
        return vectorBytesLen < 0 ? type.vectorBytesLen()[0] : vectorBytesLen;
    }

}

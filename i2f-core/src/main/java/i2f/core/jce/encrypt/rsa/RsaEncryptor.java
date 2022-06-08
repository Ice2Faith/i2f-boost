package i2f.core.jce.encrypt.rsa;

import i2f.core.jce.encrypt.BasicEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc DES加解密器
 */
public class RsaEncryptor extends BasicEncryptor {

    public RsaEncryptor(byte[] secretBytes) {
        super(RsaType.ECB_PKCS1PADDING,secretBytes);
    }

    public RsaEncryptor(RsaType type, byte[] secretBytes) {
        super(type,secretBytes);
    }

    public RsaEncryptor(RsaType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type,secretBytes,vectorBytes);
    }

}

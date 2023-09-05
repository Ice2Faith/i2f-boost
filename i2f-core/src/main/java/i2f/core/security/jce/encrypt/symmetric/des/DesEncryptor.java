package i2f.core.security.jce.encrypt.symmetric.des;

import i2f.core.security.jce.encrypt.std.symmetric.basic.BasicSymmetricEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc DES加解密器
 */
public class DesEncryptor extends BasicSymmetricEncryptor {

    public DesEncryptor(byte[] secretBytes) {
        super(DesType.ECB_PKCS5PADDING, secretBytes);
    }

    public DesEncryptor() {
        super(DesType.ECB_PKCS5PADDING);
    }

    public DesEncryptor(DesType type) {
        super(type);
    }

    public DesEncryptor(DesType type, byte[] secretBytes) {
        super(type, secretBytes);
    }

    public DesEncryptor(DesType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type, secretBytes, vectorBytes);
    }

}

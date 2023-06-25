package i2f.core.security.jce.encrypt.symmetric.des;

import i2f.core.security.jce.encrypt.std.symmetric.basic.BasicKgenSymmetricEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc DES加解密器
 */
public class DesKgenEncryptor extends BasicKgenSymmetricEncryptor {

    public DesKgenEncryptor(byte[] secretBytes) {
        super(DesType.ECB_PKCS5PADDING, secretBytes);
    }

    public DesKgenEncryptor(DesType type, byte[] secretBytes) {
        super(type, secretBytes);
    }

    public DesKgenEncryptor(DesType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type,secretBytes,vectorBytes);
    }


}

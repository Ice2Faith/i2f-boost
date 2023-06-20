package i2f.core.jce.encrypt.symmetric.des.ede;

import i2f.core.jce.encrypt.std.symmetric.basic.BasicSymmetricEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc DES-EDE加解密器
 */
public class DesEdeEncryptor extends BasicSymmetricEncryptor {

    public DesEdeEncryptor(byte[] secretBytes) {
        super(DesEdeType.ECB_PKCS5PADDING, secretBytes);
    }

    public DesEdeEncryptor(DesEdeType type, byte[] secretBytes) {
        super(type, secretBytes);
    }

    public DesEdeEncryptor(DesEdeType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type,secretBytes,vectorBytes);
    }

}

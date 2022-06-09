package i2f.core.jce.encrypt.des;

import i2f.core.jce.encrypt.BasicKgenEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc DES加解密器
 */
public class DesKgenEncryptor extends BasicKgenEncryptor {

    public DesKgenEncryptor(byte[] secretBytes) {
        super(DesType.ECB_PKCS5PADDING,secretBytes);
    }

    public DesKgenEncryptor(DesType type, byte[] secretBytes) {
        super(type,secretBytes);
    }

    public DesKgenEncryptor(DesType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type,secretBytes,vectorBytes);
    }


}

package i2f.core.jce.encrypt.des;

import i2f.core.jce.encrypt.BasicEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc DES加解密器
 */
public class DesEncryptor extends BasicEncryptor {

    public DesEncryptor(byte[] secretBytes) {
        super(DesType.CBC_NO_PADDING,secretBytes);
    }

    public DesEncryptor(DesType type, byte[] secretBytes) {
        super(type,secretBytes);
    }

    public DesEncryptor(DesType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type,secretBytes,vectorBytes);
    }

}

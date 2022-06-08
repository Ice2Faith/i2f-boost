package i2f.core.jce.encrypt.des;

import i2f.core.jce.encrypt.BasicEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc DES-EDE加解密器
 */
public class DesEdeEncryptor extends BasicEncryptor {

    public DesEdeEncryptor(byte[] secretBytes) {
        super(DesEdeType.CBC_NO_PADDING,secretBytes);
    }

    public DesEdeEncryptor(DesEdeType type, byte[] secretBytes) {
        super(type,secretBytes);
    }

    public DesEdeEncryptor(DesEdeType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type,secretBytes,vectorBytes);
    }

}

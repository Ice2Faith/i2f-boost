package i2f.core.io.stream.encrypt;

/**
 * @author ltb
 * @date 2022/5/20 9:59
 * @desc 加解密接口，因为加密过程和解密过程是一样的，因此公用一个接口即可
 */
public interface IEncryptor {
    int encrypt(int b);
    void encrypt(byte[] bts,int offset,int len);
    void reset();
}

package i2f.jce.sm.antherd.encrypt.symmetric;

import com.antherd.smcrypto.sm4.Sm4;
import i2f.jce.jdk.encrypt.Encryptor;
import i2f.jce.jdk.supports.SecureRandomAlgorithm;
import i2f.jce.std.encrypt.symmetric.ISymmetricEncryptor;
import i2f.jce.std.util.ByteUtil;

import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/28 11:34
 * @desc
 */
public class Sm4Encryptor implements ISymmetricEncryptor {

    private String key;

    public Sm4Encryptor() {
    }

    public Sm4Encryptor(String key) {
        this.key = key;
    }

    public static final int[] SECRET_BYTE_LEN={128};
    public static int[] secretBytesLen(){
        return SECRET_BYTE_LEN;
    }

    public static String genKey() throws Exception {
        return genKey(null,SecureRandomAlgorithm.SHA1PRNG.text());
    }

    public static String genKey(byte[] vectorBytes,String secureRandomAlgorithmName) throws Exception {
        byte[] keyBytes = Encryptor.genKeyBytes(vectorBytes,
                Sm4Encryptor.secretBytesLen()[0],
                secureRandomAlgorithmName);
        return ByteUtil.toHex(keyBytes);
    }


    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        String str=new String(data,"UTF-8");
        return encrypt(str).getBytes("UTF-8");
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        String str=new String(data,"UTF-8");
        return decrypt(str).getBytes("UTF-8");
    }


    public String encrypt(String data){
        return Sm4.encrypt(data,key);
    }

    public String decrypt(String data){
        return Sm4.decrypt(data,key);
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sm4Encryptor that = (Sm4Encryptor) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "Sm4Encryptor{" +
                "key='" + key + '\'' +
                '}';
    }
}

package i2f.jce.bc.encrypt.symmetric.test;

import i2f.jce.bc.encrypt.symmetric.BcSymmetricEncryptor;
import i2f.jce.bc.encrypt.symmetric.*;
import i2f.jce.jdk.encrypt.symmetric.SymmetricEncryptor;
import i2f.jce.jdk.supports.SecureRandomAlgorithm;

/**
 * @author Ice2Faith
 * @date 2024/3/28 13:55
 * @desc
 */
public class TestBcSymmetricEcryptor {
    public static void main(String[] args) throws Exception {
        SymmetricEncryptor encryptor = BcSymmetricEncryptor.genKeyEncryptor(AesType.ECB_PKCS5Padding,
                "hello".getBytes(), "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(encryptor);

        encryptor = BcSymmetricEncryptor.genKeyEncryptor(AesType.CBC_ISO10126Padding,
                "hello".getBytes(), "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(encryptor);

        encryptor = BcSymmetricEncryptor.genKeyEncryptor(DesType.ECB_PKCS5PADDING,
                "hello".getBytes(), "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(encryptor);

        encryptor = BcSymmetricEncryptor.genKeyEncryptor(DesType.CBC_PKCS5PADDING,
                "hello".getBytes(), "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(encryptor);

        encryptor = BcSymmetricEncryptor.genKeyEncryptor(DesEdeType.ECB_NO_PADDING,
                "hello".getBytes(), "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(encryptor);

        encryptor = BcSymmetricEncryptor.genKeyEncryptor(Sm4Type.ECB_PKCS5PADDING,
                "hello".getBytes(), "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(encryptor);
    }

    public static void test(SymmetricEncryptor encryptor) throws Exception {
        System.out.println("------------------------------------");
        System.out.println(encryptor.getAlgorithmName());
        String data = "hello";
        byte[] enc = encryptor.encrypt(data.getBytes());
        byte[] dec = encryptor.decrypt(enc);
        String str = new String(dec);
        System.out.println(data.equals(str));
    }
}

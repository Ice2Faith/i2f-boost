package i2f.jce.jdk.encrypt.symmetric.test;

import i2f.jce.jdk.encrypt.symmetric.*;
import i2f.jce.jdk.supports.SecureRandomAlgorithm;

/**
 * @author Ice2Faith
 * @date 2024/3/28 13:55
 * @desc
 */
public class TestSymmetricEcryptor {
    public static void main(String[] args) throws Exception {
        SymmetricEncryptor encryptor = SymmetricEncryptor.genKeyEncryptor(AesType.ECB_PKCS5Padding,
                "hello".getBytes(), "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(encryptor);

        encryptor = SymmetricEncryptor.genKeyEncryptor(AesType.CBC_ISO10126Padding,
                "hello".getBytes(), "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(encryptor);

        encryptor = SymmetricEncryptor.genKeyEncryptor(DesType.ECB_PKCS5PADDING,
                "hello".getBytes(), "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(encryptor);

        encryptor = SymmetricEncryptor.genKeyEncryptor(DesType.CBC_PKCS5PADDING,
                "hello".getBytes(), "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(encryptor);

        encryptor = SymmetricEncryptor.genKeyEncryptor(DesEdeType.ECB_NO_PADDING,
                "hello".getBytes(), "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(encryptor);


        encryptor = SymmetricEncryptor.genKeyEncryptor(Rc2Type.CBC_PKCS5PADDING,
                "hello".getBytes(), "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(encryptor);


        encryptor = SymmetricEncryptor.genKeyEncryptor(BlowfishType.CBC_PKCS5PADDING,
                "hello".getBytes(), "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(encryptor);

        encryptor = SymmetricEncryptor.genPbeKeyEncryptor(PbeType.PBEWithMD5AndDES,
                "hello".getBytes(), "hello".getBytes(),
                3, SecureRandomAlgorithm.SHA1PRNG.text());

        test(encryptor);

        encryptor = SymmetricEncryptor.genPbeKeyEncryptor(PbeType.PBEWithSHA1AndDESede,
                "hello".getBytes(), "hello".getBytes(),
                3, SecureRandomAlgorithm.SHA1PRNG.text());

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

package i2f.jce.jdk.encrypt.asymmetric.test;

import i2f.jce.jdk.encrypt.Encryptor;
import i2f.jce.jdk.encrypt.asymmetric.AsymmetricEncryptor;
import i2f.jce.jdk.encrypt.asymmetric.AsymmetricType;
import i2f.jce.jdk.encrypt.asymmetric.ElGamalType;
import i2f.jce.jdk.encrypt.asymmetric.RsaType;
import i2f.jce.jdk.supports.SecureRandomAlgorithm;

/**
 * @author Ice2Faith
 * @date 2024/3/28 15:31
 * @desc
 */
public class TestAsymmetricEncryptor {

    public static void main(String[] args) throws Exception {
        AsymmetricEncryptor encryptor = AsymmetricEncryptor.genKeyEncryptor(RsaType.ECB_PKCS1PADDING,
                "hello".getBytes(),SecureRandomAlgorithm.SHA1PRNG.text(), null);

        test(encryptor);

        encryptor = AsymmetricEncryptor.genKeyEncryptor(RsaType.ECB_OAEPPadding,
                "hello".getBytes(),SecureRandomAlgorithm.SHA1PRNG.text(), null);

        test(encryptor);



    }

    public static void test(AsymmetricEncryptor encryptor) throws Exception {
        System.out.println("---------------------------");
        System.out.println(encryptor.getAlgorithmName());
        String data = "hello";
        byte[] enc = encryptor.encrypt(data.getBytes());
        byte[] dec = encryptor.decrypt(enc);
        String str = new String(dec);
        System.out.println(data.equals(str));
    }
}

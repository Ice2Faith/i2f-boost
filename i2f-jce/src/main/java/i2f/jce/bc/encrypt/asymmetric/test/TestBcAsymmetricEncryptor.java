package i2f.jce.bc.encrypt.asymmetric.test;

import i2f.jce.bc.encrypt.asymmetric.BcAsymmetricEncryptor;
import i2f.jce.bc.encrypt.asymmetric.BcSm2Encryptor;
import i2f.jce.bc.encrypt.asymmetric.ElGamalType;
import i2f.jce.jdk.encrypt.asymmetric.AsymmetricEncryptor;
import i2f.jce.bc.encrypt.asymmetric.RsaType;
import i2f.jce.jdk.supports.SecureRandomAlgorithm;

import java.security.KeyPair;

/**
 * @author Ice2Faith
 * @date 2024/3/28 15:31
 * @desc
 */
public class TestBcAsymmetricEncryptor {

    public static void main(String[] args) throws Exception {
        AsymmetricEncryptor encryptor = BcAsymmetricEncryptor.genKeyEncryptor(RsaType.ECB_PKCS1PADDING,
                "hello".getBytes(),SecureRandomAlgorithm.SHA1PRNG.text(), null);

        test(encryptor);

        encryptor = BcAsymmetricEncryptor.genKeyEncryptor(RsaType.ECB_OAEPPadding,
                "hello".getBytes(),SecureRandomAlgorithm.SHA1PRNG.text(), null);

        test(encryptor);


        encryptor = BcAsymmetricEncryptor.genKeyEncryptor(ElGamalType.ECB_OAEPPadding,
                "hello".getBytes(),SecureRandomAlgorithm.SHA1PRNG.text(), null);

        test(encryptor);

        testSm2();
    }

    public static void testSm2() throws Exception {
        BcSm2Encryptor sm2Encryptor= new BcSm2Encryptor(BcSm2Encryptor.genKeyPair());

        System.out.println("---------------------------");
        System.out.println(sm2Encryptor.getClass().getSimpleName());
        String data = "hello";
        byte[] enc = sm2Encryptor.encrypt(data.getBytes());
        byte[] dec = sm2Encryptor.decrypt(enc);
        String str = new String(dec);
        System.out.println(data.equals(str));

        byte[] sign = sm2Encryptor.sign(data.getBytes());
        System.out.println(sm2Encryptor.verify(sign, data.getBytes()));
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

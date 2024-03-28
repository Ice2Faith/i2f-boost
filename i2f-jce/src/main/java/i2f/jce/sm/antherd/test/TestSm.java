package i2f.jce.sm.antherd.test;

import com.antherd.smcrypto.sm2.Keypair;
import i2f.jce.sm.antherd.digest.Sm3Digester;
import i2f.jce.sm.antherd.encrypt.asymmetric.Sm2Encryptor;
import i2f.jce.sm.antherd.encrypt.symmetric.Sm4Encryptor;
import i2f.jce.sm.antherd.signature.Sm2SignatureSigner;

/**
 * @author Ice2Faith
 * @date 2024/3/28 14:28
 * @desc
 */
public class TestSm {
    public static void main(String[] args) throws Exception {
        testSm3();
        testSm4();
        testSm2();
        testSmSigner();
    }

    public static void testSmSigner() throws Exception {
        System.out.println("--------------------------");
        String data="hello";
        Keypair keypair = Sm2SignatureSigner.genKey();
        Sm2SignatureSigner signer=new Sm2SignatureSigner(keypair);

        System.out.println(data);
        System.out.println(keypair.getPublicKey());
        System.out.println(keypair.getPrivateKey());

        String sign = signer.sign(data);
        System.out.println(sign);
        System.out.println(signer.verify(sign, data));
    }

    public static void testSm2() throws Exception {
        System.out.println("--------------------------");
        String data="hello";
        Keypair keypair = Sm2Encryptor.genKey();
        Sm2Encryptor encryptor=new Sm2Encryptor(keypair);

        String enc = encryptor.encrypt(data);
        String dec = encryptor.decrypt(enc);

        System.out.println(data);
        System.out.println(keypair.getPublicKey());
        System.out.println(keypair.getPrivateKey());
        System.out.println(enc);
        System.out.println(dec);

        System.out.println(data.equals(dec));

        String sign = encryptor.sign(data);
        System.out.println(sign);
        System.out.println(encryptor.verify(sign, data));
    }

    public static void testSm3() throws Exception {
        System.out.println("--------------------------");
        String data="hello";
        String sign = Sm3Digester.INSTANCE.digest(data);

        System.out.println(data);
        System.out.println(sign);

        System.out.println(Sm3Digester.INSTANCE.verify(sign, data));
    }

    public static void testSm4() throws Exception {
        System.out.println("--------------------------");
        String key = Sm4Encryptor.genKey();
        Sm4Encryptor encryptor=new Sm4Encryptor(key);

        String data="hello";
        String enc = encryptor.encrypt(data);
        String dec = encryptor.decrypt(enc);

        System.out.println(key);
        System.out.println(data);
        System.out.println(enc);
        System.out.println(enc);

        System.out.println(data.equals(dec));
    }
}

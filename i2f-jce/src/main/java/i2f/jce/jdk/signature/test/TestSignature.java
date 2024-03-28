package i2f.jce.jdk.signature.test;

import i2f.jce.jdk.signature.DsaType;
import i2f.jce.jdk.signature.EcdsaType;
import i2f.jce.jdk.signature.RsaType;
import i2f.jce.jdk.signature.SignatureSigner;
import i2f.jce.jdk.supports.SecureRandomAlgorithm;

/**
 * @author Ice2Faith
 * @date 2024/3/28 14:53
 * @desc
 */
public class TestSignature {
    public static void main(String[] args) throws Exception {
        SignatureSigner signer =SignatureSigner.genKeySignatureSigner(RsaType.MD5withRSA,
                "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(signer);

        signer =SignatureSigner.genKeySignatureSigner(RsaType.SHA1withRSA,
                "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(signer);

        signer =SignatureSigner.genKeySignatureSigner(DsaType.SHA1withDSA,
                "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(signer);


        signer =SignatureSigner.genKeySignatureSigner(EcdsaType.SHA256withECDSA,
                "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(signer);
    }

    public static void test(SignatureSigner signer) throws Exception {
        System.out.println("---------------------------");
        System.out.println(signer.getAlgorithmName());
        byte[] data="hello".getBytes();
        byte[] sign = signer.sign(data);
        boolean ok = signer.verify(sign, data);
        System.out.println(ok);
    }
}

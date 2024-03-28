package i2f.jce.bc.signature.test;

import i2f.jce.bc.signature.BcSignatureSigner;
import i2f.jce.bc.signature.DsaType;
import i2f.jce.bc.signature.EcdsaType;
import i2f.jce.bc.signature.RsaType;
import i2f.jce.jdk.signature.SignatureSigner;
import i2f.jce.jdk.supports.SecureRandomAlgorithm;

/**
 * @author Ice2Faith
 * @date 2024/3/28 14:53
 * @desc
 */
public class TestBcSignature {
    public static void main(String[] args) throws Exception {
        SignatureSigner signer = BcSignatureSigner.genKeySignatureSigner(RsaType.SHA224withRSA,
                "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(signer);

        signer =BcSignatureSigner.genKeySignatureSigner(RsaType.RIPEMD128withRSA,
                "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(signer);

        signer =BcSignatureSigner.genKeySignatureSigner(DsaType.SHA256withDSA,
                "hello".getBytes(),
                SecureRandomAlgorithm.SHA1PRNG.text());

        test(signer);


        signer =BcSignatureSigner.genKeySignatureSigner(EcdsaType.SHA256withECDSA,
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

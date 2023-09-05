package i2f.secure.crypto;

import com.antherd.smcrypto.sm2.Keypair;
import com.antherd.smcrypto.sm2.Sm2;
import i2f.core.codec.CodecUtil;
import i2f.core.security.jce.encrypt.std.asymmetric.data.BytesPrivateKey;
import i2f.core.security.jce.encrypt.std.asymmetric.data.BytesPublicKey;

import java.security.KeyPair;

/**
 * @author Ice2Faith
 * @date 2023/9/4 21:46
 * @desc
 */
public class Sm2CryptoUtils {
    public static final String ALGO_NAME = "sm-crypto";
    public static final String KEY_FORMAT = "hex";

    public static final int MODE_C1C3C2 = 0;
    public static final int MODE_C1C2C3 = 1;

    public static KeyPair genKeyPair() {
        Keypair keypair = Sm2.generateKeyPairHex();
        String privateKey = keypair.getPrivateKey(); // 公钥
        String publicKey = keypair.getPublicKey(); // 私钥
        return new KeyPair(
                new BytesPublicKey(publicKey.getBytes()),
                new BytesPrivateKey(privateKey.getBytes())
        );
    }

    public static byte[] publicEncrypt(byte[] data, String publicHex) throws Exception {
        return publicEncrypt(data, publicHex, MODE_C1C2C3);
    }

    public static byte[] publicEncrypt(byte[] data, String publicHex, int mode) throws Exception {
        String bs64 = CodecUtil.toBase64(data);
        String result = Sm2.doEncrypt(bs64, publicHex, mode);
        return CodecUtil.toUtf8(result);
    }

    public static byte[] privateDecrypt(byte[] data, String privateHex) throws Exception {
        return privateDecrypt(data, privateHex, MODE_C1C2C3);
    }

    public static byte[] privateDecrypt(byte[] data, String privateHex, int mode) throws Exception {
        String str = CodecUtil.ofUtf8(data);
        String bs64 = Sm2.doDecrypt(str, privateHex, mode);
        return CodecUtil.ofBase64(bs64);
    }

    public static byte[] makeSign(byte[] data, String privateHex) throws Exception {
        String bs64 = CodecUtil.toBase64(data);
        String sign = Sm2.doSignature(bs64, privateHex);
        return CodecUtil.toUtf8(sign);
    }

    public static boolean verifySign(byte[] sign, byte[] data, String publicHex) throws Exception {
        String str = CodecUtil.ofUtf8(sign);
        String bs64 = CodecUtil.toBase64(data);
        boolean ok = Sm2.doVerifySignature(bs64, str, publicHex);
        return ok;
    }
}

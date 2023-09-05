package i2f.secure;

import com.antherd.smcrypto.sm2.Keypair;
import com.antherd.smcrypto.sm2.Sm2;
import com.antherd.smcrypto.sm3.Sm3;
import com.antherd.smcrypto.sm4.Sm4;
import i2f.core.security.jce.encrypt.std.asymmetric.AsymmetricEncryptor;
import i2f.secure.crypto.Sm2CryptoEncryptor;

/**
 * @author Ice2Faith
 * @date 2023/9/4 13:52
 * @desc
 */
public class TestSm {
    public static void main(String[] args) throws Exception {
        Keypair keypair = Sm2.generateKeyPairHex();
        String privateKey = keypair.getPrivateKey(); // 公钥
        String publicKey = keypair.getPublicKey(); // 私钥
        System.out.println("publicKey:" + publicKey);
        System.out.println("privateKey:" + privateKey);

        String msg = "这是加解密内容";
        int cipherMode = 1;
        // cipherMode 1 - C1C3C2，0 - C1C2C3，默认为1
        String encryptData = Sm2.doEncrypt(msg, publicKey, cipherMode); // 加密结果
        String decryptData = Sm2.doDecrypt(encryptData, privateKey, cipherMode); // 解密结果
        System.out.println("msg:" + msg);
        System.out.println("encryptData:" + encryptData);
        System.out.println("decryptData:" + decryptData);

        String encrypt = Sm2Utils.encrypt(publicKey, msg);
        // TODO wrong
        String decrypt = Sm2Utils.decrypt(privateKey, encryptData);
        System.out.println("encrypt:" + encrypt);
        System.out.println("decrypt:" + decrypt);


        // 纯签名 + 生成椭圆曲线点
        String sigValueHex = Sm2.doSignature(msg, privateKey); // 签名
        boolean verifyResult = Sm2.doVerifySignature(msg, sigValueHex, publicKey); // 验签结果
        System.out.println("sigValueHex:" + sigValueHex);
        System.out.println("verifyResult:" + verifyResult);

        System.out.println("------------------------");
        AsymmetricEncryptor encryptor = new Sm2CryptoEncryptor(1, publicKey, privateKey);
        byte[] encBytes = encryptor.publicEncrypt(msg.getBytes());
        byte[] decData = encryptor.privateDecrypt(encBytes);
        System.out.println("encStr:" + new String(decData));

        byte[] signBytes = encryptor.makeSign(msg.getBytes());
        boolean signOk = encryptor.verifySign(signBytes, msg.getBytes());

        System.out.println("signOk:" + signOk);

        System.out.println("--------------------------");
        String hs = Sm3.sm3("这是待hash的内容");
        System.out.println("sm3-hash:" + hs);

        System.out.println("--------------------");
        // hex mode
        String key = "000102030405060708090a0b0c0d0e0f";// 128bit/8byte
        String enc = Sm4.encrypt(msg, key);
        System.out.println("enc:" + enc);
        String dec = Sm4.decrypt(enc, key);
        System.out.println("dec:" + dec);
    }
}

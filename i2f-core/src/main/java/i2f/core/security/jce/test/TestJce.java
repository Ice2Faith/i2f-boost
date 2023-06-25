package i2f.core.security.jce.test;

import i2f.core.security.jce.bc.BouncyCastleHolder;
import i2f.core.security.jce.bc.digest.sm.BcSm3MessageDigester;
import i2f.core.security.jce.bc.digest.sm.BcSm3Type;
import i2f.core.security.jce.bc.encrypt.asymmetric.sm2.BcSm2Encryptor;
import i2f.core.security.jce.bc.encrypt.asymmetric.sm2.BcSm2Type;
import i2f.core.security.jce.bc.encrypt.symmetric.sm4.BcSm4Encryptor;
import i2f.core.security.jce.bc.encrypt.symmetric.sm4.BcSm4Type;
import i2f.core.security.jce.digest.MessageDigestUtil;
import i2f.core.security.jce.digest.sha.ShaType;
import i2f.core.security.jce.encrypt.CipherUtil;
import i2f.core.security.jce.encrypt.asymmetric.rsa.RsaEncryptor;
import i2f.core.security.jce.encrypt.asymmetric.rsa.RsaType;
import i2f.core.security.jce.encrypt.std.asymmetric.AsymmetricEncryptor;
import i2f.core.security.jce.encrypt.symmetric.aes.AesKgenEncryptor;
import i2f.core.security.jce.encrypt.symmetric.aes.AesType;
import i2f.core.security.jce.encrypt.symmetric.des.DesKgenEncryptor;
import i2f.core.security.jce.encrypt.symmetric.des.DesType;
import i2f.core.security.jce.encrypt.symmetric.des.ede.DesEdeKgenEncryptor;
import i2f.core.security.jce.encrypt.symmetric.des.ede.DesEdeType;
import org.bouncycastle.crypto.CryptoServiceProperties;

import java.security.KeyPair;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:22
 * @desc
 */
public class TestJce {

    private static CryptoServiceProperties service(String var0, int var1) {
        return null;
    }


    public static void main(String[] args) throws Exception {


        if (true) {
            BouncyCastleHolder.registry();
            KeyPair keyPair = CipherUtil.genKeyPair(BcSm2Type.DEFAULT, BouncyCastleHolder.PROVIDER_NAME, "123456".getBytes(), 256, null);

            AsymmetricEncryptor encryptor = new BcSm2Encryptor(BcSm2Type.DEFAULT, keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded());

//            byte[] pridata = encryptor.privateEncrypt("hello".getBytes());
//            byte[] ddata = encryptor.publicDecrypt(pridata);
//            System.out.println(new String(ddata));

            byte[] ddata = "hello".getBytes();
            byte[] pubdata = encryptor.publicEncrypt(ddata);
            byte[] pdata = encryptor.privateDecrypt(pubdata);
            System.out.println(new String(pdata));
        }

        if (true) {
            BcSm4Encryptor encryptor = new BcSm4Encryptor(BcSm4Type.ECB_ISO10126PADDING, "0123456789ABCDEF".getBytes());
            byte[] edata = encryptor.encrypt("hello".getBytes());
            byte[] ddata = encryptor.decrypt(edata);
            String str = new String(ddata);
            System.out.println(str);
        }

        if (true) {
            String sm3 = new BcSm3MessageDigester(BcSm3Type.SM3).mdsText("hello".getBytes());
            System.out.println(sm3);
        }
        if (true) {
            String md5 = MessageDigestUtil.MD5.mdsText("hello".getBytes());
            String sha256 = MessageDigestUtil.SHA256.mdsText("hello".getBytes());
            System.out.println(md5);
            System.out.println(sha256);
        }

        if (true) {
            String hmacsha256 = MessageDigestUtil.hmac(ShaType.SHA256, "123456".getBytes()).mdsText("hello".getBytes());
            System.out.println(hmacsha256);
        }

        if (true) {
            AesKgenEncryptor encryptor = new AesKgenEncryptor(AesType.ECB_ISO10126PADDING, "123456".getBytes());
            byte[] edata = encryptor.encrypt("hello".getBytes());
            byte[] ddata = encryptor.decrypt(edata);
            String dstr = new String(ddata);
            System.out.println(dstr);
        }

        if (true) {
            DesKgenEncryptor encryptor = new DesKgenEncryptor(DesType.ECB_PKCS5PADDING, "123456".getBytes());
            byte[] edata = encryptor.encrypt("hello".getBytes());
            byte[] ddata = encryptor.decrypt(edata);
            String dstr = new String(ddata);
            System.out.println(dstr);
        }

        if (true) {
            DesEdeKgenEncryptor encryptor = new DesEdeKgenEncryptor(DesEdeType.ECB_PKCS5PADDING, "123456".getBytes());
            byte[] edata = encryptor.encrypt("hello".getBytes());
            byte[] ddata = encryptor.decrypt(edata);
            String dstr = new String(ddata);
            System.out.println(dstr);
        }

        if (true) {
            KeyPair keyPair = CipherUtil.genKeyPair(RsaType.ECB_PKCS1PADDING, "123456".getBytes(), 1024, null);

            AsymmetricEncryptor encryptor = new RsaEncryptor(RsaType.ECB_PKCS1PADDING, keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded());

            byte[] pridata = encryptor.privateEncrypt("hello".getBytes());
            byte[] ddata = encryptor.publicDecrypt(pridata);
            System.out.println(new String(ddata));

            byte[] pubdata = encryptor.publicEncrypt(ddata);
            byte[] pdata = encryptor.privateDecrypt(pubdata);
            System.out.println(new String(pdata));
        }
    }
}

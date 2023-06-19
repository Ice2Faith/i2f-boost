package i2f.core.jce.test;

import i2f.core.jce.digest.MessageDigestUtil;
import i2f.core.jce.digest.sha.ShaType;
import i2f.core.jce.encrypt.asymmetric.AsymmetricEncryptor;
import i2f.core.jce.encrypt.asymmetric.rsa.RsaEncryptor;
import i2f.core.jce.encrypt.asymmetric.rsa.RsaType;
import i2f.core.jce.encrypt.symmetric.aes.AesKgenEncryptor;
import i2f.core.jce.encrypt.symmetric.aes.AesType;
import i2f.core.jce.encrypt.symmetric.des.DesKgenEncryptor;
import i2f.core.jce.encrypt.symmetric.des.DesType;
import i2f.core.jce.encrypt.symmetric.des.ede.DesEdeKgenEncryptor;
import i2f.core.jce.encrypt.symmetric.des.ede.DesEdeType;

import java.security.KeyPair;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:22
 * @desc
 */
public class TestJce {
    public static void main(String[] args) throws Exception {
        String md5 = MessageDigestUtil.MD5.mdsText("hello".getBytes());
        String sha256 = MessageDigestUtil.SHA256.mdsText("hello".getBytes());
        System.out.println(md5);
        System.out.println(sha256);

        String hmacsha256 = MessageDigestUtil.hmac(ShaType.SHA256, "123456".getBytes()).mdsText("hello".getBytes());
        System.out.println(hmacsha256);

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
            KeyPair keyPair = RsaEncryptor.genKeyPair(RsaType.ECB_PKCS1PADDING, "123456".getBytes());

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

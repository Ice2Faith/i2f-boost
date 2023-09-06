package i2f.springboot.secure.preset;

import i2f.core.codec.CodecUtil;
import i2f.core.digest.CodeUtil;
import i2f.core.lang.functional.jvf.BiSupplier;
import i2f.core.security.jce.bc.BouncyCastleHolder;
import i2f.core.security.jce.bc.encrypt.asymmetric.rsa.BcRsaEncryptor;
import i2f.core.security.jce.bc.encrypt.asymmetric.rsa.BcRsaType;
import i2f.core.security.jce.digest.sha.ShaMessageDigester;
import i2f.core.security.jce.digest.sha.ShaType;
import i2f.core.security.jce.digest.std.IMessageDigester;
import i2f.core.security.jce.encrypt.CipherUtil;
import i2f.core.security.jce.encrypt.std.asymmetric.AsymmetricEncryptor;
import i2f.core.security.jce.encrypt.std.asymmetric.data.AsymKeyPair;
import i2f.core.security.jce.encrypt.std.symmetric.SymmetricEncryptor;
import i2f.core.security.jce.encrypt.symmetric.aes.AesEncryptor;
import i2f.core.security.jce.encrypt.symmetric.aes.AesType;
import i2f.core.security.jce.sm.digest.Sm3CryptoMessageDigester;
import i2f.core.security.jce.sm.encrypt.asymmetric.Sm2CryptoEncryptor;
import i2f.core.security.jce.sm.encrypt.asymmetric.Sm2CryptoUtils;
import i2f.core.security.jce.sm.encrypt.symmetric.Sm4CryptoEncryptor;

import java.security.KeyPair;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2023/6/29 9:46
 * @desc
 */
public class SecureProviderPresets {
    public static Supplier<AsymmetricEncryptor> asymmetricEncryptorSupplier_RSA = () -> new BcRsaEncryptor(BcRsaType.NONE_PKCS1PADDING);

    public static BiSupplier<AsymKeyPair, Integer> asymmetricKeyPairSupplier_RSA = (len) -> {
        try {
            BouncyCastleHolder.registry();
            KeyPair keyPair = CipherUtil.genKeyPair(BcRsaType.NONE_PKCS1PADDING, BouncyCastleHolder.PROVIDER_NAME, null, len, null);

            AsymmetricEncryptor encryptor = asymmetricEncryptorSupplier_RSA.get();
            return encryptor.encodeKeyPair(keyPair);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    };

    public static BiSupplier<SymmetricEncryptor, byte[]> symmetricEncryptorSupplier_AES = (secretBytes) -> new AesEncryptor(AesType.ECB_ISO10126PADDING, secretBytes);

    public static BiSupplier<byte[], Integer> symmetricKeySupplier_AES = (len) -> CodecUtil.toUtf8(CodeUtil.makeCheckCode(len));

    public static Supplier<IMessageDigester> messageDigesterSupplier_SHA256 = () -> new ShaMessageDigester(ShaType.SHA256);


    public static Supplier<AsymmetricEncryptor> asymmetricEncryptorSupplier_SM2 = () -> new Sm2CryptoEncryptor(Sm2CryptoUtils.MODE_C1C2C3);

    public static BiSupplier<AsymKeyPair, Integer> asymmetricKeyPairSupplier_SM2 = (len) -> {
        try {
            KeyPair keyPair = Sm2CryptoUtils.genKeyPair();

            AsymmetricEncryptor encryptor = asymmetricEncryptorSupplier_SM2.get();
            return encryptor.encodeKeyPair(keyPair);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    };

    public static BiSupplier<SymmetricEncryptor, byte[]> symmetricEncryptorSupplier_SM4 = (secretBytes) -> new Sm4CryptoEncryptor(secretBytes);

    public static BiSupplier<byte[], Integer> symmetricKeySupplier_SM4 = (len) -> CodecUtil.toUtf8(CodeUtil.makeCheckCode(len));

    public static Supplier<IMessageDigester> messageDigesterSupplier_SM3 = () -> new Sm3CryptoMessageDigester();

}

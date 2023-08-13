package i2f.springboot.secure.crypto;

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
import i2f.core.security.jce.encrypt.std.symmetric.SymmetricEncryptor;
import i2f.core.security.jce.encrypt.symmetric.aes.AesEncryptor;
import i2f.core.security.jce.encrypt.symmetric.aes.AesType;

import java.security.KeyPair;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2023/6/29 9:46
 * @desc
 */
public class SecureProvider {
    public static BiSupplier<AsymmetricEncryptor, KeyPair> asymmetricEncryptorSupplier = (keyPair) -> new BcRsaEncryptor(BcRsaType.NONE_PKCS1PADDING, keyPair);

    public static BiSupplier<SymmetricEncryptor, byte[]> symmetricEncryptorSupplier = (secretBytes) -> new AesEncryptor(AesType.ECB_ISO10126PADDING, secretBytes);

    public static Supplier<IMessageDigester> messageDigesterSupplier = () -> new ShaMessageDigester(ShaType.SHA256);

    public static BiSupplier<byte[], Integer> symmetricKeySupplier = (len) -> CodecUtil.toUtf8(CodeUtil.makeCheckCode(len));

    public static BiSupplier<KeyPair, Integer> asymmetricKeyPairSupplier = (len) -> {
        try {
            BouncyCastleHolder.registry();
            KeyPair keyPair = CipherUtil.genKeyPair(BcRsaType.NONE_PKCS1PADDING, BouncyCastleHolder.PROVIDER_NAME, null, len, null);
            return keyPair;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    };
}

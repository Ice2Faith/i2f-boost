package i2f.core.security.jce.encrypt.std.asymmetric;

import i2f.core.check.CheckUtil;
import i2f.core.codec.CodecUtil;
import i2f.core.codec.compress.IByteByteCodec;
import i2f.core.codec.exception.CodecException;
import i2f.core.container.array.ArrayUtil;
import i2f.core.security.jce.encrypt.std.asymmetric.data.AsymKeyPair;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:00
 * @desc 非对称加密中，一般采用公钥加密，私钥解密方式
 * 在不少的非对称加密算法中，都是这种模式
 * 只有少部分非对称加密算法，支持私钥加密，公钥解密的方式
 */
public interface AsymmetricEncryptor extends IByteByteCodec {
    byte[] publicEncrypt(byte[] data) throws Exception;

    byte[] privateDecrypt(byte[] data) throws Exception;

    void setKeyPair(KeyPair keyPair);

    default void setKeyPair(AsymKeyPair keyPair) {
        KeyPair pair = decodeKeyPair(keyPair);
        setKeyPair(pair);
    }

    default AsymKeyPair encodeKeyPair(KeyPair keyPair) {
        String publicKey = null;
        String privateKey = null;
        if (keyPair != null) {
            if (keyPair.getPublic() != null) {
                byte[] bts = keyPair.getPublic().getEncoded();
                privateKey = CodecUtil.toBase64(bts);
            }
            if (keyPair.getPrivate() != null) {
                byte[] bts = keyPair.getPrivate().getEncoded();
                privateKey = CodecUtil.toBase64(bts);
            }
        }
        return new AsymKeyPair(publicKey, privateKey);
    }

    default KeyPair decodeKeyPair(AsymKeyPair keyPair) {
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        if (keyPair != null) {
            if (!CheckUtil.isEmptyStr(keyPair.getPublicKey())) {
                byte[] bts = CodecUtil.ofBase64(keyPair.getPublicKey());
                try {
                    publicKey = getPublicKey(bts);
                } catch (Exception e) {

                }
            }
            if (!CheckUtil.isEmptyStr(keyPair.getPrivateKey())) {
                byte[] bts = CodecUtil.ofBase64(keyPair.getPrivateKey());
                try {
                    privateKey = getPrivateKey(bts);
                } catch (Exception e) {

                }
            }
        }

        return new KeyPair(publicKey, privateKey);
    }

    default PublicKey getPublicKey(byte[] codes) throws Exception {
        throw new UnsupportedOperationException("algorithm not support parse public key");
    }

    default PrivateKey getPrivateKey(byte[] codes) throws Exception {
        throw new UnsupportedOperationException("algorithm not support parse private key");
    }

    @Override
    default byte[] encode(byte[] data) {
        try {
            return publicEncrypt(data);
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    @Override
    default byte[] decode(byte[] enc) {
        try {
            return privateDecrypt(enc);
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    default byte[] publicDecrypt(byte[] data) throws Exception {
        throw new UnsupportedOperationException("algorithm not support public decrypt");
    }

    default byte[] privateEncrypt(byte[] data) throws Exception {
        throw new UnsupportedOperationException("algorithm not support private encrypt");
    }


    default byte[] makeSign(byte[] data) throws Exception {
        return privateEncrypt(data);
    }

    default boolean verifySign(byte[] sign, byte[] data) throws Exception {
        byte[] realData = publicDecrypt(sign);
        if (data == null || realData == null) {
            return false;
        }
        return ArrayUtil.compare(data, realData) == 0;
    }

    default String makeSignAsString(byte[] data) throws Exception {
        byte[] enc = makeSign(data);
        return CodecUtil.toBase64(enc);
    }

    default boolean verifySignByString(String hexSign, byte[] data) throws Exception {
        byte[] sign = CodecUtil.ofBase64(hexSign);
        return verifySign(sign, data);
    }

    default String publicEncryptAsBase64(byte[] data) throws Exception {
        byte[] enc = publicEncrypt(data);
        return CodecUtil.toBase64(enc);
    }

    default byte[] privateDecryptByBase64(String enc) throws Exception {
        byte[] data = CodecUtil.ofBase64(enc);
        return privateDecrypt(data);
    }

    default byte[] publicDecryptByBase64(String enc) throws Exception {
        byte[] data = CodecUtil.ofBase64(enc);
        return publicDecrypt(data);
    }

    default String privateEncryptAsBase64(byte[] data) throws Exception {
        byte[] enc = privateEncrypt(data);
        return CodecUtil.toBase64(enc);
    }
}

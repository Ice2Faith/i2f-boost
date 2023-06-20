package i2f.core.jce.encrypt.std.asymmetric;

import i2f.core.jce.codec.CodecUtil;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:00
 * @desc 非对称加密中，一般采用公钥加密，私钥解密方式
 * 在不少的非对称加密算法中，都是这种模式
 * 只有少部分非对称加密算法，支持私钥加密，公钥解密的方式
 */
public interface AsymmetricEncryptor {
    byte[] publicEncrypt(byte[] data) throws Exception;

    byte[] privateDecrypt(byte[] data) throws Exception;

    default byte[] publicDecrypt(byte[] data) throws Exception {
        throw new UnsupportedOperationException("algorithm not support public decrypt");
    }

    default byte[] privateEncrypt(byte[] data) throws Exception {
        throw new UnsupportedOperationException("algorithm not support private encrypt");
    }

    default String publicEncryptAsBase64(byte[] data) throws Exception {
        byte[] enc = publicEncrypt(data);
        return CodecUtil.toBase64(enc);
    }

    default byte[] privateDecryptByBase64(String enc) throws Exception {
        byte[] data = CodecUtil.parseBase64(enc);
        return privateDecrypt(data);
    }

    default byte[] publicDecryptByBase64(String enc) throws Exception {
        byte[] data = CodecUtil.parseBase64(enc);
        return publicDecrypt(data);
    }

    default String privateEncryptAsBase64(byte[] data) throws Exception {
        byte[] enc = privateEncrypt(data);
        return CodecUtil.toBase64(enc);
    }
}

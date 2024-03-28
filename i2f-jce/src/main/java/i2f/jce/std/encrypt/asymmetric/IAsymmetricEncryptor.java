package i2f.jce.std.encrypt.asymmetric;

import i2f.jce.std.encrypt.IEncryptor;
import i2f.jce.std.signature.ISignatureSigner;

/**
 * @author Ice2Faith
 * @date 2024/3/27 8:55
 * @desc
 */
public interface IAsymmetricEncryptor extends IEncryptor, ISignatureSigner {

    default byte[] privateEncrypt(byte[] data) throws Exception {
        throw new UnsupportedOperationException("unsupported asymmetric private encrypt mode.");
    }

    default byte[] publicDecrypt(byte[] data) throws Exception {
        throw new UnsupportedOperationException("unsupported asymmetric public decrypt mode.");
    }

    @Override
    default byte[] sign(byte[] data) throws Exception {
        return privateEncrypt(data);
    }
}

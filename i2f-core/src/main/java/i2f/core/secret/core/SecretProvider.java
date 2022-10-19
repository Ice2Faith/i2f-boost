package i2f.core.secret.core;

import i2f.core.secret.api.code.ICoder;
import i2f.core.secret.api.encrypt.IAsymmetricalEncryptor;
import i2f.core.secret.api.encrypt.ISymmetricalEncryptor;
import i2f.core.secret.api.hash.IHasher;
import i2f.core.secret.api.key.IKeyPair;
import i2f.core.secret.api.nonce.INoncer;
import i2f.core.secret.data.SecretKeyPair;
import i2f.core.secret.data.SecretMsg;
import i2f.core.secret.exception.SecretException;
import i2f.core.secret.util.SecretUtil;

/**
 * @author ltb
 * @date 2022/10/19 15:23
 * @desc
 */
public class SecretProvider {
    public IKeyPair mineKey;
    public ICoder coder;
    public INoncer noncer;
    public IHasher hasher;
    public ISymmetricalEncryptor symmetricalEncryptor;
    public IAsymmetricalEncryptor asymmetricalEncryptor;

    public SecretMsg send(byte[] data) throws Exception {
        SecretMsg ret = new SecretMsg();
        byte[] key = coder.code();
        ret.publicKey = mineKey.publicKey();
        ret.randomKey = asymmetricalEncryptor.encrypt(key, mineKey);
        ret.nonce = noncer.nonce();
        ret.msg = symmetricalEncryptor.encrypt(data, key);

        byte[] sign = hash(ret);
        ret.signature = asymmetricalEncryptor.encrypt(sign, mineKey);

        return ret;
    }

    public byte[] recv(SecretMsg msg) throws Exception {
        SecretKeyPair sendKeyPair = new SecretKeyPair(msg.publicKey);
        byte[] sendSign = null;
        try {
            sendSign = asymmetricalEncryptor.decrypt(msg.signature, sendKeyPair);
        } catch (Exception e) {
            throw new SecretException("数字签名验证失败");
        }
        if (sendSign == null || sendSign.length == 0) {
            throw new SecretException("数字签名验证失败");
        }
        byte[] recvSign = hash(msg);
        if (!SecretUtil.bytesCompare(sendSign, recvSign)) {
            throw new SecretException("消息签名验证失败");
        }
        if (!noncer.pass(msg.nonce)) {
            throw new SecretException("重放消息");
        }
        noncer.store(msg.nonce);
        byte[] key = null;
        try {
            key = asymmetricalEncryptor.decrypt(msg.randomKey, sendKeyPair);
        } catch (Exception e) {
            throw new SecretException("密文解析失败");
        }
        if (key == null) {
            throw new SecretException("密文解析失败");
        }
        byte[] data = null;
        try {
            data = symmetricalEncryptor.decrypt(msg.msg, key);
        } catch (Exception e) {
            throw new SecretException("密文解析失败");
        }
        if (data == null) {
            throw new SecretException("密文解析失败");
        }
        return data;
    }

    public byte[] hash(SecretMsg msg) throws Exception {
        byte[] sdata = SecretUtil.mergeByteArray(msg.msg, msg.nonce, msg.randomKey);
        byte[] sign = hasher.hash(sdata);
        return sign;
    }


}

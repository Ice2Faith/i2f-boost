package i2f.core.jce.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc 基本加解密器，使用KeyGenerator作为秘钥生成器，密码可以随机长度，内部其实是作为种子使用
 */
public class BasicKgenEncryptor implements IEncryptor, IChiperProvider {
    public static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";

    protected IEncryptType type;
    protected byte[] secretBytes;
    protected byte[] vectorBytes;

    public BasicKgenEncryptor(IEncryptType type, byte[] secretBytes) {
        this.type = type;
        this.secretBytes = secretBytes;
    }

    public BasicKgenEncryptor(IEncryptType type, byte[] secretBytes, byte[] vectorBytes) {
        this.type = type;
        this.secretBytes = secretBytes;
        this.vectorBytes = vectorBytes;
    }

    @Override
    public Cipher getEncryptCipher() throws Exception {
        return getCipher(true);
    }

    @Override
    public Cipher getDecryptCipher() throws Exception {
        return getCipher(false);
    }

    public Cipher getCipher(boolean encryptMode) throws Exception {
        int mode=encryptMode?Cipher.ENCRYPT_MODE:Cipher.DECRYPT_MODE;
        if(this.secretBytes==null){
            throw new UnsupportedOperationException("secret bytes length must be not null");
        }
        Cipher cipher = Cipher.getInstance(type.type());
        Key secretKey = genSecretBytes(this.secretBytes);
        if (type.requireVector()) {
            if(this.vectorBytes==null){
                throw new UnsupportedOperationException("vector bytes length must be not null");
            }
            IvParameterSpec iv=genVectorBytes(vectorBytes);
            cipher.init(mode,secretKey,iv);
        }else{
            cipher.init(mode,secretKey);
        }
        return cipher;
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher=getEncryptCipher();
        if (type.noPadding()) {
            data = CipherWorker.handleNoPaddingEncryptFormat(cipher, data);
        }
        return cipher.doFinal(data);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher=getDecryptCipher();
        if (type.noPadding()) {
            data = CipherWorker.handleNoPaddingEncryptFormat(cipher, data);
        }
        return cipher.doFinal(data);
    }

    public String getSecureRandomAlgorithm(){
        return SECURE_RANDOM_ALGORITHM;
    }

    /**
     * 获取加密的密匙，传入的slatKey可以是任意长度的，作为SecureRandom的随机种子，
     * 而在KeyGenerator初始化时设置密匙的长度128bit(16位byte)
     */
    public Key genSecretBytes(byte[] secretBytes) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(type.algorithmName());
        SecureRandom random = SecureRandom.getInstance(getSecureRandomAlgorithm());
        random.setSeed(secretBytes);
        kgen.init(type.secretBytesLen(), random);
        Key key = kgen.generateKey();
        return key;
    }

    /**
     * 获取加密的向量
     */
    public IvParameterSpec genVectorBytes(byte[] vectorBytes) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(type.algorithmName());
        SecureRandom random = SecureRandom.getInstance(getSecureRandomAlgorithm());
        random.setSeed(vectorBytes);
        kgen.init(type.vectorBytesLen(), random);
        IvParameterSpec iv = new IvParameterSpec(kgen.generateKey().getEncoded());
        return iv;
    }
}

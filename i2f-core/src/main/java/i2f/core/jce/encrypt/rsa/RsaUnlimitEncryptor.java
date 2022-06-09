package i2f.core.jce.encrypt.rsa;

import i2f.core.data.Pair;
import i2f.core.escape.Escapes;
import i2f.core.jce.encrypt.CipherWorker;
import i2f.core.jce.encrypt.IChiperProvider;
import i2f.core.jce.encrypt.IEncryptor;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc RSA加解密器
 * 每RSA_MAX_SIZE长度的内容分段加密，使用SEPARATOR_BYTE分段每一部分加密结果
 * 由于分段字节的存在，原始加密内容需要转义，使用转义符号ESCAPE_BYTE
 * 转义规则：
 * SEPARATOR_BYTE --> ESCAPE_BYTE BYTE_ESCAPE_SEPARATOR
 * ESCAPE_BYTE --> ESCAPE_BYTE BYTE_ESCAPE_SELF
 * 在这个实例中
 * 0x7e作为分隔符
 * 0x7d作为转义符
 * 转义规则为：
 * 0x7e -> 0x7d 0x02
 * 0x7d -> 0x7d 0x01
 */
public class RsaUnlimitEncryptor implements IEncryptor, IChiperProvider {
    public static final int  RSA_MAX_SIZE = 117;

    public static final byte SEPARATOR_BYTE=0x7e;
    public static final byte ESCAPE_BYTE=0x7d;
    public static final byte BYTE_ESCAPE_SELF=0x01;
    public static final byte BYTE_ESCAPE_SEPARATOR=0x02;


    protected RsaType type;
    protected byte[] secretBytes;
    protected byte[] vectorBytes;
    public static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    public RsaUnlimitEncryptor(byte[] secretBytes) {
        this.type = RsaType.ECB_PKCS1PADDING;
        this.secretBytes = secretBytes;
    }
    public RsaUnlimitEncryptor(RsaType type, byte[] secretBytes) {
        this.type = type;
        this.secretBytes = secretBytes;
    }

    public RsaUnlimitEncryptor(RsaType type, byte[] secretBytes, byte[] vectorBytes) {
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
        if(this.secretBytes==null ){
            throw new UnsupportedOperationException("secret bytes length must be not null");
        }
        Cipher cipher = Cipher.getInstance(type.type());
        if (encryptMode) {
            byte[] codes=getPublicKey(this.secretBytes);
            RSAPublicKey pubKey=getRsaPublicKey(codes);
            cipher.init(mode,pubKey);
        }else{
            byte[] codes=getPrivateKey(this.secretBytes);
            RSAPrivateKey priKey=getRsaPrivateKey(codes);
            cipher.init(mode,priKey);
        }
        return cipher;
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher=getEncryptCipher();
        if (type.noPadding()) {
            data = CipherWorker.handleNoPaddingEncryptFormat(cipher, data);
        }

        List<Pair<byte[],byte[]>> rule=new ArrayList<>();

        rule.add(new Pair<>(new byte[]{SEPARATOR_BYTE},new byte[]{ESCAPE_BYTE,BYTE_ESCAPE_SEPARATOR}));
        rule.add(new Pair<>(new byte[]{ESCAPE_BYTE},new byte[]{ESCAPE_BYTE,BYTE_ESCAPE_SELF}));

        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        int i = 0;
        boolean isFirst=true;
        while ((i + RSA_MAX_SIZE) < data.length) {
            byte[] buf = cipher.doFinal(data, i, RSA_MAX_SIZE);
            if(!isFirst){
                bos.write(SEPARATOR_BYTE);
            }
            bos.write(Escapes.byteEscape(buf,rule));
            isFirst=false;
            i += RSA_MAX_SIZE;
        }
        if (i < data.length) {
            bos.write(SEPARATOR_BYTE);
            byte[] buf = cipher.doFinal(data, i, data.length - i);
            bos.write(Escapes.byteEscape(buf,rule));
        }
        return bos.toByteArray();
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher=getDecryptCipher();
        if (type.noPadding()) {
            data = CipherWorker.handleNoPaddingEncryptFormat(cipher, data);
        }
        ByteArrayOutputStream rs=new ByteArrayOutputStream();
        List<Pair<byte[],byte[]>> rule=new ArrayList<>();

        rule.add(new Pair<>(new byte[]{ESCAPE_BYTE,BYTE_ESCAPE_SEPARATOR},new byte[]{SEPARATOR_BYTE}));
        rule.add(new Pair<>(new byte[]{ESCAPE_BYTE,BYTE_ESCAPE_SELF},new byte[]{ESCAPE_BYTE}));
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        int lastIdx=0;
        int i=0;
        while(i<data.length){
            if(data[i]==SEPARATOR_BYTE || i== data.length-1){
                if(i== data.length-1){
                    i= data.length;
                }
                bos.reset();
                bos.write(data,lastIdx,i-lastIdx);
                byte[] bytes = Escapes.byteEscape(bos.toByteArray(), rule);
                bytes=cipher.doFinal(bytes);
                rs.write(bytes);
                lastIdx=i+1;
            }
            i++;
        }

        return rs.toByteArray();
    }


    public RSAPublicKey getRsaPublicKey(byte[] codes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(type.algorithmName()).generatePublic(new X509EncodedKeySpec(codes));
        return pubKey;
    }

    public RSAPrivateKey getRsaPrivateKey(byte[] codes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(type.algorithmName()).generatePrivate(new PKCS8EncodedKeySpec(codes));
        return priKey;
    }


    /**
     * 根据slatKey获取公匙，传入的slatKey作为SecureRandom的随机种子
     * 若使用new SecureRandom()创建公匙，则需要记录下私匙，解密时使用
     */
    public byte[] getPublicKey(byte[] secretBytes) throws Exception {
        KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance(type.algorithmName());
        SecureRandom random = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
        random.setSeed(secretBytes);
        keyPairGenerator.initialize(type.secretBytesLen(), random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair.getPublic().getEncoded();
    }

    /**
     * 根据slatKey获取私匙，传入的slatKey作为SecureRandom的随机种子
     */
    public byte[] getPrivateKey(byte[] secretBytes) throws Exception {
        KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance(type.algorithmName());
        SecureRandom random = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
        random.setSeed(secretBytes);
        keyPairGenerator.initialize(type.secretBytesLen(), random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair.getPrivate().getEncoded();
    }
}

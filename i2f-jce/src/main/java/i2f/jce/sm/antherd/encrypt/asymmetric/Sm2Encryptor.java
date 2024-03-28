package i2f.jce.sm.antherd.encrypt.asymmetric;

import com.antherd.smcrypto.sm2.Keypair;
import com.antherd.smcrypto.sm2.Sm2;
import i2f.jce.std.encrypt.asymmetric.IAsymmetricEncryptor;
import i2f.jce.std.encrypt.asymmetric.key.BytesPrivateKey;
import i2f.jce.std.encrypt.asymmetric.key.BytesPublicKey;
import i2f.jce.std.util.ByteUtil;

import java.security.KeyPair;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/28 11:35
 * @desc
 */
public class Sm2Encryptor implements IAsymmetricEncryptor {

    protected String publicKey;
    protected String privateKey;

    public Sm2Encryptor() {
    }

    public Sm2Encryptor(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public Sm2Encryptor(Keypair keypair){
        this.publicKey=keypair.getPublicKey();
        this.privateKey=keypair.getPrivateKey();
    }

    public Sm2Encryptor(KeyPair keyPair){
        if(keyPair.getPublic()!=null){
            this.publicKey= ByteUtil.toHex(keyPair.getPublic().getEncoded());
        }
        if(keyPair.getPrivate()!=null){
            this.privateKey=ByteUtil.toHex(keyPair.getPrivate().getEncoded());
        }
    }

    public static Keypair genKey(){
        return Sm2.generateKeyPairHex();
    }

    public static KeyPair genKeyPair(){
        Keypair keypair = Sm2.generateKeyPairHex();

        return new KeyPair(new BytesPublicKey("sm2","hex",ByteUtil.ofHex(keypair.getPublicKey())),
                new BytesPrivateKey("sm2","hex",ByteUtil.ofHex(keypair.getPrivateKey())));
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        String str=new String(data,"UTF-8");
        return encrypt(str).getBytes("UTF-8");
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        String str=new String(data,"UTF-8");
        return decrypt(str).getBytes("UTF-8");
    }

    @Override
    public byte[] sign(byte[] data) throws Exception {
        String str=new String(data,"UTF-8");
        return sign(str).getBytes("UTF-8");
    }

    @Override
    public boolean verify(byte[] sign, byte[] data) throws Exception {
        String signHex=new String(sign,"UTF-8");
        String str=new String(data,"UTF-8");
        return verify(signHex,str);
    }

    public String encrypt(String data) throws Exception {
        return Sm2.doEncrypt(data, publicKey);
    }

    public String decrypt(String data) throws Exception {
        return Sm2.doDecrypt(data,privateKey);
    }

    public String sign(String data) throws Exception {
        return Sm2.doSignature(data, privateKey);
    }

    public boolean verify(String sign,String data) throws Exception {
        return Sm2.doVerifySignature(data,sign,publicKey);
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sm2Encryptor that = (Sm2Encryptor) o;
        return Objects.equals(publicKey, that.publicKey) &&
                Objects.equals(privateKey, that.privateKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicKey, privateKey);
    }

    @Override
    public String toString() {
        return "Sm2Encryptor{" +
                "publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                '}';
    }
}

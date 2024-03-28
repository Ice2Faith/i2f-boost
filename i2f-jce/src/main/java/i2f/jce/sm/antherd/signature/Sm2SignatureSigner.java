package i2f.jce.sm.antherd.signature;

import com.antherd.smcrypto.sm2.Keypair;
import com.antherd.smcrypto.sm2.Sm2;
import i2f.jce.sm.antherd.encrypt.asymmetric.Sm2Encryptor;
import i2f.jce.std.signature.ISignatureSigner;
import i2f.jce.std.util.ByteUtil;

import java.security.KeyPair;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/28 11:37
 * @desc
 */
public class Sm2SignatureSigner implements ISignatureSigner {
    protected String publicKey;
    protected String privateKey;

    public Sm2SignatureSigner() {
    }

    public Sm2SignatureSigner(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public Sm2SignatureSigner(Keypair keypair){
        this.publicKey=keypair.getPublicKey();
        this.privateKey=keypair.getPrivateKey();
    }

    public Sm2SignatureSigner(KeyPair keyPair){
        if(keyPair.getPublic()!=null){
            this.publicKey= ByteUtil.toHex(keyPair.getPublic().getEncoded());
        }
        if(keyPair.getPrivate()!=null){
            this.privateKey= ByteUtil.toHex(keyPair.getPrivate().getEncoded());
        }
    }

    public static Keypair genKey(){
        return Sm2Encryptor.genKey();
    }

    public static KeyPair genKeyPair(){
        return Sm2Encryptor.genKeyPair();
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
        Sm2SignatureSigner signer = (Sm2SignatureSigner) o;
        return Objects.equals(publicKey, signer.publicKey) &&
                Objects.equals(privateKey, signer.privateKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicKey, privateKey);
    }

    @Override
    public String toString() {
        return "Sm2SignatureSigner{" +
                "publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                '}';
    }
}

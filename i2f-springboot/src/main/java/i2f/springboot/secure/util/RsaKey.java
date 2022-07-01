package i2f.springboot.secure.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author ltb
 * @date 2022/6/28 15:12
 * @desc
 */
@Data
@NoArgsConstructor
public class RsaKey {
    private KeyPair keyPair;

    public RsaKey(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public RSAPublicKey publicKey(){
        return (RSAPublicKey)keyPair.getPublic();
    }

    public RSAPrivateKey privateKey(){
        return (RSAPrivateKey)keyPair.getPrivate();
    }

    public byte[] publicKeyBytes(){
        return publicKey().getEncoded();
    }

    public byte[] privateKeyBytes(){
        return privateKey().getEncoded();
    }

    public String publicKeyBase64(){
        return Base64Util.to(publicKeyBytes());
    }

    public String privateKeyBase64(){
        return Base64Util.to(privateKeyBytes());
    }

    public static RSAPublicKey parsePublicKeyBase64(String bs64) throws InvalidKeyException {
        byte[] data = Base64Util.from(bs64);
        return new RSAPublicKeyImpl(data);
    }

    public static RSAPrivateKey parsePrivateKeyBase64(String bs64) throws InvalidKeyException {
        byte[] data = Base64Util.from(bs64);
        return RSAPrivateCrtKeyImpl.newKey(data);
    }


    public static void saveRsaKey(RsaKey rsaKey,OutputStream os) throws IOException {
        String pubKey = rsaKey.publicKeyBase64();
        String priKey=rsaKey.privateKeyBase64();
        pubKey=Base64Obfuscator.encode(pubKey,true);
        priKey=Base64Obfuscator.encode(priKey,true);
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
        writer.write(pubKey);
        writer.newLine();
        writer.write(priKey);
        writer.newLine();
        writer.flush();
        os.flush();
    }

    public static RsaKey loadRsaKey(InputStream is) throws IOException, InvalidKeyException {
        BufferedReader reader=new BufferedReader(new InputStreamReader(is,"UTF-8"));
        String pubKey= reader.readLine();
        String priKey= reader.readLine();
        pubKey=Base64Obfuscator.decode(pubKey);
        priKey=Base64Obfuscator.decode(priKey);
        RSAPublicKey publicKey=parsePublicKeyBase64(pubKey);
        RSAPrivateKey privateKey=parsePrivateKeyBase64(priKey);
        KeyPair keyPair=new KeyPair(publicKey,privateKey);
        return new RsaKey(keyPair);
    }

    public static void saveRsaKey(RsaKey rsaKey,File file) throws IOException {
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos=new FileOutputStream(file);
        saveRsaKey(rsaKey,fos);
        fos.close();
    }

    public static RsaKey loadRsaKey(File file) throws IOException, InvalidKeyException {
        FileInputStream fis=new FileInputStream(file);
        RsaKey rsaKey=loadRsaKey(fis);
        fis.close();
        return rsaKey;
    }
}

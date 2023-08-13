package i2f.core.digest;

import i2f.core.check.CheckUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author ltb
 * @date 2022/6/28 15:12
 * @desc
 */
@Data
@NoArgsConstructor
public class AsymmetricKeyPair {
    private KeyPair keyPair;

    public AsymmetricKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public AsymmetricKeyPair(PublicKey publicKey, PrivateKey privateKey) {
        this(new KeyPair(publicKey, privateKey));
    }

    public static PublicKey parsePublicKeyBase64(String bs64) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (CheckUtil.isEmptyStr(bs64)) {
            return null;
        }
        byte[] data = Base64Util.decode(bs64);
        return getPublicKey(data);
    }

    public static PrivateKey parsePrivateKeyBase64(String bs64) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (CheckUtil.isEmptyStr(bs64)) {
            return null;
        }
        byte[] data = Base64Util.decode(bs64);
        return getPrivateKey(data);
    }

    public static PublicKey getPublicKey(byte[] codes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (codes == null || codes.length == 0) {
            return null;
        }
        PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(codes));
        return pubKey;
    }

    public static PrivateKey getPrivateKey(byte[] codes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (codes == null || codes.length == 0) {
            return null;
        }
        PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(codes));
        return priKey;
    }

    public PublicKey publicKey() {
        if (keyPair == null) {
            return null;
        }
        return keyPair.getPublic();
    }

    public PrivateKey privateKey() {
        if (keyPair == null) {
            return null;
        }
        return keyPair.getPrivate();
    }

    public byte[] publicKeyBytes() {
        PublicKey key = publicKey();
        if (key == null) {
            return new byte[0];
        }
        return key.getEncoded();
    }

    public byte[] privateKeyBytes() {
        PrivateKey key = privateKey();
        if (key == null) {
            return new byte[0];
        }
        return key.getEncoded();
    }

    public String publicKeyBase64() {
        byte[] bytes = publicKeyBytes();
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        return Base64Util.encode(bytes);
    }

    public String privateKeyBase64() {
        byte[] bytes = privateKeyBytes();
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        return Base64Util.encode(bytes);
    }

    public static void saveAsymKey(AsymmetricKeyPair rsaKey, OutputStream os) throws IOException {
        String pubKey = rsaKey.publicKeyBase64();
        String priKey = rsaKey.privateKeyBase64();
        pubKey = Base64Obfuscator.encode(pubKey, true);
        priKey = Base64Obfuscator.encode(priKey, true);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(pubKey);
        writer.newLine();
        writer.write(priKey);
        writer.newLine();
        writer.flush();
        os.flush();
    }

    public static AsymmetricKeyPair loadAsymKey(InputStream is) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String pubKey = reader.readLine();
        String priKey = reader.readLine();
        pubKey = Base64Obfuscator.decode(pubKey);
        priKey = Base64Obfuscator.decode(priKey);
        PublicKey publicKey = parsePublicKeyBase64(pubKey);
        PrivateKey privateKey = parsePrivateKeyBase64(priKey);
        KeyPair keyPair = new KeyPair(publicKey, privateKey);
        return new AsymmetricKeyPair(keyPair);
    }

    public static void saveAsymKey(AsymmetricKeyPair rsaKey, File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(file);
        saveAsymKey(rsaKey, fos);
        fos.close();
    }

    public static AsymmetricKeyPair loadAsymKey(File file) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        FileInputStream fis = new FileInputStream(file);
        AsymmetricKeyPair rsaKey = loadAsymKey(fis);
        fis.close();
        return rsaKey;
    }
}

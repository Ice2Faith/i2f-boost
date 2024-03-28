package i2f.jce.bc.encrypt.asymmetric;

import i2f.jce.bc.BcProvider;
import i2f.jce.jdk.encrypt.asymmetric.AsymmetricEncryptor;
import i2f.jce.jdk.supports.SecureRandomAlgorithm;
import i2f.jce.std.encrypt.asymmetric.IAsymmetricEncryptor;
import i2f.jce.std.encrypt.asymmetric.key.BytesPrivateKey;
import i2f.jce.std.encrypt.asymmetric.key.BytesPublicKey;
import i2f.jce.std.util.ByteUtil;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.Objects;


/**
 * @author Ice2Faith
 * @date 2024/3/28 16:17
 * @desc
 */
public class BcSm2Encryptor implements IAsymmetricEncryptor {
    static {
        BcProvider.registryProvider();
    }

    public static final String SM2_STD="sm2p256v1";
    public static final String SM2_PID="1234567812345678";

    protected String std=SM2_STD;
    protected String pid=SM2_PID;
    protected String secureRandomAlgorithmName= SecureRandomAlgorithm.SHA1PRNG.name();

    protected String publicKey;
    protected String privateKey;

    public BcSm2Encryptor() {
    }

    public BcSm2Encryptor(String std, String pid, String secureRandomAlgorithmName, String publicKey, String privateKey) {
        this.std = std;
        this.pid = pid;
        this.secureRandomAlgorithmName = secureRandomAlgorithmName;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }


    public BcSm2Encryptor(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public BcSm2Encryptor(KeyPair keyPair){
        if(keyPair.getPublic()!=null){
            this.publicKey= ByteUtil.toHex(keyPair.getPublic().getEncoded());
        }
        if(keyPair.getPrivate()!=null){
            this.privateKey=ByteUtil.toHex(keyPair.getPrivate().getEncoded());
        }
    }

    public static KeyPair genKeyPair() throws Exception {
        return genKeyPair(SM2_STD,SecureRandomAlgorithm.SHA1PRNG.text());
    }
    public static KeyPair genKeyPair(String sm2Std,String secureRandomAlgorithmName) throws Exception {
        X9ECParameters sm2EcParameters= GMNamedCurves.getByName(sm2Std);
        ECDomainParameters domainParameters=new ECDomainParameters(sm2EcParameters.getCurve(),
                sm2EcParameters.getG(),sm2EcParameters.getN());
        ECKeyPairGenerator generator=new ECKeyPairGenerator();
        if(secureRandomAlgorithmName==null || "".equalsIgnoreCase(secureRandomAlgorithmName)){
            secureRandomAlgorithmName="SHA1PRNG";
        }
        SecureRandom random=SecureRandom.getInstance(secureRandomAlgorithmName);
        generator.init(new ECKeyGenerationParameters(domainParameters,random));
        AsymmetricCipherKeyPair cipherKeyPair = generator.generateKeyPair();
        ECPrivateKeyParameters privateKeyParameters=(ECPrivateKeyParameters)cipherKeyPair.getPrivate();
        ECPublicKeyParameters publicKeyParameters=(ECPublicKeyParameters)cipherKeyPair.getPublic();
        byte[] privateKeyBytes = privateKeyParameters.getD().toByteArray();
        byte[] publicKeyBytes = publicKeyParameters.getQ().getEncoded(false);
        return new KeyPair(new BytesPublicKey(sm2Std,"hex",publicKeyBytes),
                new BytesPrivateKey(sm2Std,"hex",privateKeyBytes));
    }

    public SM2Engine getEncryptEngine() throws Exception {
        X9ECParameters sm2EcParameters= GMNamedCurves.getByName(std);
        ECDomainParameters domainParameters=new ECDomainParameters(sm2EcParameters.getCurve(),
                sm2EcParameters.getG(),sm2EcParameters.getN());
        ECPoint pubKeyPoint=sm2EcParameters.getCurve().decodePoint(Hex.decode(publicKey));
        ECPublicKeyParameters publicKeyParameters=new ECPublicKeyParameters(pubKeyPoint,domainParameters);
        SM2Engine engine=new SM2Engine();
        if(secureRandomAlgorithmName==null || "".equalsIgnoreCase(secureRandomAlgorithmName)){
            secureRandomAlgorithmName="SHA1PRNG";
        }
        SecureRandom random=SecureRandom.getInstance(secureRandomAlgorithmName);
        engine.init(true,new ParametersWithRandom(publicKeyParameters,random));
        return engine;
    }

    public SM2Engine getDecryptEngine() throws Exception {
        X9ECParameters sm2EcParameters= GMNamedCurves.getByName(std);
        ECDomainParameters domainParameters=new ECDomainParameters(sm2EcParameters.getCurve(),
                sm2EcParameters.getG(),sm2EcParameters.getN());
        BigInteger privateKeyD=new BigInteger(privateKey,16);
        ECPrivateKeyParameters privateKeyParameters=new ECPrivateKeyParameters(privateKeyD,domainParameters);
        SM2Engine engine=new SM2Engine();
        if(secureRandomAlgorithmName==null || "".equalsIgnoreCase(secureRandomAlgorithmName)){
            secureRandomAlgorithmName="SHA1PRNG";
        }
        SecureRandom random=SecureRandom.getInstance(secureRandomAlgorithmName);
        engine.init(false,privateKeyParameters);
        return engine;
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        SM2Engine engine = getEncryptEngine();
        return engine.processBlock(data, 0, data.length);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        SM2Engine engine=getDecryptEngine();
        return engine.processBlock(data,0,data.length);
    }

    public SM2Signer getSignSigner() throws Exception {
        X9ECParameters sm2EcParameters= GMNamedCurves.getByName(std);
        ECDomainParameters domainParameters=new ECDomainParameters(sm2EcParameters.getCurve(),
                sm2EcParameters.getG(),sm2EcParameters.getN());
        BigInteger privateKeyD=new BigInteger(privateKey,16);
        ECPrivateKeyParameters privateKeyParameters=new ECPrivateKeyParameters(privateKeyD,domainParameters);
        SM2Signer signer=new SM2Signer();
        if(secureRandomAlgorithmName==null || "".equalsIgnoreCase(secureRandomAlgorithmName)){
            secureRandomAlgorithmName="SHA1PRNG";
        }
        SecureRandom random=SecureRandom.getInstance(secureRandomAlgorithmName);
        signer.init(true,new ParametersWithID(new ParametersWithRandom(privateKeyParameters,random),pid.getBytes()));
        return signer;
    }

    public SM2Signer getVerifySigner() throws Exception {
        X9ECParameters sm2EcParameters= GMNamedCurves.getByName(std);
        ECDomainParameters domainParameters=new ECDomainParameters(sm2EcParameters.getCurve(),
                sm2EcParameters.getG(),sm2EcParameters.getN());
        ECPoint pubKeyPoint=sm2EcParameters.getCurve().decodePoint(Hex.decode(publicKey));
        ECPublicKeyParameters publicKeyParameters=new ECPublicKeyParameters(pubKeyPoint,domainParameters);
        SM2Signer signer=new SM2Signer();
        if(secureRandomAlgorithmName==null || "".equalsIgnoreCase(secureRandomAlgorithmName)){
            secureRandomAlgorithmName="SHA1PRNG";
        }
        signer.init(false,new ParametersWithID(publicKeyParameters,pid.getBytes()));
        return signer;
    }

    @Override
    public byte[] sign(byte[] data) throws Exception {
        SM2Signer signer = getSignSigner();
        signer.update(data,0,data.length);
        byte[] bytes = signer.generateSignature();

        return bytes;
    }

    @Override
    public boolean verify(byte[] sign, byte[] data) throws Exception {
        SM2Signer signer = getVerifySigner();
        signer.update(data,0,data.length);
        return signer.verifySignature(sign);
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSecureRandomAlgorithmName() {
        return secureRandomAlgorithmName;
    }

    public void setSecureRandomAlgorithmName(String secureRandomAlgorithmName) {
        this.secureRandomAlgorithmName = secureRandomAlgorithmName;
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
        BcSm2Encryptor that = (BcSm2Encryptor) o;
        return Objects.equals(std, that.std) &&
                Objects.equals(pid, that.pid) &&
                Objects.equals(secureRandomAlgorithmName, that.secureRandomAlgorithmName) &&
                Objects.equals(publicKey, that.publicKey) &&
                Objects.equals(privateKey, that.privateKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(std, pid, secureRandomAlgorithmName, publicKey, privateKey);
    }

    @Override
    public String toString() {
        return "BcSm2Encryptor{" +
                "std='" + std + '\'' +
                ", pid='" + pid + '\'' +
                ", secureRandomAlgorithmName='" + secureRandomAlgorithmName + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                '}';
    }
}

package i2f.core.jce.encrypt.des;

import i2f.core.jce.encrypt.BasicKgenEncryptor;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc DES-EDE加解密器
 */
public class DesEdeKgenEncryptor extends BasicKgenEncryptor {

    public DesEdeKgenEncryptor(byte[] secretBytes) {
        super(DesEdeType.ECB_PKCS5PADDING,secretBytes);
    }

    public DesEdeKgenEncryptor(DesEdeType type, byte[] secretBytes) {
        super(type,secretBytes);
    }

    public DesEdeKgenEncryptor(DesEdeType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type,secretBytes,vectorBytes);
    }

    @Override
    public IvParameterSpec genVectorBytes(byte[] vectorBytes) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(type.algorithmName());
        SecureRandom random = SecureRandom.getInstance(getSecureRandomAlgorithm());
        random.setSeed(vectorBytes);
        kgen.init(type.vectorBytesLen(), random);
        byte[] codes=kgen.generateKey().getEncoded();
        int len = 8;
        if(codes.length!=len){
            byte[] buf=new byte[len];
            for(int i=0;i<buf.length;i++){
                buf[i]=codes[i%codes.length];
            }
            codes=buf;
        }
        IvParameterSpec iv = new IvParameterSpec(codes);
        return iv;
    }
}

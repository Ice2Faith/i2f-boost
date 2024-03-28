package i2f.jce.sm.antherd.digest;

import com.antherd.smcrypto.sm3.Sm3;
import i2f.jce.std.digest.IMessageDigester;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2024/3/28 11:36
 * @desc
 */
public class Sm3Digester implements IMessageDigester {
    public static final Sm3Digester INSTANCE=new Sm3Digester();

    public Sm3Digester() {
    }

    @Override
    public byte[] digest(InputStream is) throws Exception {
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        byte[] buff=new byte[4096];
        int len=0;
        while((len=is.read(buff))>0){
            bos.write(buff,0,len);
        }
        is.close();
        String str = new String(bos.toByteArray(), "UTF-8");
        String digest = digest(str);
        return digest.getBytes("UTF-8");
    }

    @Override
    public byte[] digest(byte[] data) throws Exception {
        String str = new String(data, "UTF-8");
        String digest = digest(str);
        return digest.getBytes("UTF-8");
    }

    public String digest(String str){
        return Sm3.sm3(str);
    }

    public boolean verify(String sign,String str){
        String hash = Sm3.sm3(str);
        return hash.equalsIgnoreCase(sign);
    }

}

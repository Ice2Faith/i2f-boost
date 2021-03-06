package i2f.core.digest;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author ltb
 * @date 2021/9/2
 */
public class AESUtil {
    public static final String CHAR_SET="UTF-8";
    public static String genKey(String key){
        return genKey(key,32);
    }
    public static String genKey(String key, int minLen){
        if(key==null || "".equals(key)){
            return "A1B2C3D4E5F67870";
        }
        if(minLen<16){
            minLen=16;
        }
        int pi=0;
        while(pi<minLen){
            pi=pi+16;
        }
        minLen=pi;
        StringBuilder builder=new StringBuilder(key);
        int idx=0;
        int adlen=0;
        int klen=key.length();
        while((klen+adlen)<minLen){
            idx=((idx+3)*7)%(klen+adlen);
            builder.append(builder.charAt(idx));
            adlen++;
        }
        return builder.toString();
    }
    public static Cipher getAes(String key,int mode){
        try{
            byte[] keyRaw=key.getBytes(CHAR_SET);
            SecretKeySpec keySpec=new SecretKeySpec(keyRaw,"AES" );
            Cipher cipher=Cipher.getInstance("AES/ECB/ISO10126Padding");
            cipher.init(mode,keySpec);
            return cipher;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Cipher getAes(byte[] key,int mode){
        try{
            byte[] keyRaw=key;
            SecretKeySpec keySpec=new SecretKeySpec(keyRaw,"AES" );
            Cipher cipher=Cipher.getInstance("AES/ECB/ISO10126Padding");
            cipher.init(mode,keySpec);
            return cipher;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String encrypt(byte[] data,String key){
        try{
            Cipher cipher=getAes(key,Cipher.ENCRYPT_MODE);
            byte[] sdata=cipher.doFinal(data);
            return Base64Util.encode(sdata);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] decrypt(String data,String key){
        try{
            byte[] sdata=Base64Util.decode(data);
            Cipher cipher=getAes(key,Cipher.DECRYPT_MODE);
            return cipher.doFinal(sdata);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] decrypt(String data,byte[] key){
        try{
            byte[] sdata=Base64Util.decode(data);
            Cipher cipher=getAes(key,Cipher.DECRYPT_MODE);
            return cipher.doFinal(sdata);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String encryptJsonBytesAfterBase64(byte[] data,String key){
        try{
            String json=Base64Util.encode(data);
            return encrypt(json.getBytes(CHAR_SET),key);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String encryptJsonAfterBase64(String json, String key){
        try{
            json=Base64Util.encode(json.getBytes(CHAR_SET));
            return encrypt(json.getBytes(CHAR_SET),key);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptJsonBeforeBase64(String data, String key){
        try{
            byte[] sdata=decrypt(data, key);
            String json=new String(sdata,CHAR_SET);
            json=new String(Base64Util.decode(json),CHAR_SET);
            return json;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}

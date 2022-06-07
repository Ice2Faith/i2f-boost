package i2f.springboot.property.core;

/**
 * @author ltb
 * @date 2022/6/7 9:54
 * @desc
 */
public abstract class PrefixPropertyDecryptor implements IPropertyDecryptor {
    protected String prefix;
    public PrefixPropertyDecryptor(String prefix){
        this.prefix=prefix;
    }
    @Override
    public Object decrypt(Object obj, String name) {
        if(obj==null){
            return obj;
        }
        String text=String.valueOf(obj);
        if(text.startsWith(prefix)){
            text=text.substring(prefix.length());
            String value=decryptText(text);
            return value;
        }
        return obj;
    }

    public abstract String decryptText(String text);
}

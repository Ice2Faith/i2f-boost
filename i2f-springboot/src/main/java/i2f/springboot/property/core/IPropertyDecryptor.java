package i2f.springboot.property.core;

/**
 * @author ltb
 * @date 2022/6/7 9:46
 * @desc
 */
public interface IPropertyDecryptor {
    Object decrypt(Object obj,String name);
}

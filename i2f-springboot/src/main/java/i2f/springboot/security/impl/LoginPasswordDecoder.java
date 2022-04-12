package i2f.springboot.security.impl;

/**
 * @author ltb
 * @date 2022/4/8 17:49
 * @desc
 */
public interface LoginPasswordDecoder {
    String decode(String encodePassword);
}

package i2f.springboot.advice;

import i2f.core.annotations.remark.Remark;

/**
 * @author ltb
 * @date 2022/4/15 10:06
 * @desc
 */
@Remark("provide ResponseAdvice encrypt return object")
public interface IObjectEncryptor {
    String encrypt(Object obj);
}

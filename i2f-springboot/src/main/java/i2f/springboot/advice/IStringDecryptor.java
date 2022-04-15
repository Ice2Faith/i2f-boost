package i2f.springboot.advice;

import i2f.core.annotations.remark.Remark;

/**
 * @author ltb
 * @date 2022/4/15 10:08
 * @desc
 */
@Remark("provide decrypt RequestAdvice or Controller String type Param")
public interface IStringDecryptor {
    String decrypt(String text);
}

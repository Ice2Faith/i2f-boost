package i2f.core.digest;

import i2f.core.annotations.remark.Author;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author ltb
 * @date 2022/3/19 15:31
 * @desc
 */
@Author("i2f")
public class CodeUtil {

    public static String makeUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public static volatile SecureRandom rand = new SecureRandom();

    public static String makeCheckCode(int len) {
        return makeCheckCode(len, false);
    }

    public static String makeCheckCode(int len, boolean onlyNumber) {
        String ret = "";
        int bounce = 10 + 26 + 26;
        if (onlyNumber) {
            bounce = 10;
        }
        for (int i = 0; i < len; i++) {
            int val = rand.nextInt(bounce);
            if (val < 10) {
                ret += (char) (val + '0');
            } else if (val < (10 + 26)) {
                ret += (char) (val - 10 + 'a');
            } else {
                ret += (char) (val - 10 - 26 + 'A');
            }
        }
        return ret;
    }
}

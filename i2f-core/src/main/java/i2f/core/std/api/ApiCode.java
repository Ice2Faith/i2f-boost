package i2f.core.std.api;

import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;

/**
 * @author ltb
 * @date 2022/3/19 15:17
 * @desc
 */
@Author("i2f")
@Remark("defined some server response code")
public interface ApiCode {
    int SUCCESS=200;
    int ERROR=0;
    int NO_LOGIN=400;
    int NO_AUTH=401;
    int UNKNOWN=402;
    int NOT_FOUND=404;
    int SYS_EXCEPTION=500;
}

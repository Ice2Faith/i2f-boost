package i2f.springboot.secure.exception;

import lombok.Data;

/**
 * @author ltb
 * @date 2022/6/30 15:33
 * @desc
 */
@Data
public class SecureException extends RuntimeException{
    protected int code;
    protected String msg;

    public SecureException() {

    }

    public SecureException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public SecureException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public SecureException(int code,String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }
}

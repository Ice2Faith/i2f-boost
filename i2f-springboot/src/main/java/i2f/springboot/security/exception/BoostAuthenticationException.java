package i2f.springboot.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Ice2Faith
 * @date 2023/8/16 15:09
 * @desc
 */
public class BoostAuthenticationException extends AuthenticationException {
    public BoostAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public BoostAuthenticationException(String msg) {
        super(msg);
    }
}

package i2f.core.safe.token;

/**
 * @author ltb
 * @date 2022/5/25 8:44
 * @desc
 */
public interface ITokenGenerator {
    String token(Object ... args);
}

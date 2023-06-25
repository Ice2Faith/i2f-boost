package i2f.core.security.secret.api.store;

/**
 * @author ltb
 * @date 2022/10/19 17:07
 * @desc
 */
public interface ICacher {
    void set(byte[] key, byte[] value);

    void setExpire(byte[] key, byte[] value, long expireSecond);

    byte[] get(byte[] key);

    boolean exists(byte[] key);

    void remove(byte[] key);
}

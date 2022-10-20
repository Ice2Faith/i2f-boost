package i2f.core.secret.api.nonce;

/**
 * @author ltb
 * @date 2022/10/19 15:55
 * @desc
 */
public interface INoncer {
    byte[] nonce();
    boolean pass(byte[] nonce);
    void store(byte[] nonce);
}

package i2f.core.security.secret.api.key;

/**
 * @author ltb
 * @date 2022/10/19 15:37
 * @desc
 */
public interface IKeyPair {
    byte[] publicKey();
    byte[] privateKey();
}

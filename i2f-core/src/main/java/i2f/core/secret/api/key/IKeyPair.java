package i2f.core.secret.api.key;

/**
 * @author ltb
 * @date 2022/10/19 15:37
 * @desc
 */
public interface IKeyPair {
    byte[] publicKey();

    byte[] privateKey();
}

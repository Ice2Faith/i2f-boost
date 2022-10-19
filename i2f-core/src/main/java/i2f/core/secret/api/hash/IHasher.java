package i2f.core.secret.api.hash;

/**
 * @author ltb
 * @date 2022/10/19 15:22
 * @desc
 */
public interface IHasher {
    byte[] hash(byte[] data);
}

package i2f.core.jce.encrypt;

/**
 * @author ltb
 * @date 2022/6/8 10:49
 * @desc
 */
public interface IEncryptType {
    String type();
    String algorithmName();
    int secretBytesLen();
    int vectorBytesLen();
    boolean noPadding();
    boolean requireVector();
}

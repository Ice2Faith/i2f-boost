package i2f.core.codec;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:34
 * @desc
 */
public interface ICodecEx<E, D> {
    void encode(D data, E enc);

    void decode(E enc, D data);
}

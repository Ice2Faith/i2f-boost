package i2f.core.security.jce.codec.serialize;

import i2f.core.security.jce.codec.ICodec;

/**
 * @author Ice2Faith
 * @date 2023/6/27 17:10
 * @desc
 */
public interface ISerializer<E, D> extends ICodec<E, D> {
    E serialize(D data);

    D deserialize(E enc);

    @Override
    default E encode(D data) {
        return serialize(data);
    }

    @Override
    default D decode(E enc) {
        return deserialize(enc);
    }

}

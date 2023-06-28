package i2f.core.serialize.type;

import i2f.core.serialize.ISerializer;

/**
 * @author Ice2Faith
 * @date 2023/6/27 17:24
 * @desc
 */
public interface ITypeSerializer<E, D> extends ISerializer<E, D> {
    default D deserialize(E enc, Class<?> clazz) {
        return deserialize(enc);
    }

    default D deserialize(E enc, Object type) {
        return deserialize(enc);
    }
}

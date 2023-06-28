package i2f.core.codec.collection;

import i2f.core.codec.ICodec;

import java.util.Collection;

/**
 * @author Ice2Faith
 * @date 2023/6/27 16:31
 * @desc
 */
public interface IStringCollectionCodec<T, C extends Collection<T>> extends ICodec<String, C> {
}

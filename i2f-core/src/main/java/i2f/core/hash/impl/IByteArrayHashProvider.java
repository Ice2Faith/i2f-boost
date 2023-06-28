package i2f.core.hash.impl;

import i2f.core.hash.IHashProvider;
import i2f.core.serialize.bytes.IBytesObjectSerializer;

/**
 * @author ltb
 * @date 2022/4/27 15:09
 * @desc
 */
public abstract class IByteArrayHashProvider<T> implements IHashProvider<T> {
    protected IBytesObjectSerializer serializer;

    public IByteArrayHashProvider(IBytesObjectSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public long hash(T obj) {
        byte[] data= serializer.serialize(obj);
        return hashBytes(data);
    }

    public abstract long hashBytes(byte[] data);
}

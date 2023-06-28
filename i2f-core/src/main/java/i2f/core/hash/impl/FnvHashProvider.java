package i2f.core.hash.impl;

import i2f.core.serialize.bytes.IBytesObjectSerializer;

/**
 * @author ltb
 * @date 2022/4/27 15:05
 * @desc
 */
public class FnvHashProvider<T> extends IByteArrayHashProvider<T> {
    public FnvHashProvider(IBytesObjectSerializer serializer) {
        super(serializer);
    }

    @Override
    public long hashBytes(byte[] data) {
        long fnv_prime = 0x811C9DC5;
        long hash      = 0;

        for(int i = 0; i < data.length;  i++)
        {
            hash *= fnv_prime;
            hash ^= (data[i]);
        }

        return hash;
    }
}

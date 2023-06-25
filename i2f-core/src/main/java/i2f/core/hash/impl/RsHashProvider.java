package i2f.core.hash.impl;

import i2f.core.serialize.std.IBytesSerializer;

/**
 * @author ltb
 * @date 2022/4/27 15:05
 * @desc
 */
public class RsHashProvider<T> extends IByteArrayHashProvider<T> {
    public RsHashProvider(IBytesSerializer serializer) {
        super(serializer);
    }

    @Override
    public long hashBytes(byte[] data) {
        long b    = 378551;
        long a    = 63689;
        long hash = 0;

        for(int i = 0; i < data.length; i++)
        {
            hash = hash * a + (data[i]);
            a    = a * b;
        }

        return hash;
    }
}

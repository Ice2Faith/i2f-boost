package i2f.core.hash.impl;

import i2f.core.serialize.IBytesSerializer;

/**
 * @author ltb
 * @date 2022/4/27 15:05
 * @desc
 */
public class BpHashProvider<T> extends IByteArrayHashProvider<T> {
    public BpHashProvider(IBytesSerializer serializer) {
        super(serializer);
    }

    @Override
    public long hashBytes(byte[] data) {
        long hash = 0;
        for(int i = 0; i < data.length;  i++)
        {
            hash = hash << 7 ^ (data[i]);
        }

        return hash;
    }
}

package i2f.core.hash.impl;

import i2f.core.serialize.ISerializer;

/**
 * @author ltb
 * @date 2022/4/27 15:05
 * @desc
 */
public class DekHashProvider<T> extends IByteArrayHashProvider<T> {
    public DekHashProvider(ISerializer serializer) {
        super(serializer);
    }

    @Override
    public long hashBytes(byte[] data) {
        long hash = data.length;

        for(int i = 0; i < data.length; i++)
        {
            hash = ((hash << 5) ^ (hash >>> 27)) ^ (data[i]);
        }
        return hash;
    }
}

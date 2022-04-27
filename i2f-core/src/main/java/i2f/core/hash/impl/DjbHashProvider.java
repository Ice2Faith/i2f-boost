package i2f.core.hash.impl;

import i2f.core.serialize.ISerializer;

/**
 * @author ltb
 * @date 2022/4/27 15:05
 * @desc
 */
public class DjbHashProvider<T> extends IByteArrayHashProvider<T> {
    public DjbHashProvider(ISerializer serializer) {
        super(serializer);
    }

    @Override
    public long hashBytes(byte[] data) {
        long hash = 5381;

        for(int i = 0; i < data.length; i++)
        {
            hash = ((hash << 5) + hash) + (data[i]);
        }

        return hash;
    }
}

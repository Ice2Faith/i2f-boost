package i2f.core.hash.impl;

import i2f.core.serialize.ISerializer;

/**
 * @author ltb
 * @date 2022/4/27 15:05
 * @desc
 */
public class JsHashProvider<T> extends IByteArrayHashProvider<T> {
    public JsHashProvider(ISerializer serializer) {
        super(serializer);
    }

    @Override
    public long hashBytes(byte[] data) {
        long hash = 1315423911;

        for(int i = 0; i < data.length;  i++)
        {
            hash ^= ((hash << 5) + (data[i]) + (hash >>> 2));
        }

        return hash;
    }
}

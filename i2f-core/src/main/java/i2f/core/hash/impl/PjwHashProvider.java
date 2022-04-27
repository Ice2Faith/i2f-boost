package i2f.core.hash.impl;

import i2f.core.serialize.ISerializer;

/**
 * @author ltb
 * @date 2022/4/27 15:05
 * @desc
 */
public class PjwHashProvider<T> extends IByteArrayHashProvider<T> {
    public PjwHashProvider(ISerializer serializer) {
        super(serializer);
    }

    @Override
    public long hashBytes(byte[] data) {
        long bitsInUnsignedInt = (long)(4 * 8); // 4 is sizeof(unsigned int)
        long threeQuarters = (long)((bitsInUnsignedInt * 3) / 4);
        long oneEighth = (long)(bitsInUnsignedInt / 8);
        long highBits = (long)(0xFFFFFFFF) << (bitsInUnsignedInt - oneEighth);
        long hash = 0;
        long test = 0;

        for (int i = 0; i < data.length; i++) {
            hash = (hash << oneEighth) + (data[i]);

            if ((test = hash & highBits) != 0) {
                hash = ((hash ^ (test >>> threeQuarters)) & (~highBits));
            }
        }

        return hash;
    }
}

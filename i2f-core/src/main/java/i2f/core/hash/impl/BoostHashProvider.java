package i2f.core.hash.impl;

import i2f.core.serialize.bytes.IBytesObjectSerializer;

/**
 * @author ltb
 * @date 2022/4/27 15:05
 * @desc
 */
public class BoostHashProvider<T> extends IByteArrayHashProvider<T> {
    public BoostHashProvider(IBytesObjectSerializer serializer) {
        super(serializer);
    }

    @Override
    public long hashBytes(byte[] data) {
        long hash = 0x7111317;
        long[] perms={11,13,17,19,23,27,29,31};
        long fac=0x1719;

        for(int i=0;i<data.length;i++){
            int idx=data[i];
            if(idx<0){
                idx=0-idx;
            }
            idx=idx% perms.length;

            hash ^=fac;
            hash ^=data[i]*perms[idx];
            hash = (hash << (fac%5)) | (hash*7/3);

            fac=(fac+i)^(perms[i% perms.length]);
            fac+=perms[idx];
            fac=fac<<(perms[idx]%8);
            fac+=i;
        }

        hash^=data.length;
        return hash;
    }

}

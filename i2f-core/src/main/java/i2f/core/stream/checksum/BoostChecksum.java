package i2f.core.stream.checksum;

import java.util.zip.Checksum;

/**
 * @author ltb
 * @date 2022/5/20 9:54
 * @desc
 */
public class BoostChecksum implements Checksum {
    private long sign=177;
    private long idx=0;
    @Override
    public void update(int b) {
        sign = Math.abs(sign * 17 + ((idx * b) * 27) % 2077);
        idx++;
    }

    @Override
    public void update(byte[] b, int off, int len) {
        for(int i=0;i<len;i++){
            update(b[off+i]);
        }
    }

    @Override
    public long getValue() {
        return sign;
    }

    @Override
    public void reset() {
        sign=177;
        idx=0;
    }
}

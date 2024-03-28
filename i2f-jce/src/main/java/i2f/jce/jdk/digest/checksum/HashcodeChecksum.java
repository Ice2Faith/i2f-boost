package i2f.jce.jdk.digest.checksum;


import java.util.zip.Checksum;

/**
 * @author Ice2Faith
 * @date 2024/3/27 10:50
 * @desc
 */
public class HashcodeChecksum implements Checksum {
    protected long num = 0;

    @Override
    public void update(int b) {
        num = 31 * num + b;
    }

    @Override
    public void update(byte[] b, int off, int len) {
        for (int i = off; i < off + len; i++) {
            update(b[i]);
        }
    }

    @Override
    public long getValue() {
        return num;
    }

    @Override
    public void reset() {
        num = 0;
    }
}

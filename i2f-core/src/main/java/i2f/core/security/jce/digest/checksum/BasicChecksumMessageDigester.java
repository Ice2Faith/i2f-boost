package i2f.core.security.jce.digest.checksum;

import i2f.core.bit.BytesUtil;
import i2f.core.io.stream.StreamUtil;
import i2f.core.io.stream.impl.BlackHoleOutputStream;
import i2f.core.security.jce.digest.std.IMessageDigester;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

/**
 * @author Ice2Faith
 * @date 2023/6/26 10:51
 * @desc
 */
public class BasicChecksumMessageDigester implements IMessageDigester {
    protected Checksum checksum;

    public BasicChecksumMessageDigester(Checksum checksum) {
        this.checksum = checksum;
    }

    @Override
    public byte[] mds(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        return this.mds(bis);
    }

    @Override
    public byte[] mds(InputStream is) {
        CheckedInputStream cis = new CheckedInputStream(is, checksum);
        BlackHoleOutputStream bos = new BlackHoleOutputStream();
        try {
            StreamUtil.streamCopy(cis, bos, true, true);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        Checksum sum = cis.getChecksum();
        long value = sum.getValue();
        return BytesUtil.toBytes(value);
    }
}

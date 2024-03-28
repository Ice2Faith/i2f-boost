package i2f.jce.jdk.digest.checksum;

import i2f.jce.std.digest.IMessageDigester;
import i2f.jce.std.util.ByteUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * @author Ice2Faith
 * @date 2024/3/27 9:00
 * @desc
 */
public class ChecksumMessageDigester implements IMessageDigester {

    public static final ChecksumMessageDigester ADLER32 = new ChecksumMessageDigester(new Adler32());
    public static final ChecksumMessageDigester CRC32 = new ChecksumMessageDigester(new CRC32());
    public static final ChecksumMessageDigester HASHCODE = new ChecksumMessageDigester(new HashcodeChecksum());

    protected Checksum provider;

    public ChecksumMessageDigester() {
    }

    public ChecksumMessageDigester(Checksum provider) {
        this.provider = provider;
    }


    @Override
    public byte[] digest(InputStream is) throws Exception {
        return getChecksum(is, provider);
    }

    public static byte[] getChecksum(InputStream is, Checksum md) throws IOException {
        md.reset();
        byte[] buf = new byte[16];
        int len = 0;
        md.reset();
        while ((len = is.read(buf)) > 0) {
            md.update(buf, 0, len);
        }
        is.close();
        long num = md.getValue();
        return ByteUtil.toBytes(num);
    }

    public Checksum getProvider() {
        return provider;
    }

    public void setProvider(Checksum provider) {
        this.provider = provider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChecksumMessageDigester that = (ChecksumMessageDigester) o;
        return Objects.equals(provider, that.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider);
    }

    @Override
    public String toString() {
        return "ChecksumMessageDigester{" +
                "provider=" + provider +
                '}';
    }
}

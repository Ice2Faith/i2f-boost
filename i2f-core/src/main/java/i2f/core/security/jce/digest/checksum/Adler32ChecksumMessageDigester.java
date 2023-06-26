package i2f.core.security.jce.digest.checksum;

import java.util.zip.Adler32;

/**
 * @author Ice2Faith
 * @date 2023/6/26 11:12
 * @desc
 */
public class Adler32ChecksumMessageDigester extends BasicChecksumMessageDigester {
    public Adler32ChecksumMessageDigester() {
        super(new Adler32());
    }
}

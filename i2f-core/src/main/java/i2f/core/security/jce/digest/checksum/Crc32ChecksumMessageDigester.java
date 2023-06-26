package i2f.core.security.jce.digest.checksum;

import java.util.zip.CRC32;

/**
 * @author Ice2Faith
 * @date 2023/6/26 11:12
 * @desc
 */
public class Crc32ChecksumMessageDigester extends BasicChecksumMessageDigester {
    public Crc32ChecksumMessageDigester() {
        super(new CRC32());
    }
}

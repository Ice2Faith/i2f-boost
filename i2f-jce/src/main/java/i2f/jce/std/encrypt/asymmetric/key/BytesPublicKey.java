package i2f.jce.std.encrypt.asymmetric.key;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/27 9:15
 * @desc
 */
public class BytesPublicKey implements PublicKey {
    protected String algorithm = "unknown";
    protected String format = "bytes";
    protected byte[] data;

    public BytesPublicKey() {
    }

    public BytesPublicKey(String algorithm, String format, byte[] data) {
        this.algorithm = algorithm;
        this.format = format;
        this.data = data;
    }

    public BytesPublicKey(byte[] data) {
        this.data = data;
    }

    public BytesPublicKey(PublicKey key) {
        this(key.getAlgorithm(), key.getFormat(), key.getEncoded());
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public byte[] getEncoded() {
        return data;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BytesPublicKey that = (BytesPublicKey) o;
        return Objects.equals(algorithm, that.algorithm) &&
                Objects.equals(format, that.format) &&
                Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(algorithm, format);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "BytesPublicKey{" +
                "algorithm='" + algorithm + '\'' +
                ", format='" + format + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}

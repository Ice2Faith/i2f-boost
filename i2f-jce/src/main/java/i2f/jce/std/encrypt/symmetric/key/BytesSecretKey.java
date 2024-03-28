package i2f.jce.std.encrypt.symmetric.key;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/28 13:58
 * @desc
 */
public class BytesSecretKey implements SecretKey {
    protected String algorithm = "unknown";
    protected String format = "bytes";
    protected byte[] data;


    public BytesSecretKey() {
    }

    public BytesSecretKey(String algorithm, String format, byte[] data) {
        this.algorithm = algorithm;
        this.format = format;
        this.data = data;
    }

    public BytesSecretKey(byte[] data) {
        this.data = data;
    }

    public BytesSecretKey(Key key) {
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
        BytesSecretKey bytesKey = (BytesSecretKey) o;
        return Objects.equals(algorithm, bytesKey.algorithm) &&
                Objects.equals(format, bytesKey.format) &&
                Arrays.equals(data, bytesKey.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(algorithm, format);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "BytesSecretKey{" +
                "algorithm='" + algorithm + '\'' +
                ", format='" + format + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}

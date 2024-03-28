package i2f.jce.jdk.digest.md;

import i2f.jce.jdk.supports.MessageDigestAlgorithm;
import i2f.jce.std.digest.IMessageDigester;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/27 9:00
 * @desc
 */
public class MessageDigester implements IMessageDigester {

    public static final MessageDigester MD2 = new MessageDigester(messageDigestOf(MessageDigestAlgorithm.MD2.text(), null));
    public static final MessageDigester MD5 = new MessageDigester(messageDigestOf(MessageDigestAlgorithm.MD5.text(), null));

    public static final MessageDigester SHA_1 = new MessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA_1.text(), null));
    public static final MessageDigester SHA_224 = new MessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA_224.text(), null));
    public static final MessageDigester SHA_256 = new MessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA_256.text(), null));
    public static final MessageDigester SHA_384 = new MessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA_384.text(), null));
    public static final MessageDigester SHA_512 = new MessageDigester(messageDigestOf(MessageDigestAlgorithm.SHA_512.text(), null));

    protected MessageDigest provider;

    public MessageDigester() {
    }

    public MessageDigester(MessageDigest provider) {
        this.provider = provider;
    }

    @Override
    public byte[] digest(InputStream is) throws Exception {
        return getMds(is, provider);
    }

    public static MessageDigest messageDigestOf(String type, String providerName) {
        try {
            if ("".equals(providerName)) {
                providerName = null;
            }
            if (providerName != null) {
                Provider provider = Security.getProvider(providerName);
                if (provider == null) {
                    providerName = null;
                }
            }
            MessageDigest md = null;

            if (providerName != null) {
                md = MessageDigest.getInstance(type, providerName);
            } else {
                md = MessageDigest.getInstance(type);
            }
            return md;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static byte[] getMds(InputStream is, MessageDigest md) throws IOException {
        byte[] buf = new byte[16];
        int len = 0;
        md.reset();
        while ((len = is.read(buf)) > 0) {
            md.update(buf, 0, len);
        }
        is.close();
        return md.digest();
    }

    public MessageDigest getProvider() {
        return provider;
    }

    public void setProvider(MessageDigest provider) {
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
        MessageDigester that = (MessageDigester) o;
        return Objects.equals(provider, that.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider);
    }

    @Override
    public String toString() {
        return "MessageDigester{" +
                "provider=" + provider +
                '}';
    }
}

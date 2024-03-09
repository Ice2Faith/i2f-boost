package i2f.packet.stream;

import i2f.packet.rule.StreamPacketResolver;
import i2f.packet.data.StreamPacket;
import i2f.packet.rule.PacketRule;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/9 17:03
 * @desc
 */
public class InputStreamPacketReader<T> implements Closeable {
    protected static final PacketRule<Long> DEFAULT_RULE = PacketRule.defaultHashRule();

    protected InputStream is;
    protected PacketRule<T> rule;

    public static InputStreamPacketReader<Long> form(InputStream is){
        return new InputStreamPacketReader<>(is,DEFAULT_RULE);
    }

    public InputStreamPacketReader(InputStream is, PacketRule<T> rule) {
        this.is = is;
        this.rule = rule;
    }

    public StreamPacket read() throws IOException {
        return StreamPacketResolver.read(rule, is);
    }

    @Override
    public void close() throws IOException {
        if(is!=null){
            is.close();
        }
    }

    public InputStream getIs() {
        return is;
    }

    public PacketRule<T> getRule() {
        return rule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InputStreamPacketReader<?> that = (InputStreamPacketReader<?>) o;
        return Objects.equals(is, that.is) &&
                Objects.equals(rule, that.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(is, rule);
    }

    @Override
    public String toString() {
        return "InputStreamPacketReader{" +
                "is=" + is +
                ", rule=" + rule +
                '}';
    }
}

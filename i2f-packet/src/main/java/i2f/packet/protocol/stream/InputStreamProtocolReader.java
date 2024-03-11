package i2f.packet.protocol.stream;

import i2f.packet.data.StreamPacket;
import i2f.packet.protocol.PacketProtocol;
import i2f.packet.rule.PacketRule;
import i2f.packet.rule.StreamPacketResolver;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/9 17:03
 * @desc
 */
public class InputStreamProtocolReader<T> implements Closeable {
    protected static final PacketRule<Long> DEFAULT_RULE = PacketRule.defaultRule();

    protected InputStream is;
    protected PacketRule<T> rule;

    public static InputStreamProtocolReader<Long> form(InputStream is){
        return new InputStreamProtocolReader<>(is,DEFAULT_RULE);
    }

    public InputStreamProtocolReader(InputStream is, PacketRule<T> rule) {
        this.is = is;
        this.rule = rule;
    }

    public PacketProtocol read() throws IOException {
        StreamPacket pack = StreamPacketResolver.read(rule, is);
        return PacketProtocol.ofPacket(pack);
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
        InputStreamProtocolReader<?> that = (InputStreamProtocolReader<?>) o;
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

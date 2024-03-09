package i2f.packet.stream;

import i2f.packet.rule.StreamPacketResolver;
import i2f.packet.data.StreamPacket;
import i2f.packet.rule.PacketRule;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/9 17:07
 * @desc
 */
public class OutputStreamPacketWriter<T> implements Closeable {
    protected static final PacketRule<Long> DEFAULT_RULE = PacketRule.defaultHashRule();

    protected OutputStream os;
    protected PacketRule<T> rule;

    public static OutputStreamPacketWriter<Long> form(OutputStream os){
        return new OutputStreamPacketWriter<>(os,DEFAULT_RULE);
    }

    public OutputStreamPacketWriter(OutputStream os, PacketRule<T> rule) {
        this.os = os;
        this.rule = rule;
    }

    public void write(StreamPacket packet) throws IOException {
        StreamPacketResolver.write(rule,packet,os);
    }

    public void flush() throws IOException {
        os.flush();
    }

    @Override
    public void close() throws IOException {
        if(os!=null){
            os.close();
        }
    }

    public OutputStream getOs() {
        return os;
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
        OutputStreamPacketWriter<?> that = (OutputStreamPacketWriter<?>) o;
        return Objects.equals(os, that.os) &&
                Objects.equals(rule, that.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(os, rule);
    }

    @Override
    public String toString() {
        return "OutputStreamPacketWriter{" +
                "os=" + os +
                ", rule=" + rule +
                '}';
    }
}

package i2f.packet;

import i2f.packet.data.StreamPacket;
import i2f.packet.io.LocalOutputStreamInputAdapter;
import i2f.packet.rule.PacketRule;

import java.io.*;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Ice2Faith
 * @date 2024/3/8 8:49
 * @desc
 */
public class StreamPacketResolver {
    private static final PacketRule<Long> DEFAULT_RULE = PacketRule.defaultHashRule();

    private static class ReadPacketContext {
        public byte before = 0;
        public boolean hasBefore = false;
        public boolean isFirst = true;
        public boolean isOk = true;
    }

    public static <T> StreamPacket read(InputStream is) throws IOException {
        return read(DEFAULT_RULE, is);
    }

    public static <T> StreamPacket read(PacketRule<T> rule, InputStream is) throws IOException {
        StreamPacket ret = null;
        ReadPacketContext context = new ReadPacketContext();
        context.before = 0;
        context.hasBefore = false;
        context.isFirst = true;
        context.isOk = true;
        int bt = 0;
        // 读取开头
        LocalOutputStreamInputAdapter drop = new LocalOutputStreamInputAdapter();
        context.isOk = false;
        context.hasBefore = false;
        context.isFirst = true;
        while ((bt = is.read()) >= 0) {
            byte b = (byte) bt;
            drop.write(b);
            if (context.isFirst) {
                context.before = b;
                context.hasBefore = true;
                context.isFirst = false;
                continue;
            }
            if (b == rule.getStart()) {
                if (context.before == rule.getStart()) {
                    ret = new StreamPacket();
                    context.hasBefore = true;
                    context.before = b;
                    context.isOk = true;
                    break;
                }
            }
            context.hasBefore = true;
            context.before = b;
        }

        if (!context.isOk) {
            return null;
        }

        AtomicReference<T> ref = null;
        if (rule.isTailSupport()) {
            ref = new AtomicReference<>();
            T acc = rule.getTailInitializer().get();
            ref.set(acc);
        }
        boolean isTailSupport = (rule.isTailSupport() && ref != null);

        // 读取头
        LocalOutputStreamInputAdapter head = new LocalOutputStreamInputAdapter();
        readNextItem(rule, ref, rule.getSeparator(), is, context, head);
        head.close();
        ret.setHead(head.toByteArray());

        // 读取体数量
        int len = is.read();

        // 读取体
        InputStream[] body = new InputStream[len];
        LocalOutputStreamInputAdapter[] outs = new LocalOutputStreamInputAdapter[len];
        for (int i = 0; i < len; i++) {
            outs[i] = new LocalOutputStreamInputAdapter();
            readNextItem(rule, ref, rule.getSeparator(), is, context, outs[i]);
            outs[i].close();
            body[i] = outs[i].getInputStream();
        }
        ret.setBody(body);

        // 读取尾
        LocalOutputStreamInputAdapter tail = new LocalOutputStreamInputAdapter();
        readNextItem(rule, null, rule.getEnd(), is, context, tail);
        tail.close();
        ret.setTail(tail.toByteArray());

        if (isTailSupport) {
            byte[] ruleTail = rule.getTailFinisher().apply(ref.get());
            ret.setRuleTail(ruleTail);
        }

        return ret;
    }

    private static <T> void readNextItem(PacketRule<T> rule, AtomicReference<T> ref, byte end, InputStream is, ReadPacketContext context, OutputStream os) throws IOException {
        boolean isTailSupport = (rule.isTailSupport() && ref != null);
        T acc = null;
        if (isTailSupport) {
            acc = ref.get();
        }

        int bt = 0;
        context.isOk = false;
        context.hasBefore = false;
        context.isFirst = true;
        while ((bt = is.read()) >= 0) {
            byte b = (byte) bt;
            if (context.isFirst) {
                if (b == end) {
                    context.hasBefore = true;
                    context.before = b;
                    context.isOk = true;
                    break;
                }
                context.hasBefore = true;
                context.before = b;
                context.isFirst = false;
                continue;
            }
            if (b == end) {
                if (context.before != rule.getEscape()) {
                    if (context.hasBefore) {
                        if (isTailSupport) {
                            acc = rule.getTailAccumulator().apply(acc, context.before);
                        }
                        os.write(context.before);
                    }
                    context.hasBefore = true;
                    context.before = b;
                    context.isOk = true;
                    break;
                }
            }

            if (context.hasBefore) {
                if (context.before == rule.getEscape()) {
                    if (b == rule.getStart()
                            || b == rule.getEscape()
                            || b == rule.getSeparator()
                            || b == rule.getEnd()
                            || b == end) {
                        if (isTailSupport) {
                            acc = rule.getTailAccumulator().apply(acc, b);
                        }
                        os.write(b);
                        context.hasBefore = false;
                        context.before = b;
                        continue;
                    } else {
                        throw new IOException("bad packet found.");
                    }
                }
                if (isTailSupport) {
                    acc = rule.getTailAccumulator().apply(acc, context.before);
                }
                os.write(context.before);
            }
            context.before = b;
            context.hasBefore = true;
        }

        if (!context.isOk) {
            throw new IOException("packet recognize error, not expect byte found.");
        }

        if (isTailSupport) {
            ref.set(acc);
        }
    }

    public static <T> void write(StreamPacket packet, OutputStream os) throws IOException {
        write(DEFAULT_RULE, packet, os);
    }

    public static <T> void write(PacketRule<T> rule, StreamPacket packet, OutputStream os) throws IOException {
        // 写入头
        os.write(rule.getStart());
        os.write(rule.getStart());

        // 写入 head
        AtomicReference<T> ref = null;
        if (rule.isTailSupport()) {
            ref = new AtomicReference<>();
            T acc = rule.getTailInitializer().get();
            ref.set(acc);
        }

        byte[] head = packet.getHead();
        if (head != null) {
            InputStream is = new ByteArrayInputStream(head);
            writeEncoded(rule, ref, is, os);
            is.close();
        }

        // 写入分隔符
        os.write(rule.getSeparator());

        // 写入 body
        InputStream[] body = packet.getBody();
        if (body == null) {
            body = new InputStream[0];
        }

        if(body.length>127){
            throw new IOException("packet max body count is 127, but got "+body.length);
        }

        // 写入body个数
        byte len = (byte) body.length;
        os.write(len);

        // 写入body
        for (int i = 0; i < body.length; i++) {
            if (i > 0) {
                // 写入分隔符
                os.write(rule.getSeparator());
            }
            if (body[i] != null) {
                writeEncoded(rule, ref, body[i], os);
                body[i].close();
            }
        }


        if (len > 0) {
            // 写入分隔符
            os.write(rule.getSeparator());
        }

        if (rule.isTailSupport()) {
            T acc = ref.get();
            byte[] tail = rule.getTailFinisher().apply(acc);
            if (tail != null) {
                InputStream is = new ByteArrayInputStream(tail);
                writeEncoded(rule, null, is, os);
                is.close();
            }
        }

        // 写入结束符
        os.write(rule.getEnd());
        os.flush();

    }

    private static <T> void writeEncoded(PacketRule<T> rule, AtomicReference<T> ref, InputStream is, OutputStream os) throws IOException {
        BufferedInputStream bis = (is instanceof BufferedInputStream) ? ((BufferedInputStream) is) : (new BufferedInputStream(is));
        boolean isTailSupport = (rule.isTailSupport() && ref != null);
        T acc = null;
        if (isTailSupport) {
            acc = ref.get();
        }
        int bt = 0;
        while ((bt = bis.read()) >= 0) {
            byte b = (byte) bt;
            if (ref != null && isTailSupport) {
                acc = rule.getTailAccumulator().apply(acc, b);
            }
            if (b == rule.getStart()
                    || b == rule.getEscape()
                    || b == rule.getSeparator()
                    || b == rule.getEnd()) {
                os.write(rule.getEscape());
            }
            os.write(b);
        }
        os.flush();
        if (isTailSupport) {
            ref.set(acc);
        }
    }

}

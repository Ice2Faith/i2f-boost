package i2f.packet.rule;

import i2f.packet.data.StreamPacket;
import i2f.packet.io.LocalOutputStreamInputAdapter;

import java.io.*;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Ice2Faith
 * @date 2024/3/8 8:49
 * @desc
 */
public class StreamPacketResolver {
    protected static final PacketRule<Long> DEFAULT_RULE = PacketRule.defaultRule();

    public static class ReadPacketContext {
        public byte before = 0;
        public boolean hasBefore = false;
        public boolean isFirst = true;
        public boolean isOk = true;
    }

    public static <T> StreamPacket read(InputStream is) throws IOException {
        return read(DEFAULT_RULE, is);
    }

    public static <T> StreamPacket read(PacketRule<T> rule, InputStream is) throws IOException {
        RuleByte ruleByte=new RuleByte();
        StreamPacket ret = null;
        ReadPacketContext context = new ReadPacketContext();
        context.before = 0;
        context.hasBefore = false;
        context.isFirst = true;
        context.isOk = true;
        int bt = 0;
        // 读取开头
        LocalOutputStreamInputAdapter drop = new LocalOutputStreamInputAdapter(rule.getMemorySize());
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

        // 读取头数量
        int headLen=is.read();

        ruleByte.setHead(true);
        ruleByte.setCurrentCount(headLen);

        // 读取头
        byte[][] head=new byte[headLen][];
        LocalOutputStreamInputAdapter[] headOuts = new LocalOutputStreamInputAdapter[headLen];
        for (int i = 0; i < headLen; i++) {
            ruleByte.setCurrentIndex(i);
            headOuts[i] = new LocalOutputStreamInputAdapter();
            readNextItem(rule, ruleByte,ref, rule.getSeparator(), is, context, headOuts[i]);
            headOuts[i].close();
            head[i] = headOuts[i].toByteArray();
        }
        ret.setHead(head);


        // 读取体数量
        int bodyLen = is.read();

        ruleByte.setHead(false);
        ruleByte.setCurrentCount(bodyLen);

        // 读取体
        InputStream[] body = new InputStream[bodyLen];
        LocalOutputStreamInputAdapter[] outs = new LocalOutputStreamInputAdapter[bodyLen];
        for (int i = 0; i < bodyLen; i++) {
            ruleByte.setCurrentIndex(i);
            outs[i] = new LocalOutputStreamInputAdapter(rule.getMemorySize());
            readNextItem(rule, ruleByte,ref, rule.getSeparator(), is, context, outs[i]);
            outs[i].close();
            body[i] = outs[i].getInputStream();
        }
        ret.setBody(body);

        // 读取尾
        LocalOutputStreamInputAdapter tail = new LocalOutputStreamInputAdapter();
        readNextItem(rule, null,null, rule.getEnd(), is, context, tail);
        tail.close();
        ret.setTail(tail.toByteArray());

        if (isTailSupport) {
            if(headLen+bodyLen>0) {
                byte[] ruleTail = rule.getTailFinisher().apply(ref.get());
                ret.setRuleTail(ruleTail);
            }else{
                ret.setRuleTail(new byte[0]);
            }
        }

        return ret;
    }

    public static <T> void readNextItem(PacketRule<T> rule,RuleByte ruleByte, AtomicReference<T> ref, byte end, InputStream is, ReadPacketContext context, OutputStream os) throws IOException {
        boolean isTailSupport = (rule.isTailSupport() && ref != null);
        T acc = null;
        if (isTailSupport) {
            acc = ref.get();
        }

        long dataIndex=0;
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
                        byte wb=context.before;
                        if(ruleByte!=null){
                            ruleByte.setData(wb);
                            ruleByte.setDataIndex(dataIndex);
                            if(rule.getByteDecoder()!=null){
                                wb=rule.getByteDecoder().apply(ruleByte);
                                ruleByte.setData(wb);
                            }
                        }
                        if (isTailSupport) {
                            acc = rule.getTailAccumulator().apply(acc, ruleByte);
                        }
                        os.write(wb);
                        dataIndex++;
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
                        byte wb=b;
                        if(ruleByte!=null){
                            ruleByte.setData(wb);
                            ruleByte.setDataIndex(dataIndex);
                            if(rule.getByteDecoder()!=null){
                                wb=rule.getByteDecoder().apply(ruleByte);
                                ruleByte.setData(wb);
                            }
                        }
                        if (isTailSupport) {
                            acc = rule.getTailAccumulator().apply(acc, ruleByte);
                        }
                        os.write(wb);
                        context.hasBefore = false;
                        context.before = b;
                        dataIndex++;
                        continue;
                    } else {
                        throw new IOException("bad packet found.");
                    }
                }
                byte wb= context.before;
                if(ruleByte!=null){
                    ruleByte.setData(wb);
                    ruleByte.setDataIndex(dataIndex);
                    if(rule.getByteDecoder()!=null){
                        wb=rule.getByteDecoder().apply(ruleByte);
                        ruleByte.setData(wb);
                    }
                }
                if (isTailSupport) {
                    acc = rule.getTailAccumulator().apply(acc, ruleByte);
                }
                os.write(wb);
                dataIndex++;
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
        RuleByte ruleByte=new RuleByte();

        // 写入头
        os.write(rule.getStart());
        os.write(rule.getStart());


        AtomicReference<T> ref = null;
        if (rule.isTailSupport()) {
            ref = new AtomicReference<>();
            T acc = rule.getTailInitializer().get();
            ref.set(acc);
        }

        // 写入 head
        byte[][] head = packet.getHead();
        if(head==null){
            head=new byte[0][];
        }

        if(head.length>127){
            throw new IOException("packet max head count is 127, but got "+head.length);
        }

        // 写入head个数
        byte headLen = (byte) head.length;
        os.write(headLen);

        ruleByte.setHead(true);
        ruleByte.setCurrentCount(headLen);

        // 写入head
        for (int i = 0; i < head.length; i++) {
            ruleByte.setCurrentIndex(i);
            if (i > 0) {
                // 写入分隔符
                os.write(rule.getSeparator());
            }
            if (head[i] != null) {
                if(head[i]==null){
                    head[i]=new byte[0];
                }
                ByteArrayInputStream his=new ByteArrayInputStream(head[i]);
                writeEncoded(rule,ruleByte, ref,his , os);
                his.close();
            }
        }

        if(headLen>0) {
            // 写入分隔符
            os.write(rule.getSeparator());
        }

        // 写入 body
        InputStream[] body = packet.getBody();
        if (body == null) {
            body = new InputStream[0];
        }

        if(body.length>127){
            throw new IOException("packet max body count is 127, but got "+body.length);
        }

        // 写入body个数
        byte bodyLen = (byte) body.length;
        os.write(bodyLen);

        ruleByte.setHead(false);
        ruleByte.setCurrentCount(bodyLen);

        // 写入body
        for (int i = 0; i < body.length; i++) {
            ruleByte.setCurrentIndex(i);
            if (i > 0) {
                // 写入分隔符
                os.write(rule.getSeparator());
            }
            if (body[i] != null) {
                writeEncoded(rule, ruleByte,ref, body[i], os);
                body[i].close();
            }
        }


        if (bodyLen > 0) {
            // 写入分隔符
            os.write(rule.getSeparator());
        }

        if (rule.isTailSupport()) {
            if(headLen+bodyLen>0) {
                T acc = ref.get();
                byte[] tail = rule.getTailFinisher().apply(acc);
                if (tail != null) {
                    InputStream is = new ByteArrayInputStream(tail);
                    writeEncoded(rule, null,null, is, os);
                    is.close();
                }
            }
        }

        // 写入结束符
        os.write(rule.getEnd());
        os.flush();

    }

    public static <T> void writeEncoded(PacketRule<T> rule,RuleByte ruleByte, AtomicReference<T> ref, InputStream is, OutputStream os) throws IOException {
        BufferedInputStream bis = (is instanceof BufferedInputStream) ? ((BufferedInputStream) is) : (new BufferedInputStream(is));
        boolean isTailSupport = (rule.isTailSupport() && ref != null);
        T acc = null;
        if (isTailSupport) {
            acc = ref.get();
        }
        long dataIndex=0;
        int bt = 0;
        while ((bt = bis.read()) >= 0) {
            byte b = (byte) bt;
            byte wb=b;
            if(ruleByte!=null){
                ruleByte.setData(b);
                ruleByte.setDataIndex(dataIndex);
                if(rule.getByteEncoder()!=null){
                    wb=rule.getByteEncoder().apply(ruleByte);
                }
            }
            if (ref != null && isTailSupport) {
                acc = rule.getTailAccumulator().apply(acc, ruleByte);
            }
            if (wb == rule.getStart()
                    || wb == rule.getEscape()
                    || wb == rule.getSeparator()
                    || wb == rule.getEnd()) {
                os.write(rule.getEscape());
            }
            os.write(wb);
            dataIndex++;
        }
        os.flush();
        if (isTailSupport) {
            ref.set(acc);
        }
    }

}

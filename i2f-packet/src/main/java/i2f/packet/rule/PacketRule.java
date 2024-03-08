package i2f.packet.rule;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/3/8 9:48
 * @desc 封包规则
 * 0xee 0xee hh ... hh ll [bb ... bb 0xea]+ 0xea? tt ... tt 0xeb
 * 由0xee 0xee 引导包的开头，两个字节，避免单个字节时出现与转义的冲突
 * 后跟 hh ... hh 的可选头
 * 后跟ll表示的body的个数n
 * 后跟[bb ... bb 0xea]+ 表示的多个消息体，消息体使用 0xea 分隔
 * 如果长度ll为0时，0xea? 则没有此分隔符，否则跟着 0xea 分隔符
 * 后跟 tt ... tt 的可选尾部
 * 最后以0xeb结束封包
 *
 * 转义规则
 * 转义字符 0xef
 * 0xee -> 0xef 0xee
 * 0xef -> 0xef 0xef
 * 0xea -> 0xef 0xea
 * 0xeb -> 0xef 0xeb
 */
public class PacketRule<T> {
    // 由于转义字符的存在，因此选用一些频度不高的字节来控制，才比较合适
    // 0x20 - 0x7f (32-127) : 该区间是ascii可见字符部分，常用于英文文本中，不适合
    // 0x09(\t)  0x0d(\r) 0x0a(\n) : 控制空白符，不合适
    // 0x00: 控制字符串结尾，不合适
    // 0x00: 二进制文件中，可能存在大批量的0字节占位，不合适
    // 0x00-0x09: 二进制文件中，用于表示0-9的数字，占比较高，不合适
    // 0xf0-0xff: 二进制文件中，用于表示-16 - -1 ，占比较高，不合适
    // 0x80-0xcf: 这部分，大多用于多字节字符编码的常用字符，占比较高，不太适合
    // 0xd0-0xef: 这部分是不常用字符
    // 可以根据实际需要进行调整
    public static final byte DEFAULT_ESCAPE = (byte) 0xef;
    public static final byte DEFAULT_START = (byte) 0xee;
    public static final byte DEFAULT_SEPARATOR = (byte) 0xea;
    public static final byte DEFAULT_END = (byte) 0xeb;

    public static final Supplier<Long> DEFAULT_TAIL_INITIALIZER=()->0L;
    public static final BiFunction<Long,Byte,Long> DEFAULT_TAIL_ACCUMULATOR=(t,e)->((t+1)*31+e);
    public static final Function<Long,byte[]> DEFAULT_TAIL_FINISHER=(t)->{
        byte[] ret=new byte[8];
        for (int i = 0; i < 8; i++) {
            ret[i]=(byte)(t>>>((7-i)*8));
        }
        return ret;
    };

    private byte escape;
    private byte start;
    private byte separator;
    private byte end;

    private Supplier<T> tailInitializer;
    private BiFunction<T,Byte,T> tailAccumulator;
    private Function<T,byte[]> tailFinisher;

    public static PacketRule<?> defaultRule() {
        return new PacketRule<>(DEFAULT_ESCAPE,
                DEFAULT_START,
                DEFAULT_SEPARATOR,
                DEFAULT_END);
    }


    public static PacketRule<Long> defaultHashRule() {
        return new PacketRule<>(DEFAULT_ESCAPE,
                DEFAULT_START,
                DEFAULT_SEPARATOR,
                DEFAULT_END,
                DEFAULT_TAIL_INITIALIZER,
                DEFAULT_TAIL_ACCUMULATOR,
                DEFAULT_TAIL_FINISHER);
    }

    public PacketRule() {
    }

    public PacketRule(byte escape, byte start, byte separator, byte end) {
        this.escape = escape;
        this.start = start;
        this.separator = separator;
        this.end = end;
    }

    public PacketRule(byte escape, byte start, byte separator, byte end, Supplier<T> tailInitializer, BiFunction<T, Byte, T> tailAccumulator, Function<T, byte[]> tailFinisher) {
        this.escape = escape;
        this.start = start;
        this.separator = separator;
        this.end = end;
        this.tailInitializer = tailInitializer;
        this.tailAccumulator = tailAccumulator;
        this.tailFinisher = tailFinisher;
    }

    public boolean isTailSupport(){
        return this.tailInitializer!=null
                && this.tailAccumulator!=null
                && this.tailFinisher!=null;
    }

    public byte getEscape() {
        return escape;
    }

    public void setEscape(byte escape) {
        this.escape = escape;
    }

    public byte getStart() {
        return start;
    }

    public void setStart(byte start) {
        this.start = start;
    }

    public byte getSeparator() {
        return separator;
    }

    public void setSeparator(byte separator) {
        this.separator = separator;
    }

    public byte getEnd() {
        return end;
    }

    public void setEnd(byte end) {
        this.end = end;
    }

    public Supplier<T> getTailInitializer() {
        return tailInitializer;
    }

    public void setTailInitializer(Supplier<T> tailInitializer) {
        this.tailInitializer = tailInitializer;
    }

    public BiFunction<T, Byte, T> getTailAccumulator() {
        return tailAccumulator;
    }

    public void setTailAccumulator(BiFunction<T, Byte, T> tailAccumulator) {
        this.tailAccumulator = tailAccumulator;
    }

    public Function<T, byte[]> getTailFinisher() {
        return tailFinisher;
    }

    public void setTailFinisher(Function<T, byte[]> tailFinisher) {
        this.tailFinisher = tailFinisher;
    }
}

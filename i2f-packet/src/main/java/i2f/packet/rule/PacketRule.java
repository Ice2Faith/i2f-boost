package i2f.packet.rule;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/3/8 9:48
 * @desc 封包规则
 * 0xee 0xee lh [hh ... hh 0xea]+ 0xea? lb [bb ... bb 0xea]+ 0xea? (tt ... tt)? 0xeb
 * 由0xee 0xee 引导包的开头，两个字节，避免单个字节时出现与转义的冲突
 * 后跟[hh ... hh 0xea]+ 表示的多个消息体，消息体使用 0xea 分隔
 * 如果长度lh为0时，0xea? 则没有此分隔符，否则跟着 0xea 分隔符
 * 后跟lb表示的body的个数n
 * 后跟[bb ... bb 0xea]+ 表示的多个消息体，消息体使用 0xea 分隔
 * 如果长度ll为0时，0xea? 则没有此分隔符，否则跟着 0xea 分隔符
 * 后跟 (tt ... tt)? 的可选尾部
 * 当lh+lb==0是，表示既没有head也没有body，则是空包，空包则没有尾部
 * 否则，是有尾部的
 * 最后以0xeb结束封包
 * ------------------------------------------------
 * 转义规则
 * 转义字符 0xef
 * 0xee -> 0xef 0xee
 * 0xef -> 0xef 0xef
 * 0xea -> 0xef 0xea
 * 0xeb -> 0xef 0xeb
 * ------------------------------------------------
 * 举例说明：
 * 例1，空包：
 * 包内容
 * [0xee][0xee][0x00][0x00][0xeb]
 * 拆分
 * [0xee][0xee] 表示包开头标识
 * [0x00] 表示head的长度，为0
 * [0x00] 表示body的长度，为0
 * [0xeb] 表示包结束标识
 * ------------------------------------------------
 * 例2，一般包
 * 比较复杂，先给出对应的head和body的值
 * head:[0xea][0xeb][0x02][0x03][0x00]
 * head:
 * head:[0xea][0xeb][0x02][0x03][0x00]
 * body:[0x10][0xef][0xee][0xea][0xeb]
 * body:
 * body:[0x10][0xef][0xee][0xea][0xeb]
 * 可以看到，分别是三个head和三个body
 * 其中，head和body的第二个元素都是空
 * 包内容
 * [0xee][0xee][0x03][0xef][0xea][0xef][0xeb][0x02][0x03][0x00][0xea][0xea][0xef][0xea][0xef][0xeb][0x02][0x03][0x00][0xea][0x03][0x10][0xef][0xef][0xef][0xee][0xef][0xea][0xef][0xeb][0xea][0xea][0x10][0xef][0xef][0xef][0xee][0xef][0xea][0xef][0xeb][0xea][0x24][0xf6][0x84][0x71][0x7e][0x5c][0x73][0x80][0xeb]
 * 拆分
 * 因为有转义字符的存在，用空格隔开方便查看
 * [0xee][0xee] [0x03] [0xef][0xea][0xef][0xeb][0x02][0x03][0x00] [0xea] [0xea] [0xef][0xea][0xef][0xeb][0x02][0x03][0x00] [0xea] [0x03] [0x10][0xef][0xef][0xef][0xee][0xef][0xea][0xef][0xeb] [0xea] [0xea] [0x10][0xef][0xef][0xef][0xee][0xef][0xea][0xef][0xeb] [0xea] [0x24][0xf6][0x84][0x71][0x7e][0x5c][0x73][0x80] [0xeb]
 * 下面是拆分
 * [0xee][0xee] 包开始标记
 * [0x03] 值为3，表示有三个body
 * [0xef][0xea] [0xef][0xeb] [0x02] [0x03] [0x00] 是第一个head
 * [0xea] head分隔
 * 中间这里就是第二个head，没东西
 * [0xea] head分隔
 * [0xef][0xea] [0xef][0xeb] [0x02] [0x03] [0x00] 是第二个head
 * [0xea] head分隔
 * [0x03] 值为3，表示有三个body
 * [0x10] [0xef][0xef] [0xef][0xee] [0xef][0xea] [0xef][0xeb] 是第一个body
 * [0xea] body分隔
 * 中间这里就是第二个body，没东西
 * [0xea] body分隔
 * [0x10] [0xef][0xef] [0xef][0xee] [0xef][0xea] [0xef][0xeb] 是第三个body
 * [0xea] tail分隔
 * [0x24] [0xf6] [0x84] [0x71] [0x7e] [0x5c] [0x73] [0x80] 是tail
 * [0xeb] 包结束标记
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
    public static final int DEFAULT_MEMORY_SIZE=512*1024;

    public static final Supplier<Long> DEFAULT_TAIL_INITIALIZER=()->0L;
    public static final BiFunction<Long,RuleByte,Long> DEFAULT_TAIL_ACCUMULATOR=(t,e)->((t+1)*31+e.getData());
    public static final Function<Long,byte[]> DEFAULT_TAIL_FINISHER=(t)->{
        byte[] ret=new byte[8];
        for (int i = 0; i < 8; i++) {
            ret[i]=(byte)(t>>>((7-i)*8));
        }
        return ret;
    };

    public static final Function<RuleByte,Byte> DEFAULT_XOR_ENCODER=(e)->{
        long fac=0x31;
        fac=fac+(e.isHead()?3:5);
        fac=fac*11+e.getCurrentCount();
        fac=fac*13+e.getCurrentIndex();
        fac=fac*19+e.getDataIndex();
        return (byte)(e.getData()^fac);
    };

    private byte escape;
    private byte start;
    private byte separator;
    private byte end;

    private int memorySize;

    private Supplier<T> tailInitializer;
    private BiFunction<T,RuleByte,T> tailAccumulator;
    private Function<T,byte[]> tailFinisher;

    private Function<RuleByte,Byte> byteEncoder;
    private Function<RuleByte,Byte> byteDecoder;

    public static PacketRule<?> simpleRule() {
        return new PacketRule<>(DEFAULT_ESCAPE,
                DEFAULT_START,
                DEFAULT_SEPARATOR,
                DEFAULT_END,
                DEFAULT_MEMORY_SIZE);
    }

    public static PacketRule<Long> hashRule() {
        return new PacketRule<>(DEFAULT_ESCAPE,
                DEFAULT_START,
                DEFAULT_SEPARATOR,
                DEFAULT_END,
                DEFAULT_MEMORY_SIZE,
                DEFAULT_TAIL_INITIALIZER,
                DEFAULT_TAIL_ACCUMULATOR,
                DEFAULT_TAIL_FINISHER);
    }

    public static PacketRule<Long> defaultRule() {
        return new PacketRule<>(DEFAULT_ESCAPE,
                DEFAULT_START,
                DEFAULT_SEPARATOR,
                DEFAULT_END,
                DEFAULT_MEMORY_SIZE,
                DEFAULT_TAIL_INITIALIZER,
                DEFAULT_TAIL_ACCUMULATOR,
                DEFAULT_TAIL_FINISHER,
                DEFAULT_XOR_ENCODER,
                DEFAULT_XOR_ENCODER);
    }

    public PacketRule() {
    }

    public PacketRule(byte escape, byte start, byte separator, byte end, int memorySize) {
        this.escape = escape;
        this.start = start;
        this.separator = separator;
        this.end = end;
        this.memorySize = memorySize;
        assertValidBytes();
    }

    public PacketRule(byte escape, byte start, byte separator, byte end, int memorySize, Supplier<T> tailInitializer, BiFunction<T, RuleByte, T> tailAccumulator, Function<T, byte[]> tailFinisher) {
        this.escape = escape;
        this.start = start;
        this.separator = separator;
        this.end = end;
        this.memorySize = memorySize;
        this.tailInitializer = tailInitializer;
        this.tailAccumulator = tailAccumulator;
        this.tailFinisher = tailFinisher;
        assertValidBytes();
    }

    public PacketRule(byte escape, byte start, byte separator, byte end, int memorySize, Supplier<T> tailInitializer, BiFunction<T, RuleByte, T> tailAccumulator, Function<T, byte[]> tailFinisher, Function<RuleByte, Byte> byteEncoder, Function<RuleByte, Byte> byteDecoder) {
        this.escape = escape;
        this.start = start;
        this.separator = separator;
        this.end = end;
        this.memorySize = memorySize;
        this.tailInitializer = tailInitializer;
        this.tailAccumulator = tailAccumulator;
        this.tailFinisher = tailFinisher;
        this.byteEncoder = byteEncoder;
        this.byteDecoder = byteDecoder;
        assertValidBytes();
    }

    public boolean isTailSupport(){
        return this.tailInitializer!=null
                && this.tailAccumulator!=null
                && this.tailFinisher!=null;
    }

    public void assertValidBytes(){
        if(!isValidBytes()){
            throw new IllegalArgumentException("control bytes should be 4 different bytes.");
        }
    }

    public boolean isValidBytes(){
        Set<Byte> set=new HashSet<>();
        set.add(this.escape);
        set.add(this.start);
        set.add(this.separator);
        set.add(this.end);
        if(set.size()!=4){
            return false;
        }
        return true;
    }

    ////////////////////////////////////////////


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

    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public Supplier<T> getTailInitializer() {
        return tailInitializer;
    }

    public void setTailInitializer(Supplier<T> tailInitializer) {
        this.tailInitializer = tailInitializer;
    }

    public BiFunction<T, RuleByte, T> getTailAccumulator() {
        return tailAccumulator;
    }

    public void setTailAccumulator(BiFunction<T, RuleByte, T> tailAccumulator) {
        this.tailAccumulator = tailAccumulator;
    }

    public Function<T, byte[]> getTailFinisher() {
        return tailFinisher;
    }

    public void setTailFinisher(Function<T, byte[]> tailFinisher) {
        this.tailFinisher = tailFinisher;
    }

    public Function<RuleByte, Byte> getByteEncoder() {
        return byteEncoder;
    }

    public void setByteEncoder(Function<RuleByte, Byte> byteEncoder) {
        this.byteEncoder = byteEncoder;
    }

    public Function<RuleByte, Byte> getByteDecoder() {
        return byteDecoder;
    }

    public void setByteDecoder(Function<RuleByte, Byte> byteDecoder) {
        this.byteDecoder = byteDecoder;
    }
}

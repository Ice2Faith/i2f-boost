package i2f.core.guid;


/**
 * @author Ice2Faith
 * @date 2023/3/14 13:42
 * @desc 基于雪花算法，实现int32整形全局ID生成
 * 1位符号位
 * 28位时间戳（秒级）
 * 3位自增序列
 * 因此，每秒可产生2^3=8个ID
 * 仅适用于并发很低的场景使用
 * 并且，相对于起始时间，只能使用8-07-03 21:24:15
 * 这么长的时间，也就是8年多
 * 可以应用于一些表单提交类，不会长期使用（8年以上）的项目中
 */
public class Iuid {

    // 总位数
    public static final long bitsAmount = 32L;

    // 1位符号位
    public static final long bitsFlag = 1L;
    // 41位相对时间戳
    public static final long bitsTsm = 28L;
    // 剩下的位数为自增序列
    public static final long bitsSeq = bitsAmount - bitsFlag - bitsTsm;

    // 开始时间戳，2022-01-01 00:00:00
    private static final long beginTsm = 1640966400L;
    // 最大自增序列
    private static long maxSeqId = -1L ^ (-1L << bitsSeq);
    // 最大时间回退毫秒，如果发生时间回退，再次时间范围内，直接阻塞等待即可
    // 因此，此值不宜太大，否则阻塞时间太长
    private static long maxTsmMoveBackwards = 10;

    // 上一次的时间戳
    private long lastTsm = -1L;
    // 毫秒内自增序列值
    private long seqId = 0L;

    /**
     * 全局的实例
     */
    public static Iuid UID = new Iuid();

    public Iuid() {

    }

    /**
     * 获取ID
     *
     * @return
     */
    public synchronized int nextId() {
        long now = getTsm();

        if (lastTsm < 0) {
            lastTsm = now;
        }

        if (now < lastTsm) {
            long diff = lastTsm - now;
            // 如果发生时间会退，最大允许回退的时间量，小于此时间量，阻塞等待即可
            if (diff <= maxTsmMoveBackwards) {
                now = untilNextTsm(lastTsm);
            } else {
                throw new IllegalStateException("时间回退 " + (lastTsm - now) + " second(s)");
            }
        }

        // 同一时间戳内，自增
        if (now == lastTsm) {
            seqId = (seqId + 1) % maxSeqId;
            if (seqId == 0) {
                // 阻塞等待下一毫秒到来
                now = untilNextTsm(now);
            }
        } else {
            seqId = 0;
        }

        // 更新时间戳
        lastTsm = now;

        // 生成唯一ID
        long id = ((now - beginTsm) << (bitsSeq))
                | seqId;

        return (int) id;
    }

    /**
     * 等待下一秒的到来
     *
     * @param tsm
     * @return
     */
    public long untilNextTsm(long tsm) {
        long now = getTsm();
        while (now <= tsm) {
            now = getTsm();
        }
        return now;
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public long getTsm() {
        return System.currentTimeMillis() / 1000;
    }

}

package i2f.core.guid;

/**
 * @author Ice2Faith
 * @date 2023/3/13 15:58
 * @desc
 */

import java.io.ByteArrayOutputStream;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;

/**
 * Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括10位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 */
public class Uid {

    // 总位数
    public static final long bitsAmount = 64L;

    // 1位符号位
    public static final long bitsFlag = 1L;
    // 41位相对时间戳
    public static final long bitsTsm = 41L;
    // 10位机器标识位
    // 可以通过调整机器位数来实现调整自增序列的数量
    // 从而调整高并发下ID的生成性能
    // 也可以实现扩容支持的机器台数
    public static final long bitsWorker = 10;
    // 剩下的位数为自增序列
    public static final long bitsSeq = bitsAmount - bitsFlag - bitsTsm - bitsWorker;

    // 自动时钟，保证并发性，直接使用 System.currentTimeMillis() 的性能太差，但是会有一个问题，就是可能不太准确
    // 实际情况中，使用此方式的时间戳比真实的时间戳一般是晚一定的毫秒数的，因此在UID的场景中，并不会导致时间回溯
    // 可以这样使用，一般比真实时间晚10毫秒以内，不排除更大值的可能
    private static final AtomicLong clock = new AtomicLong(System.currentTimeMillis());
    private static ScheduledExecutorService clockPool = Executors.newSingleThreadScheduledExecutor();

    static {
        clockPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                clock.addAndGet(1);
            }
        }, 0, 1, TimeUnit.MILLISECONDS);
    }

    // 开始时间戳，2020-01-01 00:00:00
    private static final long beginTsm = 1577808000000L;
    // 最大机器ID
    private static long maxWorkerId = -1L ^ (-1L << bitsWorker);
    // 最大自增序列
    private static long maxSeqId = -1L ^ (-1L << bitsSeq);
    // 最大时间回退毫秒，如果发生时间回退，再次时间范围内，直接阻塞等待即可
    // 因此，此值不宜太大，否则阻塞时间太长
    private static long maxTsmMoveBackwards = 10 * 1000;

    // 上一次的时间戳
    private long lastTsm = -1L;
    // 机器ID
    private long workerId = -1L;
    // 毫秒内自增序列值
    private long seqId = 0L;

    /**
     * 全局的实例
     */
    public static Uid UID = getByMac();

    /**
     * 根据MAC地址序列校验和获取实例
     *
     * @return
     */
    public static Uid getByMac() {
        long workerId = 0;
        try {
            TreeSet<String> macSet = new TreeSet<>();
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface element = interfaces.nextElement();
                try {
                    if (!element.isLoopback() && !element.isVirtual()) {
                        byte[] macBytes = element.getHardwareAddress();
                        if (macBytes != null) {
                            StringBuilder builder = new StringBuilder();
                            for (byte bt : macBytes) {
                                builder.append(":");
                                builder.append(String.format("%02X", (int) (bt & 0x0ff)));
                            }
                            String mac = builder.toString();
                            if (!"".equals(mac)) {
                                mac = mac.substring(1);
                            }
                            String niName = element.getName() + "/" + mac;
                            macSet.add(niName);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("[WARN] 获取网卡信息失败：" + e.getMessage());
                }

            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            CheckedOutputStream cos = new CheckedOutputStream(bos, new Adler32());
            for (String mac : macSet) {
                cos.write("[".getBytes());
                cos.write(mac.getBytes());
                cos.write("]".getBytes());
            }
            cos.close();
            long checksum = cos.getChecksum().getValue();
            workerId = Math.abs(checksum) % maxWorkerId;
        } catch (Exception e) {
            System.out.println("[WARN] 获取网卡信息失败：" + e.getMessage());
        }
        return new Uid(workerId);
    }

    public Uid(long workerId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException("无效的机器ID，机器ID在整个应用中应该唯一，值应该在区间[0," + maxWorkerId + "]");
        }
        this.workerId = workerId;
    }

    /**
     * 获取ID
     *
     * @return
     */
    public synchronized long nextId() {
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
                throw new IllegalStateException("时间回退 " + (lastTsm - now) + " millisecond(s)");
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
        return ((now - beginTsm) << (bitsWorker + bitsSeq))
                | (workerId << bitsSeq)
                | seqId;

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
//        return System.currentTimeMillis();
        return clock.get();
    }


    /**
     * 使用此方式存在对象构建的开销
     * 性能要求很高的场景下，不建议使用此方法
     * 此方法是提供在特定场合，需要对ID不同部分使用或调试时使用的
     *
     * @return
     */
    public synchronized UidPart nextPart() {
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
                throw new IllegalStateException("时间回退 " + (lastTsm - now) + " millisecond(s)");
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

        return new UidPart(now - beginTsm, workerId, seqId);
    }

    public class UidPart {
        public long tsm;
        public long workerId;
        public long seqId;

        public UidPart(long tsm, long workerId, long seqId) {
            this.tsm = tsm;
            this.workerId = workerId;
            this.seqId = seqId;
        }
    }

}

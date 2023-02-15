package i2f.springboot.log.data;

/**
 * @author Ice2Faith
 * @date 2023/2/9 11:10
 * @desc
 */
public class LinuxTop5Dto {
    public String topDate;
    public int topDays;
    public String topTime;
    public int topUsers;
    public double loadAverage1;
    public double loadAverage2;
    public double loadAverage3;

    public int tasksTotal;
    public int tasksRunning;
    public int tasksSleeping;
    public int tasksStopped;
    public int tasksZombie;

    public double cpuUs;
    public double cpuSy;
    public double cpuNi;
    public double cpuId;
    public double cpuWa;
    public double cpuHi;
    public double cpuSi;
    public double cpuSt;

    public long kibMemTotal;
    public long kibMemFree;
    public long kibMemUsed;
    public long kibMemBuffCache;

    public long kibSwapTotal;
    public long kibSwapFree;
    public long kibSwapUsed;
    public long kibSwapAvailMem;
}

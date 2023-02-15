package i2f.springboot.log.data;

/**
 * @author Ice2Faith
 * @date 2023/2/9 11:10
 * @desc
 */
public class LinuxFreeDto {
    public long memTotal;
    public long memUsed;
    public long memFree;
    public long memShared;
    public long memBuffCache;
    public long memAvailable;

    public long swapTotal;
    public long swapUsed;
    public long swapFree;
}

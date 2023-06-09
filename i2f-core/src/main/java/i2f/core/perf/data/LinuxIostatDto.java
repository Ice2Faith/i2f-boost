package i2f.core.perf.data;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/2/13 16:27
 * @desc
 */
public class LinuxIostatDto {
    public Double userPer;
    public Double nicePer;
    public Double systemPer;
    public Double iowaitPer;
    public Double stealPer;
    public Double idlePer;
    public List<LinuxIostatItemDto> items;
}

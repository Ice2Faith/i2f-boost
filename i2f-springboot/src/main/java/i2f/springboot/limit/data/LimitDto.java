package i2f.springboot.limit.data;

import i2f.springboot.limit.consts.LimitType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/8/3 8:49
 * @desc
 */
@Data
@NoArgsConstructor
public class LimitDto {
    private boolean enable;

    private int window;
    private TimeUnit unit;

    private int count;

    private ConcurrentSkipListSet<Long> currents;
}

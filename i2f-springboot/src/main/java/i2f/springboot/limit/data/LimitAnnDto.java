package i2f.springboot.limit.data;

import i2f.springboot.limit.consts.LimitType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/8/3 8:49
 * @desc
 */
@Data
@NoArgsConstructor
public class LimitAnnDto {
    private boolean value;

    private int window;
    private TimeUnit unit;

    private int count;
    private LimitType type;
}

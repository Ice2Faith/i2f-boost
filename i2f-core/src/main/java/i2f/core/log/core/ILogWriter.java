package i2f.core.log.core;

import i2f.core.log.data.LogDto;

/**
 * @author Ice2Faith
 * @date 2023/8/2 9:09
 * @desc
 */
public interface ILogWriter {
    void write(LogDto dto);
}

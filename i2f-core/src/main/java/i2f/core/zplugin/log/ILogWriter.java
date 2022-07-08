package i2f.core.zplugin.log;

import i2f.core.zplugin.log.data.LogData;

/**
 * @author ltb
 * @date 2022/3/28 16:48
 * @desc
 */
public interface ILogWriter {
    void write(LogData data);
}

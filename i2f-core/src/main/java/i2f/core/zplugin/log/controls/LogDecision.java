package i2f.core.zplugin.log.controls;

import i2f.core.zplugin.log.data.LogData;

/**
 * @author ltb
 * @date 2022/3/3 13:59
 * @desc
 */
public interface LogDecision {
    boolean decision(LogData log);
}

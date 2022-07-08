package i2f.core.zplugin.log.impl;

import i2f.core.zplugin.log.data.LogData;
import i2f.core.zplugin.log.enums.LogLevel;

/**
 * @author ltb
 * @date 2022/3/28 19:57
 * @desc
 */
public class StdoutLogWriter extends AbstractPlainTextLogWriter {
    @Override
    public void writeTextLog(String text, LogData data) {
        LogLevel level=data.getLevel();
        if(level!=null && level.level()<=LogLevel.WARN.level()){
            System.err.println(text);
            return;
        }
        System.out.println(text);
    }
}

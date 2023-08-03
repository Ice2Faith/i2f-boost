package i2f.core.log.impl;

import i2f.core.log.consts.LogLevel;
import i2f.core.log.data.LogDto;

/**
 * @author Ice2Faith
 * @date 2023/8/2 10:38
 * @desc
 */
public class SysoutLogWriter extends AbsPrintLogWriter {
    @Override
    public void print(String str, LogLevel level, LogDto dto) {
        if(level.code()<=LogLevel.WARN.code()){
            System.err.println(str);
        }else{
            System.out.println(str);
        }
    }
}

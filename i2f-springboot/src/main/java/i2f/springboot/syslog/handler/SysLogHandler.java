package i2f.springboot.syslog.handler;

import i2f.springboot.syslog.data.SysLogDto;

/**
 * @author Ice2Faith
 * @date 2023/2/7 10:17
 * @desc
 */
public interface SysLogHandler {
    void handle(SysLogDto log);
}

package i2f.springboot.log.handler;

import i2f.springboot.log.data.SysLogDto;

/**
 * @author Ice2Faith
 * @date 2023/2/7 10:17
 * @desc
 */
public interface SysLogHandler {
    void handle(SysLogDto log);
}

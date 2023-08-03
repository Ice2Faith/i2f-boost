package i2f.core.log.data;

import i2f.core.log.consts.LogLevel;
import i2f.core.log.consts.LogSource;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/8/1 18:21
 * @desc
 */
@Data
@NoArgsConstructor
public class LogDto {
    private Date time;
    private LogLevel level;
    private String location;

    private String system;
    private String module;
    private String label;

    private LogSource source;

    private String thread;

    private String content;

    private String clazz;
    private String method;
    private String args;
    private String ret;
    private long useMillSeconds;

    private String exceptClass;
    private String exceptMsg;
    private String exceptStack;

    private LogOriginDto origin;
}

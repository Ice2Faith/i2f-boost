package i2f.springboot.log;

import i2f.springboot.log.impl.SysLoggerImpl;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2023/2/7 10:16
 * @desc
 */
public class SysLoggerFactory {
    private static Map<String, SysLogger> cachedSysLogger = new ConcurrentHashMap<>();

    public static SysLogger getLogger(String name) {
        if (StringUtils.isEmpty(name)) {
            name = SysLogger.class.getName();
        }
        if (cachedSysLogger.containsKey(name)) {
            return cachedSysLogger.get(name);
        }
        SysLogger logger = new SysLoggerImpl(name);
        cachedSysLogger.put(name, logger);
        return logger;
    }

    public static SysLogger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }
}

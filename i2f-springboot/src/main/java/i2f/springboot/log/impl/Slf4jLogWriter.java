package i2f.springboot.log.impl;

import i2f.core.log.consts.LogLevel;
import i2f.core.log.data.LogDto;
import i2f.core.log.impl.AbsPrintLogWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author Ice2Faith
 * @date 2023/8/2 10:38
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.log.slf4j.enable:true}")
@Component
public class Slf4jLogWriter extends AbsPrintLogWriter {
    private Logger defaultLogger = LoggerFactory.getLogger(Slf4jLogWriter.class);

    @Override
    public void print(String str, LogLevel level, LogDto dto) {
        Logger logger = null;
        if (logger == null) {
            if (!StringUtils.isEmpty(dto.getLocation())) {
                logger = LoggerFactory.getLogger(dto.getLocation());
            }
        }
        if (logger == null) {
            if (!StringUtils.isEmpty(dto.getClazz())) {
                logger = LoggerFactory.getLogger(dto.getClazz());
            }
        }
        if (logger == null) {
            logger = defaultLogger;
        }

        if (level == LogLevel.FATAL) {
            logger.error(str);
        } else if (level == LogLevel.ERROR) {
            logger.error(str);
        } else if (level == LogLevel.WARN) {
            logger.warn(str);
        } else if (level == LogLevel.INFO) {
            logger.info(str);
        } else if (level == LogLevel.DEBUG) {
            logger.debug(str);
        } else if (level == LogLevel.TRACE) {
            logger.trace(str);
        } else {
            logger.info(str);
        }
    }
}

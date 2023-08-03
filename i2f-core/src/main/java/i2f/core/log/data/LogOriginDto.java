package i2f.core.log.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2023/8/1 18:21
 * @desc
 */
@Data
@NoArgsConstructor
public class LogOriginDto {
    private LogAnnDto ann;

    private Thread thread;

    private Method method;
    private Object[] args;
    private Object ret;

    private Throwable except;
}

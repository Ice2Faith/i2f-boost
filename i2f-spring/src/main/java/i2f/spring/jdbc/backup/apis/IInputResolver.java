package i2f.spring.jdbc.backup.apis;


import i2f.spring.jdbc.backup.data.BasicIoMeta;

import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/10/4 14:07
 * @desc
 */
public interface IInputResolver {
    void begin(BasicIoMeta meta);

    List<Map<String, Object>> resolve() throws Exception;

    void end();
}

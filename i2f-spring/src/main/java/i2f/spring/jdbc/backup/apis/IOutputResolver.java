package i2f.spring.jdbc.backup.apis;


import i2f.core.jdbc.data.PageMeta;
import i2f.spring.jdbc.backup.data.BasicIoMeta;

import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/10/4 9:43
 * @desc
 */
public interface IOutputResolver {
    void begin(BasicIoMeta meta);

    void resolve(List<Map<String, Object>> list, PageMeta page) throws Exception;

    void end();
}

package i2f.core.jdbc.core;

import i2f.core.annotations.remark.Author;

/**
 * @author ltb
 * @date 2021/9/27
 */
@Author("i2f")
public interface IJdbcMeta {
    String getDriverClassName();
    String getUrl();
    String getUsername();
    String getPassword();
}

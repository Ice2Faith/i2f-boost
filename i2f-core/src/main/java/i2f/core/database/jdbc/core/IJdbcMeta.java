package i2f.core.database.jdbc.core;

import i2f.core.annotations.remark.Author;

/**
 * @author ltb
 * @date 2021/9/27
 */
@Author("i2f")
public interface IJdbcMeta {
    String getDriver();

    String getUrl();
    String getUsername();
    String getPassword();
}

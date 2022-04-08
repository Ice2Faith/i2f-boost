package i2f.core.jdbc.core;

import i2f.core.annotations.remark.Author;

/**
 * @author ltb
 * @date 2022/3/23 21:16
 * @desc
 */
@Author("i2f")
public class JdbcMetaAdapter implements IJdbcMeta{
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public JdbcMetaAdapter() {
    }

    public JdbcMetaAdapter(String driverClassName, String url, String username, String password) {
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public JdbcMetaAdapter setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        return this;
    }

    public JdbcMetaAdapter setUrl(String url) {
        this.url = url;
        return this;
    }

    public JdbcMetaAdapter setUsername(String username) {
        this.username = username;
        return this;
    }

    public JdbcMetaAdapter setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String getDriverClassName() {
        return driverClassName;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}

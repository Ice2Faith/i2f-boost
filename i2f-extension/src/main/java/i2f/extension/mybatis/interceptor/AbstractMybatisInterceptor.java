package i2f.extension.mybatis.interceptor;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Plugin;

import java.util.Properties;

/**
 * @author ltb
 * @date 2022/4/4 15:20
 * @desc
 */
public abstract class AbstractMybatisInterceptor implements Interceptor {

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

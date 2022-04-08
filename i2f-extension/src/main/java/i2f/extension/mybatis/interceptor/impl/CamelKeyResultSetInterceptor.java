package i2f.extension.mybatis.interceptor.impl;

import i2f.extension.mybatis.interceptor.AbstractMybatisInterceptor;
import i2f.extension.mybatis.interceptor.proxy.MybatisInterceptorUtil;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.sql.Statement;

/**
 * @author ltb
 * @date 2022/4/4 16:16
 * @desc
 */
@Intercepts(
        @Signature(type = ResultSetHandler.class,
        method = "handleResultSets",
        args = {Statement.class}
        )
)
public class CamelKeyResultSetInterceptor extends AbstractMybatisInterceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return MybatisInterceptorUtil.intercept(invocation,new CamelKeyResultSetProxyHandler());
    }
}

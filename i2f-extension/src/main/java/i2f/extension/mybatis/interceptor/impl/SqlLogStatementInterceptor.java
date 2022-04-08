package i2f.extension.mybatis.interceptor.impl;

import i2f.extension.mybatis.interceptor.AbstractMybatisInterceptor;
import i2f.extension.mybatis.interceptor.proxy.MybatisInterceptorUtil;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Statement;

/**
 * @author ltb
 * @date 2022/4/4 16:16
 * @desc
 */
@Intercepts(
        {
                @Signature(type = StatementHandler.class,
                        method = "query",
                        args = {Statement.class, ResultHandler.class}
                ),
                @Signature(type = StatementHandler.class,
                        method = "update",
                        args = {Statement.class}
                ),
                @Signature(type = StatementHandler.class,
                        method = "queryCursor",
                        args = {Statement.class}
                ),
                @Signature(type = StatementHandler.class,
                        method = "batch",
                        args = {Statement.class}
                )
        }
)
public class SqlLogStatementInterceptor extends AbstractMybatisInterceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return MybatisInterceptorUtil.intercept(invocation, new SqlLogStatementProxyHandler());
    }
}

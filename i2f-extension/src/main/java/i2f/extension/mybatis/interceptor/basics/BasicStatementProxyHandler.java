package i2f.extension.mybatis.interceptor.basics;

import i2f.core.proxy.impl.BasicProxyHandler;
import i2f.core.reflect.ValueResolver;
import i2f.extension.mybatis.interceptor.proxy.MybatisInterceptorUtil;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/4/4 20:22
 * @desc
 */
public class BasicStatementProxyHandler extends BasicProxyHandler {

    public boolean valid(Object ivkObj){
        return ivkObj instanceof StatementHandler;
    }

    public MappedStatement findMappedStatement(Object ivkObj){
        if(!(ivkObj instanceof RoutingStatementHandler)){
            return null;
        }
        return (MappedStatement)ValueResolver.get(ivkObj,"delegate.mappedStatement");
    }

    public Method findMapperMethod(Object ivkObj){
        MappedStatement stat=findMappedStatement(ivkObj);
        if(stat==null){
            return null;
        }
        return MybatisInterceptorUtil.getMappedMethod(stat);
    }
}

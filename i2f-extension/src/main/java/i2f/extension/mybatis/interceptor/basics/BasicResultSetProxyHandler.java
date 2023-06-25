package i2f.extension.mybatis.interceptor.basics;

import i2f.core.lang.proxy.impl.BasicProxyHandler;
import i2f.core.reflection.reflect.ValueResolver;
import i2f.extension.mybatis.interceptor.proxy.MybatisInterceptorUtil;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/4/4 20:22
 * @desc
 */
public class BasicResultSetProxyHandler extends BasicProxyHandler {

    public boolean valid(Object ivkObj){
        return ivkObj instanceof ResultSetHandler;
    }

    public MappedStatement findMappedStatement(Object ivkObj){
        if(!(ivkObj instanceof DefaultResultSetHandler)){
            return null;
        }
        return (MappedStatement) ValueResolver.get(ivkObj,"mappedStatement");
    }

    public Method findMapperMethod(Object ivkObj){
        MappedStatement stat=findMappedStatement(ivkObj);
        if(stat==null){
            return null;
        }
        return MybatisInterceptorUtil.getMappedMethod(stat);
    }
}

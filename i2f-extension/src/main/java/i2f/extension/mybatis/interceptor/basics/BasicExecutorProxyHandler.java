package i2f.extension.mybatis.interceptor.basics;

import i2f.core.proxy.impl.BasicProxyHandler;
import i2f.extension.mybatis.interceptor.proxy.MybatisInterceptorUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/4/4 20:18
 * @desc
 */
public class BasicExecutorProxyHandler extends BasicProxyHandler {

    public boolean valid(Object ivkObj){
        return ivkObj instanceof Executor;
    }

    public MappedStatement findMappedStatement(Object ... args){
        for(Object item : args){
            if(item instanceof MappedStatement){
                return (MappedStatement) item;
            }
        }
        return null;
    }

    public Method findMapperMethod(Object ... args){
        MappedStatement stat=findMappedStatement(args);
        if(stat==null){
            return null;
        }
        return MybatisInterceptorUtil.getMappedMethod(stat);
    }
}

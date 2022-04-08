package i2f.extension.mybatis.interceptor.basics;

import i2f.core.proxy.impl.BasicProxyHandler;
import i2f.core.reflect.ValueResolver;
import i2f.extension.mybatis.interceptor.proxy.MybatisInterceptorUtil;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/4/4 20:22
 * @desc
 */
public class BasicParameterProxyHandler extends BasicProxyHandler {

    public boolean valid(Object ivkObj){
        return ivkObj instanceof ParameterHandler;
    }

    public MappedStatement findMappedStatement(Object ivkObj){
        if(!(ivkObj instanceof DefaultParameterHandler)){
            return null;
        }
        return (MappedStatement)ValueResolver.get(ivkObj,"mappedStatement");
    }

    public Method findMapperMethod(Object ivkObj){
        MappedStatement stat=findMappedStatement(ivkObj);
        if(stat==null){
            return null;
        }
        return MybatisInterceptorUtil.getMappedMethod(stat);
    }
}

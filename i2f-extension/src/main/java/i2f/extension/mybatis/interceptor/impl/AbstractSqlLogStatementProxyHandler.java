package i2f.extension.mybatis.interceptor.impl;

import i2f.core.proxy.IInvokable;
import i2f.core.reflect.ValueResolver;
import i2f.core.reflect.core.ReflectResolver;
import i2f.extension.mybatis.interceptor.annotations.MybatisLog;
import i2f.extension.mybatis.interceptor.basics.BasicStatementProxyHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/4/4 15:59
 * @desc
 */
public abstract class AbstractSqlLogStatementProxyHandler extends BasicStatementProxyHandler {

    @Override
    public Object after(Object context,Object ivkObj, IInvokable invokable, Object retVal, Object... args) {
        if(!valid(ivkObj)){
            return retVal;
        }

        return retVal;
    }

    @Override
    public Object before(Object context,Object ivkObj, IInvokable invokable, Object... args) {
        if(!(ivkObj instanceof RoutingStatementHandler)){
            return null;
        }
        Method method=findMapperMethod(ivkObj);
        MybatisLog ann= ReflectResolver.findElementAnnotation(method, MybatisLog.class,true,false,false);
        if(ann==null || !ann.value()){
            return null;
        }

        BoundSql boundSql=(BoundSql) ValueResolver.get(ivkObj,"delegate.boundSql");
        String sql=boundSql.getSql();
        List<MybatisSqlLogData> paramsList=new ArrayList<>();
        List<ParameterMapping> params=boundSql.getParameterMappings();
        Object paramObj=boundSql.getParameterObject();
        for(ParameterMapping item : params){
            paramsList.add(new MybatisSqlLogData(item.getProperty(),item.getJavaType(),item.getJdbcTypeName(),ValueResolver.get(paramObj,item.getProperty())));
        }
        writeLog(method,sql,paramsList);
        return null;
    }

    public static class MybatisSqlLogData{
        public String property;
        public Class javaType;
        public String jdbcType;
        public Object value;

        public MybatisSqlLogData(String property, Class javaType, String jdbcType, Object value) {
            this.property = property;
            this.javaType = javaType;
            this.jdbcType = jdbcType;
            this.value = value;
        }
    }

    protected abstract void writeLog(Method method,String sql,List<MybatisSqlLogData> params);

}

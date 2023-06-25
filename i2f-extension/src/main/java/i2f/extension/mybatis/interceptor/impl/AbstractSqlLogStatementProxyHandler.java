package i2f.extension.mybatis.interceptor.impl;

import i2f.core.lang.proxy.IInvokable;
import i2f.core.reflection.reflect.ValueResolver;
import i2f.core.reflection.reflect.core.ReflectResolver;
import i2f.extension.mybatis.interceptor.annotations.MybatisLog;
import i2f.extension.mybatis.interceptor.basics.BasicStatementProxyHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author ltb
 * @date 2022/4/4 15:59
 * @desc
 */
public abstract class AbstractSqlLogStatementProxyHandler extends BasicStatementProxyHandler {
    protected MybatisLog ann;

    protected Method method;
    protected String sql;
    protected List<MybatisSqlLogData> params;

    protected String seqId;

    protected Throwable ex;
    protected long useMillSecond;
    protected Integer retCount;
    protected Number retNum;

    @Override
    public Object initContext() {
        long btime= System.currentTimeMillis();
        seqId=btime+"_"+Thread.currentThread().getId()+"_"+new Random().nextInt(1024);
        return btime;
    }

    @Override
    public Object after(Object context,Object ivkObj, IInvokable invokable, Object retVal, Object... args) {
        if(!valid(ivkObj)){
            return retVal;
        }
        if(ann==null || !ann.value()){
            return retVal;
        }
        long curr=System.currentTimeMillis();
        long diff=curr-(Long)context;
        this.useMillSecond=diff;

        if(retVal instanceof List){
            int size=((List)retVal).size();
            this.retCount=size;
        }
        if(retVal instanceof Number){
            Number num=(Number)retVal;
            this.retNum=num;
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
        this.ann=ann;
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

        this.method=method;
        this.sql=sql;
        this.params=paramsList;
        return null;
    }

    @Override
    public Throwable except(Object context, Object ivkObj, IInvokable invokable, Throwable ex, Object... args) {
        if(ann==null || !ann.value()){
            return ex;
        }
        this.ex=ex;
        return ex;
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

    @Override
    public void onFinally(Object context, Object ivkObj, IInvokable invokable, Object... args) {
        if(ann!=null && ann.value()){
            writeFinallyLog(seqId,method,sql,params,ex,retCount,retNum,useMillSecond);
        }
    }

    protected abstract void writeFinallyLog(String seqId, Method method, String sql, List<MybatisSqlLogData> params, Throwable ex, Integer retCount, Number retNum, long useMillSecond);
}

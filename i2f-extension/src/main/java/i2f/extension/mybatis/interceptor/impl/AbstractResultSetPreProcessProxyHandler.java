package i2f.extension.mybatis.interceptor.impl;

import i2f.core.jdbc.core.JdbcProvider;
import i2f.core.jdbc.data.DBResultData;
import i2f.core.proxy.IInvokable;
import i2f.core.proxy.impl.IMethodAccessInvokable;
import i2f.core.reflect.ValueResolver;
import i2f.core.reflect.bean.BeanResolver;
import i2f.core.reflect.convert.ConvertResolver;
import i2f.core.reflect.core.ReflectResolver;
import i2f.extension.mybatis.interceptor.annotations.MybatisCamel;
import i2f.extension.mybatis.interceptor.basics.BasicResultSetProxyHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.mapping.ResultMap;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/4/4 15:59
 * @desc
 */
public abstract class AbstractResultSetPreProcessProxyHandler extends BasicResultSetProxyHandler {

    @Override
    public Object after(Object context,Object ivkObj, IInvokable invokable, Object retVal, Object... args) {
        if(!valid(ivkObj)){
            return retVal;
        }
        Method method=findMapperMethod(ivkObj);
        MybatisCamel ann=ReflectResolver.findElementAnnotation(method, MybatisCamel.class,true,false,false);
        if(ann==null || !ann.value()){
            return retVal;
        }
        if(!(invokable instanceof IMethodAccessInvokable)){
            return retVal;
        }
        IMethodAccessInvokable ivk=(IMethodAccessInvokable)invokable;
        if(!"handleResultSets".equals(ivk.method().getName())){
            return retVal;
        }
        if(!(retVal instanceof List)){
            return retVal;
        }
        List list=(List)retVal;
        if(list.size()==0){
            return retVal;
        }
        Object obj=list.get(0);
        if(!(obj instanceof Map)){
            return retVal;
        }
        List<Map<String, Object>> ret = preProcess((List<Map<String, Object>>) list);

        return ret;
    }

    protected abstract List<Map<String, Object>> preProcess(List<Map<String, Object>> list);

    @Override
    public Object before(Object context,Object ivkObj, IInvokable invokable, Object... args) {
        if(!(ivkObj instanceof DefaultResultSetHandler)){
            return null;
        }
        Method method=findMapperMethod(ivkObj);
        MybatisCamel ann=ReflectResolver.findElementAnnotation(method, MybatisCamel.class,true,false,false);
        if(ann==null || !ann.value()){
            return null;
        }

        DefaultResultSetHandler resultHandler=(DefaultResultSetHandler)ivkObj;
        List<ResultMap> list=(List<ResultMap>)ValueResolver.get(resultHandler,"mappedStatement.resultMaps");
        if(list==null || list.size()==0){
            return null;
        }
        ResultMap map= list.get(0);
        Class type=map.getType();
        if(ConvertResolver.isInTypes(type,Map.class)){
            return null;
        }

        Statement stat=(Statement)args[0];

        ResultSet rs=null;
        List<Object> ret=new ArrayList<>();
        try{
            rs=stat.getResultSet();
            DBResultData data= JdbcProvider.parseResultSet(rs);
            List<Map<String,Object>> src=data.getDatas();
            src= preProcess(src);
            ret=BeanResolver.mapList2BeanList(src,type,true,null);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }finally {
            if(rs!=null){
                try{
                    rs.close();
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }
        return ret;
    }


}

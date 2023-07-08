# Mybatis Interceptor plugin develop

---
## 1. declare class
- define a class implements Interceptor
```java
public class AbstractMybatisInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
```
- implements intercept method to do you logical

## declare signature for class
- @Intercepts annotated on class who implements Interceptor
- add multiply @Signature in @Intercepts annotation
- like this
```java
@Intercepts(@Signature(
        type = ResultSetHandler.class,
        method = "handleResultSets",
        args = {Statement.class}
))
public class MybatisInterceptor implements Interceptor {
}
```
- which mean that
- intercept class ResultSetHandler.class
- intercept class's method handleResultSets
- which method arguments match Statement.class
- so intercept this method
```java
public interface ResultSetHandler {
    <E> List<E> handleResultSets(Statement stat) throws SQLException;
}
```

## which class could be interceptor?
- Executor
    - do sql operation
- ParameterHandler
    - do set sql parameter
- ResultSetHandler
    - do get sql result
- StatementHandler
    - do sql statement

## simple use with proxy env
- use case
- define a interceptor
```java
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
```
- registry interceptor
```java
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer(){
        return new ConfigurationCustomizer() {
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                log.info("MybatisConfig camel key interceptor config");
                configuration.addInterceptor(new CamelKeyResultSetInterceptor())
            }
        };
    }
```
- just it
- easy start join it.

## get mapper interface method info for process annotations
- recommend IProxyHandler extends classes under package i2f.extension.mybatis.interceptor.basics
- them provide findMapperMethod() method to find mapper interface method
- use case reference AbstractSqlLogStatementProxyHandler.class
```java
Method method=findMapperMethod(ivkObj);
// if some other framework enhance,maybe cloud not found
MybatisLog ann= ReflectResolver.findElementAnnotation(method, MybatisLog.class,true,false,false);
if(ann==null || !ann.value()){
    return null;
}
```

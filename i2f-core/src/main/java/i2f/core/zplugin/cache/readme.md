# 自动方法缓存使用简介
- 根据Cached注解指定的方法进行自动缓存代理

## 使用方法
- CacheProxyHandler 是一个ProxyHandler
- 因此直接在代理中使用即可
- 在对应需要缓存的方法上添加@Cached注解，并对这个注解所在的类进行代理即可
- 基于JDK的动态代理实现
```java
pubilc interface ICacheService{
    @Cached(binds = {
        @DataBind(value = "",bind = "0")
    },expire = 300,unit = TimeUnit.SECONDS)
    User getUserById(String userId);
}
```
- 这样，对于方法getUserById就被赋予了缓存的能力，过期时间为300秒，绑定了参数userId作为缓存key的一部分
- 接下来使用JDK动态代理获得代理对象
```java
ICacheService service=JdkProxyUtil.proxy(new CacheServiceImpl(),new CacheProxyHandler());
service.getUserById("1");
```
- 同样的，在AspectJ中，使用对应的AspectJ的代理工具类，同样能够实现
```java
public static Object inject(ProceedingJoinPoint pjp) throws Throwable{
    AspectjProxy proxy=AspectjUtil.aop(pjp,new CacheProxyHandler());
    return proxy.invoke(pjp);
}
```
- 对于一些场景下，一个方法的执行，会导致某些缓存需要失效
- 比如，更新了商品信息，商品信息获取的缓存就需要失效
- Cached注解提供了cleans属性，来指定要清除哪些缓存的前缀

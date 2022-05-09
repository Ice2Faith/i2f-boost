# 自动注入变量使用简介
- 根据Injects注解指定的fields字段
- 使用InjectProvider从一些列的IInjectFieldProvider中获取值，注入到被注解的变量中
- 其中，InjectProxyHandler提供了基于代理的注入驱动能力

## 应用场景
- 自动从环境中添加创建时间、创建人信息
- 自动从环境中添加修改时间、修改人信息

## 如何使用
### 基于JDK动态代理方式
- 基于JDK动态代理方式，此时@Injects注解需要加载接口上
- 首先是一个接口，
- 在接口上说明了，
- 使用环境中的testBeanProvider这个provider来提供数据进行对TestBean这个入参进行变量注入
- 注入的变量有sex,age,account三个字段，
- 因此这个入参的这三个字段将会被更新
```java
public interface ITest {
    
    String bean(@Injects(fields = {"sex","age","account"},providerNames = "testBeanProvider") TestBean obj);
}
```
- 接口的实现类不再给出
- 下面是如何调用
- 首先使用了静态方法，获取了一个全局的provider对象
- 然后，在其中注册了testBeanProvider这个数据提供类，上面的接口中，使用了这个名称
- 接着，实话化一个实现类的同时，使用InjectProxyHandler进行代理
- 然后，实例化调用参数，然后为它设置了account的值
- 在我们注册的provider中，并没有对account做处理，所以返回了null
- 因此，在代理调用之后，sex="男",age=12,account=null
- 注意，这里的account为重置为null了
```java
    InjectProvider provider = InjectProvider.provider();
        provider.registry("testBeanProvider",new IInjectFieldProvider() {
            @Override
            public Object getValue(String fieldName) throws Exception {
                if("sex".equals(fieldName)){
                    return "男";
                }
                if("age".equals(fieldName)){
                    return 12;
                }
                return null;
            }
        });

        ITest test=new JdkProxyProvider().proxy(new Test2Impl(),new InjectProxyHandler(provider));

        TestBean bean=new TestBean();

        bean.setAccount("user");
        test.bean(bean);
```

### 基于AOP的方式
- 基于AOP时，大体流程时一致的
```java
public static Object inject(ProceedingJoinPoint pjp) throws Throwable{
    AspectjProxy proxy=proxy(new InjectProxyHandler(InjectProvider.provider()));
    return proxy.invoke(pjp);
}
```
- 所以，在此时，就可以正常的AOP调用了
- 同时为它设置所有的provider即可

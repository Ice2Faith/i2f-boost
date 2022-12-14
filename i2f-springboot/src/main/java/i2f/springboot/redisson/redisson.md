# redisson 客户端
---

## 简介
- 是一个redis客户端
- 但是引入了其他的高级功能
- 比如grid,lock等功能
- 其中的分布式锁是开发中比较常用的

## 使用
- 这一个版本需要和springboot版本对应
- 这里用的springboot版本如下
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.7.RELEASE</version>
    <relativePath/>
</parent>
```
- 引入maven
- 注意，需要和springboot版本对应
```xml
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-boot-starter</artifactId>
    <version>3.14.0</version>
</dependency>
```
- 移除原来的maven
- 原因是已经包含了这个原来，没必要重复引入
- 重复引入还可能版本冲突
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
- 版本配置可以在maven搜索网站查看对应的 Compile Dependencies
- 例如这个版本的依赖中，就明确了适用于 spring-boot-starter-data-redis 2.3.x 版本
Compile Dependencies (5)
-----------------------------------------------------------------------------------------
| Group / Artifact   |	Version  |	Updates  |
| --- | --- | --- |
org.redisson / redisson |	3.14.0 |	3.18.1 |
org.redisson / redisson-spring-data-23 |	3.14.0 |	3.18.1 |
org.springframework.boot / spring-boot-starter-actuator |	2.3.4.RELEASE |	3.0.0 |
org.springframework.boot / spring-boot-starter-web1 vulnerability |	2.3.4.RELEASE |	3.0.0 |
org.springframework.boot / spring-boot-starter-data-redis |	2.3.4.RELEASE |	3.0.0 |
-----------------------------------------------------------------------------------------

- 添加配置
- 配置和原来的一样，可以不变
 -但是min-idle建议不要设置为0，可能启动报错
```yaml
spring:
  redis:
    host: 127.0.0.1
    password: ''
    port: 6379
    database: 0
    lettuce:
      pool:
        max-active: 8   #最大连接数据库连接数,设 0 为没有限制
        max-idle: 8     #最大等待连接中的数量,设 0 为没有限制
        max-wait: -1ms  #最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。
        min-idle: 1     #最小等待连接中的数量,设 0 为没有限制
      shutdown-timeout: 100ms
```
- 将本包及子包下面的内容放到自己的项目中

## 本包使用
- RedissonConfig 配置了 RedissonClient 
- RedissonLockProvider 提供了几种获取分布式锁的方式
- RedissonLockAop 提供了方法注解方式，进行方法的锁操作
- RedisLock 提供了互斥锁的方法注解
- RedisReadLock 提供了读锁的方法注解
- RedisWriteLock 提供了写锁的方法注解
- 使用示例
- 直接获取锁方式
```java
@Autowired
private RedissonLockProvider lockProvider;

public void lockMethod(){

    RLock lock=lockProvider.getLock(TestServiceImpl.class,"unique_no");
   
    lock.lock();

    try{
     ...
    }finally{
        lock.unlock();
    }
}
```
- 使用方法注解方式
```java
@RedisLock(clazz=TestServiceImpl.class,value="unique_no")
public void lockMethod(){
    ...
}
```

- 关于AOP中注解的使用说明和AOP定义如下
```java
/**
 * 实现基于AOP的Redisson分布式锁机制
 * 解析方法上的 RedisLock RedisReadLock RedisWriteLock 注解
 * 实现自动获取锁，自动释放锁，避免代码中添加不必要的try...finally...嵌套
 * 注解的参数都是四个：
 * value 分布式锁的key
 * keyIdx 函数入参的第几个参数，参与锁的一部分
 * clazz 分布式锁的类锁前缀
 * classify 分布式锁的类前缀，是否为方法的声明类
 * timeout 分布式锁的自动超时时间
 * unit 超时单位
 * 逻辑：
 * 方法上允许重复着三个注解
 * 生效的优先级为：
 * RedisLock > RedisWriteLock > RedisReadLock
 * 锁键的生成规则：
 * [锁键]=[限定类]+":"+[限定值]
 * [限定类]和[限定值]都可以无值
 * 当[限定类]有值，[限定值]无值，[锁键]=[限定类]
 * 当[限定类]无值，[限定值]有值，[锁键]=[限定值]
 * 当[限定类]有值，[限定值]有值，[限定类]+":"+[限定值]
 * 当[限定类]无值，[限定值]无值，[锁键]=[方法声明类名]+":"+[方法名]
 *
 * [限定类]=clazz
 * 当clazz==Object.class,表示clazz无值
 * 当classify==true && clazz==Object.class，表示clazz值应该为method.getDeclaringClass()
 * 其他情况，[限定类]就是clazz的值
 *
 * [限定值]=value+":"+args[keyIdx]
 * 当value==""，表示value无值
 * 当keyIdx<0 || keyIdx>=args.length，表示keyIdx无值
 * 当value有值，keyIdx无值，[限定值]=value
 * 当value无值，keyIdx有值，[限定值]=args[keyIdx]
 * 当value有值，keyIdx有值，[限定值]=value+":"+args[keyIdx]
 * 当value和keyIdx均为无值，[限定值]=无值
 *
 * 如果超时timout大于0，则按照超时设置锁超时
 * 否则，按照默认
 *
 * 下面是注解的使用和说明，假定都是在com.test.TestController类中的test方法上使用
 * -------------------------------------------------------
 * @RedisLock(value="app")
 * 锁键=app
 * 等价于，全局锁，应用锁
 *
 * @RedisLock(classify = true)
 * 锁键=com.test.TestController
 * 等价于，类锁
 *
 * @RedisLock(classify = true,value="unique")
 * 锁键=com.test.TestController:unique
 * 等价于，类锁，类部分锁
 *
 * @RedisLock(clazz=User.class)
 * 锁键=com.test.model.User
 * 等价于，类锁，锁的类可以为其他类
 *
 * @RedisLock(clazz=User.class,value="unique")
 * 锁键=com.test.model.User:unique
 * 等价于，类锁
 *
 * @RedisLock(value="unique",keyIdx = 0)
 * 假设入参第0个为userId，此时的值为1001
 * 锁键=unique:args[0]=unique:userId=unique:1001
 * 等价于应用分区锁，这个示例中，就是按照用户分区锁
 * 
 * @RedisLock(classify=true,value="unique",keyIdx = 0)
 * 假设入参第0个为userId，此时的值为1001
 * 锁键=com.test.TestController:unique:args[0]=com.test.TestController:unique:userId=com.test.TestController:unique:1001
 * 等价于类分区锁，这个示例中，就是按照用户使用类分区锁
 * 
 * @RedisLock
 * 锁键=com.test.TestController:test
 * 等价于，方法锁，锁的是这个方法
 */
@Slf4j
@Aspect
@Component
public class RedissonLockAop {
    @Pointcut("@annotation(org.jeecg.config.redisson.annotation.RedisLock)" +
            "|| @annotation(org.jeecg.config.redisson.annotation.RedisReadLock)" +
            "|| @annotation(org.jeecg.config.redisson.annotation.RedisWriteLock)")
    public void lockPointCut() {
    }

    ...
}
```

## 注意事项
- 原来的RedisConfig等对redis的配置可以不变
- 其他注意的点
- 原来的配置中可能用到了LettuceConnectionFactory或者JedisConnectionFactory
- 对应的换为RedissonConnectionFactory即可
- 如果没有明确需要为Lettuce或者Jedis类型的ConnectionFactory的地方
- 可以全部使用接口类RedisConnectionFactory替换
- 因为RedisConnectionFactory是lettuce/jedis/redisson各自ConnectionFactory的父类
- 针对原来从Lettuce等中获取redis配置的
- 可以改变为注入RedisProperties对象获取配置
- 例如，获取host,port
- 判断是否为集群配置，可以判断 RedisProperties.getCluster() 是否为空

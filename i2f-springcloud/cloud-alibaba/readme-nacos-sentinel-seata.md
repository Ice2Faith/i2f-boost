# cloud-alibaba 需要的服务端组件

---
## 介绍
- nacos是基于CS模式的，也就是各个微服务都是客户端，向nacos的服务端注册和获取配置信息
    - 下载：https://github.com/alibaba/nacos/releases
- sentinel也是基于CS模式的，但是没有那么强制需要服务端，服务端指示能够更好的进行可视化配置
    - 下载：https://github.com/alibaba/Sentinel/releases
---
## nacos-server
- 对于nacos而言，提供了.zip包和.tar.gz包
- 实际上，两个包都是需要java环境运行的
- 所以，只要你能解压.tar.gz包，那么在windows环境下也是可以使用的
- 那么为什么还需要两个包呢？
- 原因
- .tar.gz包可以包含权限信息
- 在linux下想要执行一个程序或者脚本，是需要可执行权限的
- 而在Windows下，双击就行了

### 启动
- 直接解压进入bin路径启动即可
- 但是，默认是集群模式的
- 所以，单击使用的话，启动模式需要修改一下
- 进入路径nacos/bin
```
cd nacos/bin
```
- 编辑启动脚本
```
vi starup.sh
```
- 找到如下模式行
```
set MODE="standalone"
```
- 可选的值
```
cluster 集群
standalone 单机
```
- 在脚本后面的判定中可以找到，直接复制就行
- 运行启动脚本即可
- 直到出现如下的日志行，则启动完毕
```
2022-05-28 20:58:52,151 INFO Nacos started successfully in stand alone mode. use embedded storage
```
- 同时就可以进入浏览器访问了
- 日志中有输出访问路径
```
 Console: http://localhost:8848/nacos/index.html
```
- 默认的用户名和密码
```
nacos/nacos
```


### 配置存入数据库
- 默认的配置数据和登录信息存放在内存中
- 因此，如果有需要，可以配置到数据库中
- 配置到数据库中，也才能实现集群
- 进入路径nacos/conf
```
cd nacos/conf
```
- 编辑配置文件
```
vi application.properties
```
- 找对入下行，进行对应的数据库信息配置即可
```
#*************** Config Module Related Configurations ***************#
### If use MySQL as datasource:
# spring.datasource.platform=mysql

### Count of DB:
# db.num=1

### Connect URL of DB:
# db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
# db.user.0=nacos
# db.password.0=nacos
```
- 同时，需要对应的建库建表，这个建表脚本在nacos-server中有给出

### 开启权限管理
- 默认情况下，client访问nacos的配置文件中，是不需要指明用户名和密码的
- 但是可以配置开启，开启之后，client中的配置文件就需要指明用户名和密码
- 找到入下行开启即可
```
#spring.security.enabled=false

nacos.core.auth.enabled=false
```

### 集群部署
- 前面说了改成单机模式的方式
- 对应改成集群的方式就不再给出，同时注意下虚拟机堆大小，默认的配置有点大
- 集群，就需要数据一致，所以就需要数据库模式
- 上面有说到，怎么改为数据库的方式

- 集群配置，主要的就是集群需要知道自己集群有哪些主机
- 所以需要修改配置
-进入配置路径nacos/conf
```
cd nacos/conf
```
- 复制模板配置文件
```
cp cluster.conf.example cluster.conf
```
- 编辑集群配置文件
```
vi cluster.conf
```
- 保存重启即可
- 对于集群的nacos-server来说
- 一般需要一个负载均衡器在前面支撑
- 一般使用nginx
- 配置nginx进行负载均衡
```
http {
   upstream nacos-cluster {
        server 127.0.0.1:8850;
        server 127.0.0.1:8851;   
   }

    server {
        listen  8849;
        server_name localhost;

        location /nacos/{
            proxy_pass http://nacos-cluster/nacos/;
        }
    }

}
```


---
## sentinel
- 主要用于流控，降级，熔断
- 包含核心库和管理面板两部分，核心库不依赖管理面板
- 可以通过代码的方式进行对资源进行流控和降级
- 也可以通过对应的sentinel-dashboard管理界面进行资源的控制
- 一般情况下，结合nacos实现对流控等规则的持久化
### 直接使用核心库方式编写规则
- 核心库依赖
```xml
<!-- 核心依赖，就已经可以单独使用了 -->
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-core</artifactId>
    <version>1.8.0</version>
</dependency>
<!-- 使用aspectj来支持@SentinelResource注解，简化API使用 -->
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-annotation-aspectj</artifactId>
    <version>1.8.0</version>
</dependency>
```
- 首先，定义规则
```java
// 定义一个资源，这个资源一般就是一个接口，但是也可以是一个方法
String REOURCE_NAME="api/test";

// 流控规则组
List<FlowRule> rules=new ArrayList<>();

// 定义一个流控规则
// 这里定义一个根据QPS进行限流的规则
FlowRule rule=new FlowRule();
rule.setResource(RESOURCE_NAME);
rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
rule.setCount(3);

rules.add(rule);

// 注册规则
FlowRuleManager.loadRules(rules);
```
- 接着，业务中使用规则
```java
// 定义一个资源，这个资源一般就是一个接口，但是也可以是一个方法
String REOURCE_NAME="api/test";

Entry entry=null;
try{
    entry=SphU.entry(REOURCE_NAME);
    // 常规业务逻辑
}catch(BlockException e){
    // 被流控逻辑
}
```
### 使用注解简化规则
- 相比较与直接使用API方式，注解方式简化了一些
- 但是依旧需要通过代码方式指定资源的规则
- 直接使用的话，需要将解析 @SentinelResource 注解的切面处理类，添加到环境中
```java
@Bean
public SentinelReourceAspect sentinelResourceAspect(){
    return new SentinelResourceAspect();
}
```
- 业务中使用
-`通过value指定资源名称
- 通过blockHandler指定流控后的处理方法
- 也就是说，处理方法需要在同一个类中，并且最后一个入参必须为：BlockException
- 其余参数和接口参数一致，返回值也要一致，并且为public
- 也可以是在其他类中，但必须为public static，同时注解参数需要通过blockHandleClass指定类
- 通过BlockException的具体类型，可以判定为不同的规则触发的控制
```java
@RequestMapping("hello")
@SentinelResource(value="api/hello",blockHandler="helloApiBlockHandlerMethod")
public Object hello(String name){
    return 1;
}

public Object helloApiBlockHandlerMethod(String name,BlockException e){
    return 0;
}
```
### 降级规则的代码方式
```java

List<DegradeRule> rules=new ArrayList<>();

// 设置一个在10秒内出现两次异常就触发降级的规则，但是最少2次请求才会触发
DegradeRule rule=new DegradeRule();
rule.setResource("api/hello");
rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
rule.setCount(2);
// 半开时长，在半开期间，如果还是失败，则直接降级
rule.setTimeWindow(10);
rule.setMinRequestAmount(2);
// 统计时长
// rule.setStatIntervalMs(3*1000);

rules.add(rule);

DegradeRuleManager.loadRules(rules);
```
### 使用dashboard方式进行规则控制
- 相较于在代码中编写资源规则的方式，这种方式更加友好
- 毕竟通过界面配置，结合springmvc自动识别接口作为资源
- 能够达到不使用注解，不编写规则代码方式，就能够实现流控
- 但是，资源不会立即出现在dashboard中，需要有请求触发才会出现
- 启动dashboard
```
java -jar sentinel-dashboard-1.8.1.jar
```
- 默认启动在8080端口
```
http://localhost:8080/
```
- 默认用户名/密码
```
sentinel/sentinel
```
- 修改默认端口
- 方式一：直接解压jar包修改application.properties文件中的配置
- 方式二：通过启动参数覆盖
```
java -Dserver.port=7799 -jar sentinel-dashboard-1.8.1.jar
```
- 两种方式都可以

---
### sentinel规则讲解
- 对于流控之后的响应信息
- 一般有两种方式，一种是使用@SentinelResource中的blockHandler进行处理
- 另外一种则是使用全局的异常处理

---
#### 流控规则
- QPS
    - 每秒可处理的请求数
    - 单用户疯狂刷新就是一个场景
- 线程数
    - 能够同时处理的线程数
    - 多个主机同时请求一个耗时接口，导致线程长时间占用
    - 后续线程则需要限流
- 高级配置
    - 流控模式
        - 直接（默认）
            - 直接对资源本身限制
        - 关联
            - 如果关联的其他规则流控触发了，则被流控的是自己
        - 链路
            - 如果自身触发了流控规则，则被流控的是入口规则
            - 默认情况下，链路是不会自动建立的
            - 需要自己开启
            - spring.cloud.sentinel.web-context-unify=false
    - 流控效果
        - 快速失败（默认）
            - 直接失败处理
        - Warm Up
            - 预热模式，逐步一批量失败，而不是全部直接失败
            - 主要用于洪峰激增流量，缓存还没建立的情况
            - 这样逐步放开，缓存也逐步建立
        - 排队等待
            - 将请求在一定的超时时间允许条件下，先排队，如果未超时，则可以处理
            
---
#### 降级规则
- 强依赖失败整个不可用
- 若依赖失败则可以通过补偿手段补偿
- 慢调用比例
    - 最大RT
        - 超过这个毫秒数就算慢调用
    - 比例阈值
        - 指定达到多少的比例触发熔断
    - 熔断时长
        - 一次熔断多长时间
    - 最小请求数
        - 最小请求多少次才触发
- 异常比例
    - 比例阈值
        - 指定达到多少的比例触发熔断
    - 熔断时长
        - 一次熔断多长时间
    - 最小请求数
        - 最小请求多少次才触发
- 异常数
    - 异常数
        - 指定达到多少的异常数触发熔断
    - 熔断时长
        - 一次熔断多长时间
    - 最小请求数
        - 最小请求多少次才触发

---
#### 热点规则
- 可以针对部分数据进行控制
- 而不是面向接口的全部数据
- 必须使用注解@SentinelResource的方式声明资源
- 参数索引
    - 第几个参数是热点参数
- 单击阈值
    - 单击访问的QPS
    - 区分大部分的是热点还是大部分是一般的
- 统计窗口时长
    - 就是几秒内的QPS
- 再次编辑
    - 参数类型
    - 参数值
        - 少数数据的数据值
    - 限流阈值
        - 这些少数数据的阈值

---
#### 系统规则
- 可以设定系统性能的保护规则，避免系统宕机
- 接口太多，不可能都指定了规则
- 系统环境原因，硬件资源情况变化，都是意料之外的情况
- 因此，一个系统兜底的防护规则是必须的
- 阈值类型
    - LOAD
    - RT
    - 线程数
    - 入口QPS
    - CPU使用率
    
    
### 统一异常处理
- 实现接口 BlockExceptionHandler 接口，并且注册到环境中
- 这里只会处理被流控的异常，其他异常不会被处理
- 一般还要配合springmvc的全局异常处理一起使用

### 对openfeign的降级
- 变更配置
```
feign.sentinel.enabled=true
```
- 其余的fallback在openfeign中的使用不变

### 规则的持久化nacos
- 第一种方式，直接在代码中写规则
- 第二种，以JSON方式配置在nacos中
- 这里以第二种为例
- 首先，引入依赖
```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
```
- 其次，在nacos中写入规则的JSON数据
- 但是，不支持在管理面板中修改后，同步修改到nacos
- 需要更变源码方式实现
- 这是一个示例
```json
[
    {
        "resource":"api/hello",
        "controlBehavior": 0,
        "count": 10.0,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0
    }
]
```
- 在配置文件中添加nacos的配置
```yaml
spring:
  cloud:
    sentinel:
      datasource:
        # 规则ID，可以自定义
        flow-rule:
          server-addr: 127.0.0.1:8847
          username: naocs
          password: nacos
          data-id: flow-rule.json
          namespace: public
          data-type: json
          rule-type: flow
```

### 与gateway的整合
- 引入依赖
```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-sentinel-gateway</artifactId>
</dependency>
```
- 其余使用基本不变


---
## seata
- 同样采用CS模式，使用server作为事务协调者，实现2PC的事务管理
- 使用AT模式（auto-transactional）提供声明式事务
- 对比spring的声明式事务，使用基本一致
- @GlobalTransactional 注解来进行事务管理
### 服务端部署
- 默认文件存储信息
#### 改为使用数据库
- 进入配置文件
```
cd  seata/conf
```
- 编辑file.conf
```
vi file.conf
```
- 找到模式行
```
mode = "file"
```
- 改为db
```
mode = "db"
```
- 对应找到db节点
- 修改对应的数据库连接信息即可
- 建立对应的库和表
- 数据库初始化脚本位置
```
https://github.com/seata/seata/tree/develop/script/server/db
```
- 本地已经下载保存为对应的文件
```
seata-server-init-db-mysql.sql
seata-server-init-db-oracle.sql
```


#### 使用nacos的分布式服务发现能力
- 分布式事务是需要和其他服务一样
- 在注册中心中注册与发现其他服务
- 同时也有利于进行集群部署
- 实现分布式事务
- 进入配置目录
```
cd seata/conf
```
- 编辑文件
```
vi registry.conf
```
- 找到模式行
```
type = "file"
```
- 改为
```
type = "nacos"
```
- 修改对应的nacos节点的配置即可

#### 初始化nacos对应的配置
- 在官网下载响应的脚本和文件
- 已下载到seata-docs中
- 修改seata-docs/config.txt
- 找到如下行
```
#Transaction storage configuration, only for the server. The file, DB, and redis configuration values are optional.
store.mode=file
store.lock.mode=file
store.session.mode=file
#Used for password encryption
store.publicKey=
```
- 更改为db模式
```
#Transaction storage configuration, only for the server. The file, DB, and redis configuration values are optional.
store.mode=db
store.lock.mode=db
store.session.mode=db
#Used for password encryption
store.publicKey=
```
- 对应db部分的信息对应修改即可
- 事务分组概念
```
#Transaction routing rules configuration, only for the client
service.vgroupMapping.default_tx_group=default
```
- 注意这里的这个分组名称default_tx_group，在客户端也会用到
- 两边需要一致
- 可以按照不同的机房来区分事务分组，以便于切换分组，实现异地容灾
```
#Transaction routing rules configuration, only for the client
service.vgroupMapping.guangzhou=default
```
- 如果改成这样，那么事务分组名称就为guangzhou
- 并且这个值必须等于注册中心中的cluster的值
- seata/conf/registry.conf-->nacos:cluster
- 将配置文件同步到nacos中
- 进入seata-docs/nacos
- 运行nacos-config.py或者nacos-config.sh脚本
- 即可将上层目录seata-docs/中的config.txt保存到nacos中
- 默认脚本中的指向为本机中默认端口的nacos
- 如果需要修改，可以修改脚本或者通过脚本参数指定

- 启动seata-server即可
- 其中有一些可选参数
- 指定端口 -p
- 指定日志 -m [file|db|redis]

### 客户端搭建
- 在客户端使用的时候，使用AT模式的情况下，客户端也需要响应的表
```
https://github.com/seata/seata/tree/develop/script/client/at/db
```
- 本地也已经下载
```
seata-at-client-init-db-mysql.sql
seata-at-client-init-db-oracle.sql
```

- 修改配置
- 修改事务分组的配置
```yaml
spring:
  cloud:
    alibaba:
      seata:
        tx-service-group: default_tx_group
```
- 这个值就是之前在服务端seata-docs/config.txt中的这个属性的名称
```
#Transaction routing rules configuration, only for the client
service.vgroupMapping.default_tx_group=default
```
- 就是紧跟service.vgroupMapping的名称，这里就是default_tx_group

- 修改配置，增加与server端的连接配置
- 注意，根配置就是seata,不要加以为在spring.cloud.alibaba.seata下面
```yaml
# 配置seata-server信息
seata:
  # 注册中心
  registry:
    type: nacos
    nacos:
      # seata-server所在的nacos主机，服务名，用户名，密码，分组
      server-addr: 127.0.0.1:8848
      application: seata-server
      username: nacos
      password: nacos
      group: SETA_GROUP
  # 配置中心
  config:
    type: nacos
    nacos:
      # 配置所在的nacos信息
      server-addr: 127.0.0.1:8848
      username: nacos
      password: nacos
      group: SETA_GROUP
```
- 在业务方法中添加事务注解即可
- @GlobalTransactional

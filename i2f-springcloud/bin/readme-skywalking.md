# SkyWalking 链路跟踪
- 国人于2015年基于agent增强技术实现的无侵入式的链路跟踪组件
- 后来开源后进入apache维护孵化器
- 相比较于sleuth+zipkin这种侵入式的方式，显然是一个更好的方式
- 另一方面，从性能上来说，优于zipkin
- 同时，也支持多种持久化方式，db/es

- 同样的，也是一个基于CS模式的组件
- server负责数据的收集和信息报表化展示
- client负责数据的采集和上传

## 服务端启动
- 进入目录
```
cd apache-skywalking-apm-bin-es7/bin
```
- 启动脚本
```
./startup.sh
```
- 即可在浏览器访问
```
http://localhost:8080
```
- 默认的端口
    - UI界面：8080
    - 信息收集：11800
    - UI接口：12800


## 客户端启动
- 前面说了，使用的是agent技术
- 因此在项目中是不会使用的
- 而是在项目启动时，使用javaagent增强
- 因此也是无侵入的原因
- 这里以启动给一个test-svc.jar服务为例
- 命令,分别指定agent-jar,服务名，上报主机地址
```
java -javaagent:/apache-skywalking-apm-bin-es7/agent/skywalking-agent.jar -DSW_AGENT_NAME=test-svc -DSW_AGENT_COLLECTOR_BACKEND_SERVICES=127.0.0.1:11800 -jar test-svc.jar
```

### 持久化与nacos接入配置
- 进入配置路径
```
cd apache-skywalking-apm-bin-es7/config
```
- 编辑文件
```
vi application.yml
```
- 持久化到数据库
- 找到行
```
storage:
  selector: ${SW_STORAGE:h2}
```
- 改变h2为mysql
```
storage:
  selector: ${SW_STORAGE:mysql}
```
- 找到下面的mysql节点，修改为对应的数据库配置即可
- 表会自动建立，不需要关注
- 同时需要把mysql的驱动jar包放到oap-libs下面

- 配置使用nacos
- 找到行
```
configuration:
  selector: ${SW_CONFIGURATION:none}
```
- 改变none为nacos
```
configuration:
  selector: ${SW_CONFIGURATION:nacos}
```
- 找到下面的nacos节点，修改为对应的nacos配置即可


## 自定义链路跟踪
- 默认不会去记录执行的方法的链路，也就是只保留了网络请求部分
- 通过自定义链路方式，可以将普通方法加入到链路中
- 同时也能够支持入参和返回值的记录

- 添加依赖
```xml
<dependency>
    <groupId>org.apache.skywalking</groupId>
    <artifactId>apm-toolkit-trace</artifactId>
    <version>8.6.0</version>
</dependency>
```
- 在需要追踪的方法上添加注解 @Trace 即可
- 在方法上添加 @Tag/@Tags 注解可以实现入参返回值的监控
- key 返回值时，一般写为方法名，为入参时，一般为参数名
```java
@Trace
@Tag(key="hello",value="returnedObj") // 记录hello返回值
//@Tag(key="hello",value="arg[0]") // 记录hello第一个入参
public Object hello(String id){
    return "";
}
```

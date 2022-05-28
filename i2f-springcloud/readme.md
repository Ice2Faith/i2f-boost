# spring-cloud 专题

## 简介
- 发展史
### 概念阶段
- spring-framework 团队给出springcloud概念
### 第一个具体实现方案
- 最初netflix给出了最初的具体实现体系
    - eureka 注册中心
    - zuul 网关
    - ribbon 负载均衡
    - openfeign 服用调用
    - hytrix 熔断
    - seluth 链路跟踪
    - zipkin 链路跟踪
- spring-framework团队进行补充
    - actuator 监控
    - actuator-admin 监控面板
    - bus 消息总线
    - config 配置中心
- 在这一套方案中，以netflix组建为主，动态配置中心结合git与bus实现动态配置的刷新
- 但是，好景不长，netflix进行了部分组建的闭源
### 第二个具体实现方案
- spring-framework 团队给出了部分的替代方案
    - gateway 替换 zuul
    - loadbalance 替换 ribbon
### 目前系统化的主流实现方案
- 紧接着，alibaba团队给出了一套系统化的开源方案
    - nacos 注册中心，配置中心
    - sentinel 流控，熔断，性能感知
    - skywalking 链路跟踪
    - seata 分布式事务管理
- 同时结合spring-framework团队中的gateway和loadbalance，整个生态进行了完善
- 同时，alibaba团队将项目给到了孵化器中进行孵化，正式进入spring-cloud中，完全开源
- 虽然netflix部分闭源，但是其中的ribbon和openfeign都是常用的
- 因此，并没有闭源，所以，目前主流的还是使用ribbon和openfeign
- 主要是由于loadbalance目前的情况不太稳定

## 模块说明
- 基于上面的简介，所以能够得出spring-cloud-alibaba是目前主流，完善的微服务体系实现方案
- 因此，本模块就是以cloud-alibaba为主
- 涉及的内容如下
    - nacos 注册中心，配置中心
    - sentinel 流控，熔断，性能感知
    - skywalking 链路跟踪
    - seata 分布式事务
    - gateway 网关
    - ribbon 负载均衡
    - loadbalance 负载均衡
    - openfeign 服务调用
    
- 对于actuator监控，在sentinel或者nacos中具有一定的功能性
- 虽然不如actuator全面，但是就不再特别给出

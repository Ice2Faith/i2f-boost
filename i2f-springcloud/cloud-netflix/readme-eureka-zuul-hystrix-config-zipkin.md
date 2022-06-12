# cloud-netflix 需要的服务端组件

---
## 介绍
- springcloud-netflix 是最早的springcloud实现
- 由netflix提供部分主要的实现
- 但是好景不长，netflix对部分组件进行了闭源
- 因此这一套基本已经很少使用
- 转而使用springcloud-alibaba了

## 构成
- eureka 作为注册中心和服务发现
- config 作为配置中心
- zuul 作为网关
- ribbon 作为负载均衡
- hystrix 作为熔断降级
- sleuth 作为跟踪
- zipkin 作为链路

## 一般的运行模式
- 启动eureka-server注册中心等待服务注册
- 启动config-server作为配置中心，并注册到注册中心
- 启动actuator-server监控中心，捕获各个服务健康状态
- 启动zuul网关，web入口都从这里经过
- 启动zipkin服务端，用于链路跟踪
- 启动各个微服务模块，都注册到注册中心，并且从配置中心拉取配置
- 服务之间调用使用feign
- 均衡使用ribbon
- 降级使用hystrix
- 也就是如此，maven中的依赖引入顺序也是要对应的
- 先引入eureka-client再引入config
- 否则将会拉取不到配置，从而启动失败

- 对于动态拉取配置，需要结合bus和mq以及git进行实现
- 使用比较复杂，已经不建议使用了

## 使用简介
- 对于这一套中，不少组件都存在两个依赖，即server和client
- 对应的client向对应的server上报或者拉取信息
- 遵循一个原则：
- 所有的服务，都需要向注册中心注册
- 所有的服务，都需要上报健康状态
- 所有的服务，都尽量从配置中心拉取配置
- 这个原则，也是整个微服务的基本原则
- 当然，也不是必须的，因为微服务没有做强制要求


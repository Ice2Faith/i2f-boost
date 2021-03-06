# i2f-boost
boost java develop
## what's this project?
- a set tools for java develop
- provide simple api class to use
## how to use?
- download /bin/deploy-{version}/*.jar
- add to your project
- just use it.
## introduce
- i2f-boost
    - i2f-agent
    - i2f-core
    - i2f-core-j2ee
    - i2f-extension
    - i2f-spring
    - i2f-springboot
    - i2f-springcloud
    - i2f-test
### i2f-boost
- project root,manage maven version and module relation
### i2f-core
- just rely on jdk8
- provide basic power
- includes
    - reflect/value visitor
    - agent 
    - package scan
    - jdbc/jdbc wrapper
    - lambda/method reference naming
    - jce
    - thread/thread pool/fork join
    - lock
    - collection/map/array
    - proxy/dynamic proxy
    - check
    - validate
    - command
    - compress/zip
    - tree/list convert
    - api bean
    - date/calendar
    - db reverse engineer
    - escape
    - file
    - bloom filter
    - hash
    - regex generator
    - json/xml implements
    - multi-lang/i18n like
    - limiter
    - simple/ant matcher
    - tcp/udp/http server/client
    - nio file/tcp/udp
    - properties
    - resource loader
    - rmi invoke
    - token manager
    - serialize
    - string/appender
    - checksum/encrypt/copy stream
    - stack trace
    - type token
    - unsafe
    - cache/log/databind/inject/log/validate proxy handler
### i2f-core-j2ee
- rely on i2f-core
- provide servlet power
- includes:
    - file upload/download
    - servlet context
    - forward/redirect
    - http header
    - web token implement
### i2f-extension
- rely on i2f-core , i2f-core-j2ee
- provide multi-third package use power
- includes:
    - javasssist
    - aspectj/aspectj proxy handler
    - compress/zip/tar
    - elasticsearch manager/query
    - email sender
    - ftp/sftp
    - hdfs
    - httpclient/http provider
    - fastjson/gson
    - jstorm invoker
    - mybatis typeHandler/interceptor, page-helper
    - netty for http
    - cglib/cglib proxy handler
    - qrcode make and parse
    - quartz manage
    - redis client/pool/template
    - velocity template engine
    - kaptcha verifycode
    - easyexcel import/export
    - dom4j xml
    - zookeeper manager
    - maven package set
### i2f-spring
- rely on i2f-core, i2f-core-j2ee
- provide spring-core,spring-context,spring-bean,spring-mvc,spring-security power
- includes:
    - spring context
    - environment
    - event drive
    - json/jackson
    - log/slf4j
    - spring-mvc/file
    - param naming
    - http proxy pass
    - resource find
    - class scan
    - security context
    - spel parse
### i2f-springboot
- rely on i2f-core,i2f-core-j2ee,i2f-extension,i2f-spring
- provide annotation drive enable functions power
- includes:
    - activiti7 @EnableActivityConfig
    - request/response encrypt/decrypt @EnableHttpWebAdviceConfig
    - war application boot
    - asyn @EnableAsynConfig
    - cors @EnableCorsConfig
    - multi-datasource @EnableDynamicDatasourceConfig
    - elasticsearch manage @EnableElasticsearchConfig
    - aop log,param spy,public exception,object mapper @EnableMvcConfig
    - mybatis mapper scan, log,calmel key,page helper @EnableMybatisConfig
    - property decrypt @EnablePropertyDecryptConfig
    - quartz scan startup @EnableQuartzConfig
    - redis cache/serializer @EnableRedisConfig
    - dynamic context refresh @EnableRefreshConfig
    - rest template @EnableRestTemplateConfig
    - schedule @EnableScheduleConfig
    - spring security @EnableSecurityConfig
    - shiro auth @EnableShiroConfig
    - swagger @EnableSwaggerConfig
    - websocket @EnableWebsocketConfig
    - plugin dict config @EnableConfigConfig
    - plugin file config @EnableFileConfig
### i2f-springcloud
- rely on i2f-core,i2f-core-j2ee
- provide spring cloud,spring cloud alibaba power
- includes:
    - nacos config/discovery @EnableNacosConfig
    - seata distribute transaction @EnableSeataConfig
    - sentinel fallback/limiter @EnableSentinelConfig
    - skywalking remote invoke trace @EnableSkyWalkingConfig
    - feign remote method invoke @EnableFeignConfig
    - ribbon http load balance @EnableRibbonConfig
    - gateway micro-service gateway @EnableGatewayConfig
    - loadbanlancer http load balance @EnableLoadBalancerConfig
    - actuator health spy @EnableActuatorClientConfig / @EnableActuatorServerConfig
    - config center @EnableConfigClientConfig / @EnableConfigServerConfig
    - sleuth trace @EnableSleuthConfig
    - zipkin link relation @EnableZipkinConfig
- includes:
    - eureka registry and discovery @EnableEurekaClientConfig / @EnableEurekaServerConfig
    - hystrix fallback @EnableHystrixConfig
    - zuul gateway @EnableZuulConfig
    

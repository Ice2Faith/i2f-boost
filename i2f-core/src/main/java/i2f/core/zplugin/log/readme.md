## design
- log proxy auto output
    - for target object proxy log output by LogProxyHandler
    - before,after,exception
    - proxy output log require method or class has @Log annotation
- log direct output
    - declare an ILogger instance and use it.
- and,inner use BroadcastLogWritter implement multiply output log when use LoggerFactory build
    - stdout
    - file
    - etc.
### use case
- so you can use like this
- by proxy
- in this part,our custom defined use BroadcastLogWriter as proxy ILogWriter
- and registry an file log writer named [file]
- so,in this proxy,BroadcastLogWriter has two writers[STDOUT,file]
- log output will have two channel
```java
System.out.println("log start-----------");
BroadcastLogWriter writer=new BroadcastLogWriter();
writer.registerLogWriter("file",new FileLogWriter(new File("./logs/boost-log.log"),true));
ITest ltest=new JdkProxyProvider().proxy(new Test2Impl(),new LogProxyHandler(writer));
ltest.say(null);
ltest.bean(null);
System.out.println("log end------------------");
```
- maybe complex
- let easy use
- use LogWriterHolder
```java
LogWriterHolder.registerLogWriter("file",new FileLogWriter(new File("./logs/factory-log.log"),true));
ITest fltest=new JdkProxyProvider().proxy(new Test2Impl(),LoggerFactory.getHandler());
fltest.say(null);
fltest.bean(null);
```
- this result same as up last one

- by direct log
- in this part,direct use LoggerFactory get an ILogger instance
- and use it output
```java
private static ILogger logger= LoggerFactory.getLogger("冰念通用组件","日志插件","日志测试");
...
logger.trace("trace","value",1);
logger.info("something","is","ok",200);
logger.warn("warn");
logger.debug("debug");
logger.fatal();
```

### how to control class log output
- provide LogDecision interface to do this job
- by DecisionLogWriterAdapter decorate an ILogWriter implements management log level output control
- and default provide an PackageClassMethodLogLevelDecision implement instance
- recommend use LoggerFactory.getDecisionLogWriter method get an decorated LogWriter

### defined your's ILogWriter
- direct implements ILogWriter to customer output an LogData bean
- direct extends AbstractPlainTextLogWriter to output log string

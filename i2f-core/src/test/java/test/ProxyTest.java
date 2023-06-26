package test;

import i2f.core.container.collection.CollectionUtil;
import i2f.core.functions.Algorithm;
import i2f.core.lang.functional.impl.SysPrintlnExecutor;
import i2f.core.lang.proxy.IInvokable;
import i2f.core.lang.proxy.IProxyHandler;
import i2f.core.lang.proxy.impl.JdkProxyProvider;
import i2f.core.zplugin.log.ILogger;
import i2f.core.zplugin.log.LogProxyHandler;
import i2f.core.zplugin.log.LoggerFactory;
import i2f.core.zplugin.log.context.LogWriterHolder;
import i2f.core.zplugin.log.impl.BroadcastLogWriter;
import i2f.core.zplugin.log.impl.FileLogWriter;

import java.io.File;

/**
 * @author ltb
 * @date 2022/3/25 20:59
 * @desc
 */
public class ProxyTest {
    private static ILogger logger= LoggerFactory.getLogger("冰念通用组件","日志插件","日志测试");

    public static void main(String[] args){
//        LogLevelMappingHolder.addMapping("*", LogLevel.ALL);
        ITest test=new JdkProxyProvider().proxy(new Test2Impl(), new IProxyHandler() {
            @Override
            public Object initContext() {
                return null;
            }

            @Override
            public Object before(Object context,Object ivkObj, IInvokable invokable, Object... args) {
                System.out.println("before...");
                System.out.println("args:");
                Algorithm.execute(CollectionUtil.arrayList(args), new SysPrintlnExecutor<>());
                System.out.println("before.");
                return "bad";
            }

            @Override
            public Object after(Object context,Object ivkObj, IInvokable invokable, Object retVal, Object... args) {
                System.out.println("after...");
                System.out.println("ret:"+retVal);
                System.out.println("after.");
                return retVal;
            }

            @Override
            public Throwable except(Object context,Object ivkObj, IInvokable invokable, Throwable ex, Object... args) {
                System.out.println("except...");
                System.out.println("ex:"+ex.getMessage()+" of "+ex.getClass().getName());
                System.out.println("except.");
                return ex;
            }

            @Override
            public void onFinally(Object context, Object ivkObj, IInvokable invokable, Object... args) {
                System.out.println("finally ...");
            }
        });
//        String ret=test.say("hello");
//        System.out.println("ivkRet:"+ret);
//
//        ITest vtest=new JdkProxyProvider().proxy(new Test2Impl(),new ValidateProxyHandler());
//        String vret=vtest.say(new TestBean());
//        System.out.println("vivkRet:"+vret);
//        vtest.bean(new TestBean());

        System.out.println("log start-----------");
        BroadcastLogWriter writer=new BroadcastLogWriter();
        writer.registerLogWriter("file",new FileLogWriter(new File("./logs/boost-log.log"),true));
        ITest ltest=new JdkProxyProvider().proxy(new Test2Impl(),new LogProxyHandler(writer));
        ltest.say(null);
        ltest.bean(null);
        System.out.println("log end------------------");

        logger.trace("trace","value",1);
        logger.info("something","is","ok",200);
        logger.warn("warn");
        logger.debug("debug");
        logger.fatal();

        LogWriterHolder.registerLogWriter("file",new FileLogWriter(new File("./logs/factory-log.log"),true));
        ITest fltest=new JdkProxyProvider().proxy(new Test2Impl(),LoggerFactory.getHandler());
        fltest.say(null);
        fltest.bean(null);
    }
}

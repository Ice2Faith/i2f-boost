package proxy;

import i2f.core.collection.Collections;
import i2f.core.functions.Algorithm;
import i2f.core.functional.impl.SysPrintlnExecutor;
import i2f.core.proxy.IInvokable;
import i2f.core.proxy.IProxyHandler;
import i2f.core.proxy.IProxyProvider;
import i2f.core.proxy.impl.JdkProxyProvider;
import i2f.extension.proxy.cglib.CglibProxyProvider;

/**
 * @author ltb
 * @date 2022/3/25 20:59
 * @desc
 */
public class ProxyTest {
    public static void main(String[] args){
        IProxyHandler handler=getProxyHandler();
        IProxyProvider provider=new JdkProxyProvider();
        Object proxy=new Test2Impl();
        System.out.println("jdk proxy");
        testProxy(provider,proxy,handler);

        System.out.println("---------------------------");
        provider=new CglibProxyProvider();
        proxy=Test2Impl.class;
        System.out.println("cglib proxy");
        testProxy(provider,proxy,handler);
    }

    private static void testProxy(IProxyProvider provider,Object proxy,IProxyHandler handler){
        ITest test=provider.proxy(proxy, handler);
        String ret=test.say("hello");
        System.out.println("ivkRet:"+ret);
    }

    private static IProxyHandler getProxyHandler(){
        return new IProxyHandler() {
            @Override
            public Object initContext() {
                return null;
            }

            @Override
            public Object before(Object context,Object ivkObj, IInvokable invokable, Object... args) {
                System.out.println("before...");
                System.out.println("args:");
                Algorithm.execute(Collections.arrayList(args), new SysPrintlnExecutor<>());
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
        };
    }
}

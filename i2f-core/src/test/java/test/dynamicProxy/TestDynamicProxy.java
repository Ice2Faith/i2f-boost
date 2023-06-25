package test.dynamicProxy;

import i2f.core.lang.lambda.Lambdas;
import i2f.core.lang.proxy.JdkProxyUtil;
import i2f.core.lang.proxy.impl.BasicDynamicProxyHandler;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author ltb
 * @date 2022/5/18 8:48
 * @desc
 */
public class TestDynamicProxy {
    public static void main(String[] args){
        IDynamicInterface drive= JdkProxyUtil.proxy(IDynamicInterface.class, new BasicDynamicProxyHandler() {
            @Override
            public Object resolve(Object context, Object ivkObj, Method method, Object... args) {
                String methodName= method.getName();
                System.out.println("dynamic proxy:"+methodName);
                Object ret=null;
                if(methodName.equals(Lambdas.methodName(IDynamicInterface::todoVoidRetVoidArgs))){
                    System.out.println("resolve:"+methodName);
                }else if(methodName.equals(Lambdas.methodName(IDynamicInterface::todoObjRetVoidArgs))){
                    ret=new Date();
                }else if(methodName.equals(Lambdas.methodName(IDynamicInterface::todoStrRetVoidArgs))){
                    ret= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }else if(methodName.equals(Lambdas.methodName(IDynamicInterface::todoAddInt))){
                    Integer v1=(Integer)args[0];
                    Integer v2=(Integer)args[1];
                    ret=v1+v2;
                }
                return ret;
            }
        });

        drive.todoVoidRetVoidArgs();
        Object obj=drive.todoObjRetVoidArgs();
        System.out.println(obj);
        String str=drive.todoStrRetVoidArgs();
        System.out.println(str);
        int ival = drive.todoAddInt(1, 2);
        System.out.println(ival);
    }
}

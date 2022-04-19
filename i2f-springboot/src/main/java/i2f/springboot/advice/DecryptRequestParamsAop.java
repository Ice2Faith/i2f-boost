package i2f.springboot.advice;

import i2f.core.annotations.remark.Remark;
import i2f.core.reflect.core.ReflectResolver;
import i2f.spring.jackson.JacksonJsonProcessor;
import i2f.springboot.advice.annotation.SecureParams;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@ConditionalOnExpression("${i2f.springboot.config.advice.controller-aop.enable:true}")
@Slf4j
@Aspect
@Component
@Remark({
        "because of request advice only support post json decrypt",
        "this aop to decrypt params on controllers methods params which type is string"
})
public class DecryptRequestParamsAop implements InitializingBean {

    @Autowired
    private IStringDecryptor decryptor;

    @Autowired
    private JacksonJsonProcessor processor;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DecryptRequestParamsAop config done.");
    }

    @Pointcut("execution(public * com..*.controller..*.*(..))")
    public void controllerAspect(){}

    @Around("controllerAspect()")
    public Object controllerLog(ProceedingJoinPoint pjp) throws Throwable{
        MethodSignature ms=(MethodSignature) pjp.getSignature();
        Method method=ms.getMethod();
        Class clazz=method.getDeclaringClass();

        Parameter[] params=method.getParameters();
        Object[] args=pjp.getArgs();

        boolean hasProcess=false;
        for(int i=0;i< params.length;i++){
            Parameter parameter=params[i];
            if(!parameter.getType().equals(String.class)){
                continue;
            }
            String data=(String)args[i];
            if(data==null || "".equals(data)){
                continue;
            }
            SecureParams ann= ReflectResolver.findAnnotation(parameter,SecureParams.class,true);
            if(ann==null){
                continue;
            }
            if(!ann.in()){
                continue;
            }

            String ddata=decryptor.decrypt(data);
            ddata=processor.parseText(ddata,String.class);
            args[i]=ddata;
            hasProcess=true;
        }
        if(hasProcess){
            return pjp.proceed(args);
        }
        return pjp.proceed();
    }
}

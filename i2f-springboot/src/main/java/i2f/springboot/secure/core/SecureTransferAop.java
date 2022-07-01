package i2f.springboot.secure.core;


import i2f.springboot.secure.annotation.SecureParams;
import i2f.springboot.secure.exception.SecureException;
import i2f.springboot.secure.util.JacksonJsonProcessor;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author ltb
 * @date 2022/6/29 13:59
 * @desc RSA+AES解密的核心切面类
 */
@ConditionalOnExpression("${i2f.springboot.config.secure.controller-aop.enable:true}")
@Slf4j
@Component
@Aspect
public class SecureTransferAop implements InitializingBean {

    @Autowired
    private SecureTransfer secureTransfer;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired(required = false)
    private HttpServletResponse response;

    @Autowired
    private JacksonJsonProcessor processor;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("SecureTransferAop config done.");
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void controllerAspect(){}

    @Around("controllerAspect()")
    public Object controllerLog(ProceedingJoinPoint pjp) throws Throwable{
        MethodSignature ms=(MethodSignature) pjp.getSignature();
        Method method=ms.getMethod();
        Class clazz=method.getDeclaringClass();

        Parameter[] params=method.getParameters();
        Object[] args=pjp.getArgs();

        log.debug("enter mapping aop:"+clazz.getName()+"."+method.getName());

        // 如果方法上有注解指定返回加密，则给上响应头占位符，在filter中将会处理这个响应头进行数据加密
        // 如果方法上不存在注解，则以类上存在的注解为准
        SecureParams ann=getMethodAnnotation(method);
        if(ann!=null){
            if(ann.in()){
                log.debug("request secure require.");
                String decryptHeader = request.getHeader(SecureTransfer.FILTER_DECRYPT_HEADER);
                if(!SecureTransfer.FLAG_ENABLE.equals(decryptHeader)){
                    log.debug("not decrypt request error.");
                    throw new SecureException();
                }
            }

        }

        // 特殊标记返回值为String的方法
        Class<?> returnType = method.getReturnType();
        if(String.class.isAssignableFrom(returnType)){
            log.debug("mapping return string type.");
            response.setHeader(SecureTransfer.STRING_RETURN_HEADER,SecureTransfer.FLAG_ENABLE);
        }

        // 获取aes解密秘钥
        String aesKey = secureTransfer.getRequestSecureHeader(request);

        // 解密url中的参数，也就是非body中的参数
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
            SecureParams pann= getAnnotation(parameter);
            if(pann==null){
                continue;
            }
            if(!pann.in()){
                continue;
            }
            log.debug("method params["+i+"] called "+parameter.getName()+" secure decrypt." );
            String ddata=secureTransfer.decrypt(data,aesKey);
            ddata=processor.parseText(ddata,String.class);
            args[i]=ddata;
            hasProcess=true;
        }
        log.debug("aop process...");
        Object ret=null;
        if(hasProcess){
            ret= pjp.proceed(args);
        }else {
            ret = pjp.proceed();
        }
        log.debug("aop process done.");
        // 特殊标记返回值为String的方法
        if(ret!=null && (ret instanceof String)){
            log.debug("actual return string type.");
            response.setHeader(SecureTransfer.STRING_RETURN_HEADER,SecureTransfer.FLAG_ENABLE);
        }
        log.debug("leave mapping aop.");
        return ret;
    }

    public static SecureParams getMethodAnnotation(Method method){
        SecureParams ann=getAnnotation(method);
        if(ann==null){
            ann=getAnnotation(method.getDeclaringClass());
        }
        return ann;
    }

    public static SecureParams getAnnotation(AnnotatedElement elem){
        SecureParams ann=elem.getDeclaredAnnotation(SecureParams.class);
        if(ann==null){
            ann=elem.getAnnotation(SecureParams.class);
        }
        return ann;
    }
}

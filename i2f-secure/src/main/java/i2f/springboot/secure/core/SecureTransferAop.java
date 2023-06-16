package i2f.springboot.secure.core;


import i2f.spring.mapping.MappingUtil;
import i2f.springboot.secure.SecureConfig;
import i2f.springboot.secure.annotation.SecureParams;
import i2f.springboot.secure.consts.SecureConsts;
import i2f.springboot.secure.consts.SecureErrorCode;
import i2f.springboot.secure.data.SecureCtrl;
import i2f.springboot.secure.exception.SecureException;
import i2f.springboot.secure.util.SecureUtils;
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
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author ltb
 * @date 2022/6/29 13:59
 * @desc RSA+AES解密的核心切面类
 */
@ConditionalOnExpression("${i2f.springboot.config.secure.aop.enable:true}")
@Slf4j
@Component
@Aspect
public class SecureTransferAop implements InitializingBean {

    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired(required = false)
    private HttpServletResponse response;

    @Autowired
    private SecureConfig secureConfig;

    @Autowired
    private MappingUtil mappingUtil;

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
    public void controllerAspect() {
    }

    @Around("controllerAspect()")
    public Object controllerLog(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
        Class clazz = method.getDeclaringClass();

        Parameter[] params = method.getParameters();
        Object[] args = pjp.getArgs();

        log.debug("enter mapping aop:" + clazz.getName() + "." + method.getName());

        // 如果过滤器中有异常，在AOP中进行抛出，以在ExceptionHandler中进行捕获处理
        Throwable ex = (Throwable) request.getAttribute(SecureConsts.FILTER_EXCEPTION_ATTR_KEY);
        if (ex != null) {
            throw ex;
        }

        // 特殊标记返回值为String的方法
        Class<?> returnType = method.getReturnType();
        if (String.class.isAssignableFrom(returnType)) {
            log.debug("mapping return string type.");
            response.setHeader(SecureConsts.STRING_RETURN_HEADER, SecureConsts.FLAG_ENABLE);
        }

        // 如果方法上有注解指定返回加密，则给上响应头占位符，在filter中将会处理这个响应头进行数据加密
        // 如果方法上不存在注解，则以类上存在的注解为准
        SecureParams ann = SecureUtils.getSecureAnnotation(method);
        if (ann != null) {
            if (ann.in()) {
                log.debug("request secure require.");
                String decryptHeader = (String) request.getAttribute(SecureConsts.FILTER_DECRYPT_HEADER);
                if (!SecureConsts.FLAG_ENABLE.equals(decryptHeader)) {
                    log.debug("not decrypt request error.");
                    throw new SecureException(SecureErrorCode.BAD_SECURE_REQUEST, "不安全的请求");
                }
            }
        }

        SecureCtrl ctrl = SecureUtils.parseSecureCtrl(request, secureConfig, mappingUtil);
        if (ctrl.out) {
            request.setAttribute(SecureConsts.SECURE_REQUIRE_RESPONSE, SecureConsts.FLAG_ENABLE);
        }

        log.debug("aop process...");
        Object ret = pjp.proceed();

        log.debug("aop process done.");
        // 特殊标记返回值为String的方法
        if (ret != null && (ret instanceof String)) {
            log.debug("actual return string type.");
            response.setHeader(SecureConsts.STRING_RETURN_HEADER, SecureConsts.FLAG_ENABLE);
        }
        log.debug("leave mapping aop.");
        return ret;
    }

}

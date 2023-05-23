package i2f.springboot.trace.spy.plugin;

import i2f.springboot.trace.spy.SpringTraceLogger;
import i2f.springboot.trace.spy.core.InvokeTrace;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2023/5/22 16:00
 * @desc
 */
@ConditionalOnExpression("${invoke.trace.aop.enable:false}")
@Slf4j
@Aspect
@Data
@Component
public class AopTrace {
    // 定义切点Pointcut
    @Pointcut(" execution(* com.test.modules.*.*.*Controller.*(..))" +
            "|| execution(* com.test.modules.*.*.*Service*.*(..))" +
            "|| execution(* com.test.modules.*.*.*Mapper.*(..))" +
            "|| execution(* com.test.modules.*.*.*Dao.*(..))" +
            "|| execution(* com.test.modules.*.*.*Util*.*(..))" +
            "|| execution(* com.test.modules.*.*.*Config*.*(..))" +
            "|| execution(* com.test.modules.*.*.*Api*.*(..))")
    public void allExecable() {
    }

    @Around("allExecable()")
    public Object aroundAllExecable(ProceedingJoinPoint pjp) throws Throwable {
        return doAround(pjp);
    }

    public static Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Object _target = pjp.getTarget();
        Object _this = pjp.getThis();
        for (Object bean : CglibProxyTrace.enhancerBeans.values()) {
            if (bean.equals(_this) || bean.equals(_target)) {
                return pjp.proceed();
            }
        }
        Method method = signature.getMethod();
        Object[] args = pjp.getArgs();
        return InvokeTrace.trace(() -> pjp.proceed(),
                SpringTraceLogger.LOGGER,
                _target.getClass(),
                method, args);
    }
}

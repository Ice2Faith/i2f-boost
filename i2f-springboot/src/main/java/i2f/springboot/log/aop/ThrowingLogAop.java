package i2f.springboot.log.aop;

import i2f.core.log.core.LogCore;
import i2f.core.log.core.LogDispatcher;
import i2f.core.log.data.LogDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Ice2Faith
 * @date 2023/8/1 17:53
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.log.throwing.enable:false}")
@Slf4j
@Aspect
@Component
public class ThrowingLogAop {
    // 匹配所有bean的所有方法
    @Pointcut("execution(* *(..))")
    public void allBeanMethods() {}

    @AfterThrowing(pointcut = "allBeanMethods()", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint pjp, Exception ex) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Object _target = pjp.getTarget();
        Object _this = pjp.getThis();
        Method method = signature.getMethod();
        Object[] args = pjp.getArgs();
        Parameter[] params = method.getParameters();

        if(method!=null){
            LogDto dto = LogCore.invokeThrowing(method,ex,-1, args);
            if(dto!=null){
                LogDispatcher.write(dto);
            }
        }
    }
}

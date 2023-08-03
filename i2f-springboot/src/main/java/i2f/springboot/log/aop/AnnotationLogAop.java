package i2f.springboot.log.aop;

import i2f.core.log.core.LogCore;
import i2f.springboot.log.impl.AspectProxyInvokable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * @author Ice2Faith
 * @date 2023/8/1 17:53
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.log.annotation.enable:true}")
@Slf4j
@Aspect
@Component
public class AnnotationLogAop {


    @Pointcut("@annotation(i2f.core.log.annotations.Log)" +
            "")
    public void annPointCut() {
    }

    @Around("annPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return LogCore.aopAround(new AspectProxyInvokable(pjp));
    }
}

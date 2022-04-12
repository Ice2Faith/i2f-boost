package i2f.springboot.mvc;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@ConditionalOnExpression("${i2f.springboot.config.mvc.aop-global-log.enable:true}")
@Aspect
@Component
public class LogAop {
    @Pointcut("execution(public * com..*.controller..*.*(..))")
    public void controllerAspect(){}

    @Pointcut("execution(public * com..*.service..*.*(..))")
    public void serviceAspect(){}

    @Pointcut("execution(public * com..*.dao..*.*(..))")
    public void daoAspect(){}

    @ConditionalOnExpression("${i2f.springboot.config.mvc.aop-global-log.controller.enable:true}")
    @Around("controllerAspect()")
    public Object controllerLog(ProceedingJoinPoint pjp) throws Throwable{
        return AopLogUtil.controllerLogAround(pjp);
    }

    @ConditionalOnExpression("${i2f.springboot.config.mvc.aop-global-log.service.enable:true}")
    @Around("serviceAspect()")
    public Object serviceLog(ProceedingJoinPoint pjp) throws Throwable{
        return AopLogUtil.serviceLogAround(pjp);
    }

    @ConditionalOnExpression("${i2f.springboot.config.mvc.aop-global-log.dao.enable:true}")
    @Around("daoAspect()")
    public Object daoLog(ProceedingJoinPoint pjp) throws Throwable{
        return AopLogUtil.daoLogAround(pjp);
    }

}

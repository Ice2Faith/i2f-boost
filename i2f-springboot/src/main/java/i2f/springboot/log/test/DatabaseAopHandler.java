package i2f.springboot.log.test;

import i2f.springboot.log.annotations.SysLog;
import i2f.springboot.log.aop.SysLogAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ice2Faith
 * @date 2023/2/13 10:49
 * @desc
 */
@SysLog(value = false)
@Slf4j
@Aspect
@Component
public class DatabaseAopHandler {

    @Autowired
    private SysLogAop sysLogAop;

    @Pointcut("@annotation(com.xxl.job.core.handler.annotation.XxlJob)")
    public void xxlJobPointcut() {
    }

    @Around("xxlJobPointcut()")
    public Object doAnnotationLogAround(ProceedingJoinPoint pjp) throws Throwable {
        return sysLogAop.doLogAround(pjp);
    }


    @Pointcut("execution(public * com..*.*Service*.*(..))" +
            "|| execution(public * com..*.*Handler*.*(..))" +
            "|| execution(public * com..*.*Resolver*.*(..))" +
            "|| execution(public * com..*.*Service.*(..))" +
            "|| execution(public * com..*.*Config.*(..))"
    )
    public void executePointcut() {
    }

    @Around("executePointcut()")
    public Object doExecuteLogAround(ProceedingJoinPoint pjp) throws Throwable {
        return sysLogAop.doLogAround(pjp);
    }
}

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
@ConditionalOnExpression("${i2f.springboot.config.log.api.enable:true}")
@Slf4j
@Aspect
@Component
public class ApiLogAop {


    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping)" +
            "")
    public void annPointCut() {
    }

    @Around("annPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return LogCore.aopAround(new AspectProxyInvokable(pjp));
    }
}

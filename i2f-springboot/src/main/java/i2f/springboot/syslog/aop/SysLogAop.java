package i2f.springboot.syslog.aop;

import i2f.springboot.syslog.SysLogUserProvider;
import i2f.springboot.syslog.SysLogger;
import i2f.springboot.syslog.SysLoggerFactory;
import i2f.springboot.syslog.annotations.SysLog;
import i2f.springboot.syslog.consts.LogLevel;
import i2f.springboot.syslog.data.SysLogDto;
import i2f.springboot.syslog.util.SysLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Ice2Faith
 * @date 2023/2/7 10:04
 * @desc
 */
@SysLog(value = false)
@Slf4j
@Aspect
@Component
public class SysLogAop {


    @Autowired(required = false)
    private SysLogUserProvider userProvider;

    @Pointcut("@annotation(i2f.springboot.syslog.annotations.SysLog)")
    public void logPointcut() {
    }

    @Around("logPointcut()")
    public Object doAnnotationLogAround(ProceedingJoinPoint pjp) throws Throwable {
        return doLogAround(pjp);
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void mappingPointcut() {
    }

    @ConditionalOnExpression("${sys.log.mapping.enable:false}")
    @Around("mappingPointcut()")
    public Object doMappingLogAround(ProceedingJoinPoint pjp) throws Throwable {
        return doLogAround(pjp);
    }


    public Object doLogAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Object _target = pjp.getTarget();
        Object _this = pjp.getThis();
        Method method = signature.getMethod();
        Object[] args = pjp.getArgs();
        Parameter[] params = method.getParameters();

        SysLog ann = SysLogUtil.getMethodSysLog(method);

        if (ann != null && !ann.value()) {
            return pjp.proceed();
        }

        String location = method.getDeclaringClass().getName();
        long beginTime = System.currentTimeMillis();
        Throwable ex = null;
        SysLogger logger = SysLoggerFactory.getLogger(location);

        SysLogUtil.prepareCurrentTrace();
        SysLogUtil.addTraceLevel();
        try {
            return pjp.proceed();
        } catch (Throwable e) {
            ex = e;
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();

            SysLogDto dto = SysLogUtil.instanceLog(method);
            dto.setCostTime(endTime - beginTime);
            dto.setLogLocation(location);

            SysLogUtil.fillLogContentByArgs(dto, args);

            fillUserInfoByProvider(dto);

            SysLogUtil.fillLogExceptionTrackByException(dto, ex);

            SysLogUtil.fillRequestInfosByRequestContextHolder(dto);

            SysLogUtil.fillTraceId(dto);

            SysLogUtil.fillSystemModuleByEnvironment(dto);

            SysLogUtil.fillLogKeyValBySpringMvc(dto, method);

            SysLogUtil.fillLabelBySwagger(dto, method);

            SysLogUtil.subTraceLevel();

            dto.setLogLevel(LogLevel.INFO.code());
            if (ex != null) {
                dto.setLogLevel(LogLevel.ERROR.code());
            }
            logger.log(dto);
        }
    }

    protected void fillUserInfoByProvider(SysLogDto dto) {
        try {
            if (userProvider != null) {
                dto.setUserId(userProvider.getUserId());
                dto.setUserName(userProvider.getUsername());
            }
        } catch (Exception e) {
            log.debug("填充系统日志用户信息失败：" + e.getClass().getName() + ":" + e.getMessage());
        }
    }


}

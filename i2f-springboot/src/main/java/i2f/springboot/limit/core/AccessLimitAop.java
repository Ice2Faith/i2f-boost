package i2f.springboot.limit.core;

import i2f.springboot.limit.annotation.AccessLimit;
import i2f.springboot.limit.exception.AccessLimitException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * @author ltb
 * @date 2022/7/2 22:01
 * @desc
 */
@Slf4j
@Component
@Aspect
public class AccessLimitAop implements InitializingBean {
    ConcurrentMap<String, Long> limitCache = new ConcurrentHashMap<>();
    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(30);

    @Autowired(required = false)
    private IUserIdProvider userIdProvider;


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("AccessLimitAop config done.");
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void controllerAspect() {
    }


    @Before("controllerAspect()")
    public void controllerLog(JoinPoint pjp) throws Throwable {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
        Class clazz = method.getDeclaringClass();

        AccessLimit ann=getMethodAnnotation(method);
        if(ann==null || !ann.value()){
            return;
        }

        if(ann.apiCount()>0){
            boolean ok=processLimit(method,ann.apiCount(),"API_",null,ann.timeline(),ann.timeUnit());
            if(!ok){
                throw new AccessLimitException("接口访问上限，限制访问！");
            }
        }

        if(ann.userCount()>0){
            if(userIdProvider==null){
                return;
            }
            boolean ok=processLimit(method,ann.userCount(),"USER_", userIdProvider.getUserId(pjp),ann.timeline(),ann.timeUnit() );
            if(!ok){
                throw new AccessLimitException("用户访问上限，限制访问！");
            }
        }
    }

    public synchronized boolean processLimit(Method method, long limitCount, String prefixKey, String userKey, long timeline, TimeUnit timeUnit){
        String lockKey="LIMIT_"+prefixKey+(userKey==null?"":("_"+userKey+"_"))+method.toGenericString();
        if(!limitCache.containsKey(lockKey)){
            limitCache.put(lockKey,0L);
        }
        long curCount=limitCache.get(lockKey);
        if(curCount>limitCount){
            return false;
        }
        curCount++;
        limitCache.put(lockKey,curCount);
        pool.schedule(new Runnable() {
            @Override
            public void run() {
                synchronized (AccessLimitAop.this){
                    if(limitCache.containsKey(lockKey)){
                        long curCount=limitCache.get(lockKey);
                        curCount--;
                        if(curCount<=0){
                            limitCache.remove(lockKey);
                        }else{
                            limitCache.put(lockKey,curCount);
                        }
                    }
                }
            }
        },timeline,timeUnit);
        return true;
    }

    public static AccessLimit getMethodAnnotation(Method method){
        AccessLimit ann=getAnnotation(method);
        if(ann==null){
            ann=getAnnotation(method.getDeclaringClass());
        }
        return ann;
    }

    public static AccessLimit getAnnotation(AnnotatedElement elem){
        AccessLimit ann=elem.getDeclaredAnnotation(AccessLimit.class);
        if(ann==null){
            ann=elem.getAnnotation(AccessLimit.class);
        }
        return ann;
    }

}

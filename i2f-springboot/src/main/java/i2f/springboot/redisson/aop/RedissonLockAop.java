package i2f.springboot.redisson.aop;

import i2f.springboot.redisson.RedissonLockProvider;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.TimeUnit;

/**
 * 实现基于AOP的Redisson分布式锁机制
 * 解析方法上的 RedisLock RedisReadLock RedisWriteLock 注解
 * 实现自动获取锁，自动释放锁，避免代码中添加不必要的try...finally...嵌套
 * 注解的参数都是四个：
 * value 分布式锁的key
 * keyIdx 函数入参的第几个参数，参与锁的一部分
 * clazz 分布式锁的类锁前缀
 * classify 分布式锁的类前缀，是否为方法的声明类
 * timeout 分布式锁的自动超时时间
 * unit 超时单位
 * 逻辑：
 * 方法上允许重复着三个注解
 * 生效的优先级为：
 * RedisLock > RedisWriteLock > RedisReadLock
 * 锁键的生成规则：
 * [锁键]=[限定类]+":"+[限定值]
 * [限定类]和[限定值]都可以无值
 * 当[限定类]有值，[限定值]无值，[锁键]=[限定类]
 * 当[限定类]无值，[限定值]有值，[锁键]=[限定值]
 * 当[限定类]有值，[限定值]有值，[限定类]+":"+[限定值]
 * 当[限定类]无值，[限定值]无值，[锁键]=[方法声明类名]+":"+[方法名]
 * <p>
 * [限定类]=clazz
 * 当clazz==Object.class,表示clazz无值
 * 当classify==true && clazz==Object.class，表示clazz值应该为method.getDeclaringClass()
 * 其他情况，[限定类]就是clazz的值
 * <p>
 * [限定值]=value+":"+args[keyIdx]
 * 当value==""，表示value无值
 * 当keyIdx<0 || keyIdx>=args.length，表示keyIdx无值
 * 当value有值，keyIdx无值，[限定值]=value
 * 当value无值，keyIdx有值，[限定值]=args[keyIdx]
 * 当value有值，keyIdx有值，[限定值]=value+":"+args[keyIdx]
 * 当value和keyIdx均为无值，[限定值]=无值
 * <p>
 * 如果超时timout大于0，则按照超时设置锁超时
 * 否则，按照默认
 * <p>
 * 下面是注解的使用和说明，假定都是在com.test.TestController类中的test方法上使用
 * -------------------------------------------------------
 *
 * @RedisLock(value="app") 锁键=app
 * 等价于，全局锁，应用锁
 * @RedisLock(classify = true)
 * 锁键=com.test.TestController
 * 等价于，类锁
 * @RedisLock(classify = true,value="unique")
 * 锁键=com.test.TestController:unique
 * 等价于，类锁，类部分锁
 * @RedisLock(clazz=User.class) 锁键=com.test.model.User
 * 等价于，类锁，锁的类可以为其他类
 * @RedisLock(clazz=User.class,value="unique") 锁键=com.test.model.User:unique
 * 等价于，类锁
 * @RedisLock(value="unique",keyIdx = 0)
 * 假设入参第0个为userId，此时的值为1001
 * 锁键=unique:args[0]=unique:userId=unique:1001
 * 等价于应用分区锁，这个示例中，就是按照用户分区锁
 * @RedisLock(classify=true,value="unique",keyIdx = 0)
 * 假设入参第0个为userId，此时的值为1001
 * 锁键=com.test.TestController:unique:args[0]=com.test.TestController:unique:userId=com.test.TestController:unique:1001
 * 等价于类分区锁，这个示例中，就是按照用户使用类分区锁
 * @RedisLock 锁键=com.test.TestController:test
 * 等价于，方法锁，锁的是这个方法
 */
@Slf4j
@Aspect
@Component
public class RedissonLockAop {

    @Pointcut("@annotation(org.jeecg.config.redisson.annotation.RedisLock)" +
            "|| @annotation(org.jeecg.config.redisson.annotation.RedisReadLock)" +
            "|| @annotation(org.jeecg.config.redisson.annotation.RedisWriteLock)")
    public void lockPointCut() {
    }

    @Autowired
    private RedissonLockProvider redissonLockProvider;

    @Around("lockPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Object _target = pjp.getTarget();
        Object _this = pjp.getThis();
        Method method = signature.getMethod();
        Object[] args = pjp.getArgs();
        Parameter[] params = method.getParameters();

        org.jeecg.config.redisson.annotation.RedisLock lann = getAnnotation(method, org.jeecg.config.redisson.annotation.RedisLock.class);
        if (lann != null) {
            String lockName = lann.value();
            Class<?> clazz = lann.clazz();
            long timeout = lann.timeout();
            TimeUnit timeUnit = lann.unit();
            int kidx = lann.keyIdx();
            if (kidx > 0 && kidx < params.length) {
                String keyArg = String.valueOf(args[kidx]);
                if (StringUtils.isEmpty(lockName)) {
                    lockName = keyArg;
                } else {
                    lockName += ":" + keyArg;
                }
            }
            RLock lock = null;
            try {
                if (Object.class.equals(clazz) && lann.classify()) {
                    clazz = method.getDeclaringClass();
                }
                if (!Object.class.equals(clazz)) {
                    if (!StringUtils.isEmpty(lockName)) {
                        lock = redissonLockProvider.getLock(clazz, lockName);
                    } else {
                        lock = redissonLockProvider.getLock(clazz);
                    }
                } else {
                    if (!StringUtils.isEmpty(lockName)) {
                        lock = redissonLockProvider.getLock(lockName);
                    }
                }
                if (lock == null) {
                    lock = redissonLockProvider.getLock(method.getDeclaringClass(), method.getName());
                }

                log.info("RLock lock clazz=" + clazz + ",value=" + lockName + ",timeout=" + timeout + ",unit=" + timeUnit);
                if (timeout > 0) {
                    lock.lock(timeout, timeUnit);
                } else {
                    lock.lock();
                }

                return pjp.proceed();
            } finally {
                if (lock != null) {
                    log.info("RLock unlock clazz=" + clazz + ",value=" + lockName + ",timeout=" + timeout + ",unit=" + timeUnit);
                    lock.unlock();
                }
            }
        }

        org.jeecg.config.redisson.annotation.RedisWriteLock wann = getAnnotation(method, org.jeecg.config.redisson.annotation.RedisWriteLock.class);
        if (wann != null) {
            String lockName = wann.value();
            Class<?> clazz = wann.clazz();
            long timeout = wann.timeout();
            TimeUnit timeUnit = wann.unit();
            int kidx = wann.keyIdx();
            if (kidx > 0 && kidx < params.length) {
                String keyArg = String.valueOf(args[kidx]);
                if (StringUtils.isEmpty(lockName)) {
                    lockName = keyArg;
                } else {
                    lockName += ":" + keyArg;
                }
            }
            RReadWriteLock lock = null;
            try {
                if (Object.class.equals(clazz) && wann.classify()) {
                    clazz = method.getDeclaringClass();
                }
                if (!Object.class.equals(clazz)) {
                    if (!StringUtils.isEmpty(lockName)) {
                        lock = redissonLockProvider.getReadWriteLock(clazz, lockName);
                    } else {
                        lock = redissonLockProvider.getReadWriteLock(clazz);
                    }
                } else {
                    if (!StringUtils.isEmpty(lockName)) {
                        lock = redissonLockProvider.getReadWriteLock(lockName);
                    }
                }
                if (lock == null) {
                    lock = redissonLockProvider.getReadWriteLock(method.getDeclaringClass(), method.getName());
                }

                log.info("RReadWriteLock writeLock lock clazz=" + clazz + ",value=" + lockName + ",timeout=" + timeout + ",unit=" + timeUnit);
                if (timeout > 0) {
                    lock.writeLock().lock(timeout, timeUnit);
                } else {
                    lock.writeLock().lock();
                }

                return pjp.proceed();
            } finally {
                if (lock != null) {
                    log.info("RReadWriteLock writeLock unlock clazz=" + clazz + ",value=" + lockName + ",timeout=" + timeout + ",unit=" + timeUnit);
                    lock.writeLock().unlock();
                }
            }
        }

        org.jeecg.config.redisson.annotation.RedisReadLock rann = getAnnotation(method, org.jeecg.config.redisson.annotation.RedisReadLock.class);
        if (rann != null) {
            String lockName = rann.value();
            Class<?> clazz = rann.clazz();
            long timeout = rann.timeout();
            TimeUnit timeUnit = rann.unit();
            int kidx = rann.keyIdx();
            if (kidx > 0 && kidx < params.length) {
                String keyArg = String.valueOf(args[kidx]);
                if (StringUtils.isEmpty(lockName)) {
                    lockName = keyArg;
                } else {
                    lockName += ":" + keyArg;
                }
            }
            RReadWriteLock lock = null;
            try {
                if (Object.class.equals(clazz) && rann.classify()) {
                    clazz = method.getDeclaringClass();
                }
                if (!Object.class.equals(clazz)) {
                    if (!StringUtils.isEmpty(lockName)) {
                        lock = redissonLockProvider.getReadWriteLock(clazz, lockName);
                    } else {
                        lock = redissonLockProvider.getReadWriteLock(clazz);
                    }
                } else {
                    if (!StringUtils.isEmpty(lockName)) {
                        lock = redissonLockProvider.getReadWriteLock(lockName);
                    }
                }
                if (lock == null) {
                    lock = redissonLockProvider.getReadWriteLock(method.getDeclaringClass(), method.getName());
                }

                log.info("RReadWriteLock readLock lock clazz=" + clazz + ",value=" + lockName + ",timeout=" + timeout + ",unit=" + timeUnit);
                if (timeout > 0) {
                    lock.readLock().lock(timeout, timeUnit);
                } else {
                    lock.readLock().lock();
                }

                return pjp.proceed();
            } finally {
                if (lock != null) {
                    log.info("RReadWriteLock readLock unlock clazz=" + clazz + ",value=" + lockName + ",timeout=" + timeout + ",unit=" + timeUnit);
                    lock.readLock().unlock();
                }
            }
        }

        return pjp.proceed();
    }

    public static <T extends Annotation> T getAnnotation(AnnotatedElement elem, Class<T> annClazz) {
        T ann = elem.getDeclaredAnnotation(annClazz);
        if (ann == null) {
            ann = elem.getAnnotation(annClazz);
        }
        return ann;
    }
}

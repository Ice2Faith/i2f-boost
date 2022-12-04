package i2f.spring.chain;


import i2f.core.safe.Nulls;
import i2f.core.thread.LatchRunnable;
import i2f.spring.context.SpringUtil;
import i2f.spring.filter.WhiteFilter;
import i2f.spring.slf4j.PerfLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/9/20 15:18
 * @desc 链式执行管理器
 */
@Slf4j
public class ChainManager {
    private static PerfLogger logger = new PerfLogger(log);
    private static AntPathMatcher matcher = new AntPathMatcher(".");

    /**
     * 获取指定处理器的所有后置处理器
     *
     * @param parent
     * @return
     */
    public static Set<IChainResolver> getNextResolvers(IChainResolver parent, ChainContext context) {
        Set<IChainResolver> resolverSet = new HashSet<>();
        if (parent == null) {
            return resolverSet;
        }
        Map<String, IChainResolver> beans = SpringUtil.getBeans(IChainResolver.class);
        Class<? extends IChainResolver> parentClass = parent.getClass();
        for (Map.Entry<String, IChainResolver> item : beans.entrySet()) {
            IChainResolver value = item.getValue();
            Set<Class<? extends IChainResolver>> attach = value.attach();
            for (Class<? extends IChainResolver> attachClass : attach) {
                if (parentClass.equals(attachClass)) {
                    resolverSet.add(value);
                }
            }
        }
        return WhiteFilter.antPkgFilter(new HashSet<IChainResolver>(), resolverSet,
                Nulls.get(context, ChainContext::getIncludesResolver),
                Nulls.get(context, ChainContext::getExcludesResolver),
                (elem) -> elem.getClass().getName());
    }

    /**
     * 分发调用后置处理器
     *
     * @param parent
     * @param action
     * @param params
     * @param async  是否异步执行
     * @param await  异步执行时是否需要等待结束
     * @param pool   异步执行的线程池，可以为null,为null则直接创建线程
     */
    public static void dispatch(IChainResolver parent, Object action, Object params, boolean async, boolean await, ExecutorService pool, ChainContext context) {
        if (async) {
            if (await) {
                logger.trace((val) -> "resolver next async await, pool=" + val, pool);
                ChainManager.chainAsyncAwait(pool, parent, action, params, context);
            } else {
                logger.trace((val) -> "resolver next async, pool=" + val, pool);
                ChainManager.chainAsync(pool, parent, action, params, context);
            }
        } else {
            logger.trace(() -> "resolver next sync");
            ChainManager.chain(parent, action, params, context);
        }
    }

    /**
     * 直接执行对应的处理器的后续处理器
     *
     * @param parent
     * @param params
     */
    public static void chain(IChainResolver parent, Object action, Object params, ChainContext context) {
        Set<IChainResolver> resolverSet = getNextResolvers(parent, context);
        logger.traceArgs((args) -> "chain parent=" + args[0] + ", action=" + args[1] + ", resolverCount=" + ((Set<IChainResolver>) args[2]).size(), parent, action, resolverSet);
        for (IChainResolver item : resolverSet) {
            logger.trace((val) -> "chain dispatch resolver=" + val, item);
            try {
                item.task(parent, action, params, context);
            } catch (Exception e) {
                logger.error((ex) -> "chain resolver error:" + ex.getMessage(), e);
            }
        }
    }

    /**
     * 异步执行后续处理器
     *
     * @param pool
     * @param parent
     * @param action
     * @param params
     */
    public static void chainAsync(ExecutorService pool, IChainResolver parent, Object action, Object params, ChainContext context) {
        Set<IChainResolver> resolverSet = getNextResolvers(parent, context);
        logger.traceArgs((args) -> "chainAsync parent=" + args[0] + ", action=" + args[1] + ", resolverCount=" + ((Set) args[2]).size(), parent, action, resolverSet);
        for (IChainResolver item : resolverSet) {
            if (pool == null) {
                logger.trace((val) -> "chainAsync dispatch (new Thread) resolver=" + val, item);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            item.task(parent, action, params, context);
                        } catch (Exception e) {
                            logger.error((ex) -> "chain resolver error:" + ex.getMessage(), e);
                        }
                    }
                }).start();
            } else {
                logger.traceArgs((args) -> "chainAsync dispatch (pool:" + args[0] + ") resolver=" + args[1], pool, item);
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            item.task(parent, action, params, context);
                        } catch (Exception e) {
                            logger.error((ex) -> "chain resolver error:" + ex.getMessage(), e);
                        }
                    }
                });
            }
        }
    }

    /**
     * 异步执行后续处理器并等待处理结束
     *
     * @param pool
     * @param parent
     * @param action
     * @param params
     */
    public static void chainAsyncAwait(ExecutorService pool, IChainResolver parent, Object action, Object params, ChainContext context) {
        Set<IChainResolver> resolverSet = getNextResolvers(parent, context);
        logger.traceArgs((args) -> "chainAsyncAwait parent=" + args[0] + ", action=" + args[1] + ", resolverCount=" + ((Set) args[2]).size(), parent, action, resolverSet);
        CountDownLatch latch = new CountDownLatch(resolverSet.size());
        for (IChainResolver item : resolverSet) {
            if (pool == null) {
                logger.trace((val) -> "chainAsyncAwait dispatch (new Thread) resolver=" + val, item);
                new Thread(new LatchRunnable(latch) {
                    @Override
                    public void doTask() throws Exception {
                        try {
                            item.task(parent, action, params, context);
                        } catch (Exception e) {
                            logger.error((ex) -> "chain resolver error:" + ex.getMessage(), e);
                        }
                    }
                }).start();
            } else {
                logger.traceArgs((args) -> "chainAsyncAwait dispatch (pool:" + args[0] + ") resolver=" + args[1], pool, item);
                pool.submit(new LatchRunnable(latch) {
                    @Override
                    public void doTask() throws Exception {
                        try {
                            item.task(parent, action, params, context);
                        } catch (Exception e) {
                            logger.error((ex) -> "chain resolver error:" + ex.getMessage(), e);
                        }
                    }
                });
            }
        }
        try {
            logger.trace(() -> "chainAsyncAwait latch await begin...");
            latch.await();
            logger.trace(() -> "chainAsyncAwait latch await done.");
        } catch (InterruptedException e) {
            logger.error((ex) -> "chainAsyncAwait interrupted:" + ex.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}

package i2f.spring.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/9/20 15:28
 * @desc 抽象的链式执行器
 * 结合ChainManager.entry实现空的task构造参数透传与异步执行控制
 */
public abstract class AbsChainResolver implements IChainResolver {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Set<Class<? extends IChainResolver>> attach() {
        return new HashSet<>();
    }

    @Override
    public void next(Object action, Object params, boolean async, boolean await, ExecutorService pool, ChainContext context) {
        ChainManager.dispatch(this, action, params, async, await, pool, context);
    }

    public void next(Object action, Object params, boolean async, boolean await, ExecutorService pool) {
        ChainManager.dispatch(this, action, params, async, await, pool, null);
    }

    public void next(Object action, Object params, boolean async, boolean await) {
        ChainManager.dispatch(this, action, params, async, await, null, null);
    }

    public void next(Object action, Object params) {
        ChainManager.dispatch(this, action, params, false, false, null, null);
    }
}

package i2f.springboot.tx;

import i2f.core.thread.NamingThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/1/28 8:44
 * @desc 提供手动事务控制的能力
 * -----------------------------------
 * 使用方法
 * 1.自己控制事务
 * 需要注意，事务一定要做到一定提交或一定回滚
 * 使用步骤：
 * 使用begin系列函数，开启一个事务，得到一个事务标识对象 TransactionStatus
 * 使用commit或者rollback函数，提交或回滚相应事务标识对象的事务 TransactionStatus
 * 这样使用，会发现不太好用，必须自己catch异常之后rollback
 * 并且自己需要关注事务标识对象 TransactionStatus
 * 这样是有点麻烦的
 * 所以，开启事务可以使用超时，事务超时之后自动提交，例如默认的：beginTimeout()
 * 因此，一般建议使用tx系列函数，也就是方法2
 * 2.使用tx系列函数
 * void tx(TransactionStatus status, TxExecute task)
 * 这个系列函数，第一个参数都是事务标识对象
 * 后面的根据场景，定义了三个函数式接口
 * 分别为有入参有返回值TxFunction<R, T>，有入参无返回值TxConsumer<T>，无入参无返回值TxExecute
 * 使用示例：
 * transactionHelper.tx(transactionHelper.beginTimeout(),()->{
 * // 数据库操作
 * });
 */
@Component
public class TransactionHelper {

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    private final ConcurrentHashMap<TransactionStatus, Boolean> autoCommitMap = new ConcurrentHashMap<>();
    public static final Boolean SPACE_HOLD = Boolean.TRUE;
    private final ScheduledExecutorService pool = Executors.newScheduledThreadPool(30, new NamingThreadFactory("tx", "autocommit"));
    public static final long DEFAULT_TIMEOUT_SECONDS = 3 * 60;

    public TransactionStatus beginTimeout() {
        return begin(IsolationLevel.DEFAULT, PropagationBehavior.REQUIRED, DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    public TransactionStatus begin(long timeout, TimeUnit timeUnit) {
        return begin(IsolationLevel.DEFAULT, PropagationBehavior.REQUIRED, timeout, timeUnit);
    }

    public TransactionStatus begin() {
        return begin(IsolationLevel.DEFAULT, PropagationBehavior.REQUIRED);
    }

    public TransactionStatus begin(IsolationLevel level, long timeout, TimeUnit timeUnit) {
        return begin(level, PropagationBehavior.REQUIRED, timeout, timeUnit);
    }

    public TransactionStatus begin(IsolationLevel level) {
        return begin(level, PropagationBehavior.REQUIRED);
    }

    public TransactionStatus begin(PropagationBehavior behavior) {
        return begin(IsolationLevel.DEFAULT, behavior);
    }

    public TransactionStatus begin(PropagationBehavior behavior, long timeout, TimeUnit timeUnit) {
        return begin(IsolationLevel.DEFAULT, behavior, timeout, timeUnit);
    }

    public TransactionStatus begin(IsolationLevel level, PropagationBehavior behavior) {
        return begin(level, behavior, -1, TimeUnit.SECONDS);
    }

    public TransactionStatus begin(IsolationLevel level, PropagationBehavior behavior, long timeout, TimeUnit timeUnit) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setIsolationLevel(level.code());
        def.setPropagationBehavior(behavior.code());

        TransactionStatus status = dataSourceTransactionManager.getTransaction(def);

        autoCommitTx(status, timeout, timeUnit);

        return status;
    }

    protected void autoCommitTx(TransactionStatus status, long timeout, TimeUnit timeUnit) {
        if (timeout < 0) {
            return;
        }
        autoCommitMap.put(status, SPACE_HOLD);
        pool.schedule(() -> {
            if (autoCommitMap.containsKey(status)) {
                commit(status);
                autoCommitMap.remove(status);
            }
        }, timeout, timeUnit);
    }

    public void commit(TransactionStatus status) {
        dataSourceTransactionManager.commit(status);
    }

    public void rollback(TransactionStatus status) {
        dataSourceTransactionManager.rollback(status);
    }

    @FunctionalInterface
    public interface TxFunction<R, T> {
        R run(T arg) throws Throwable;
    }

    public <R, T> R tx(T arg, TxFunction<R, T> function) {
        return tx(beginTimeout(), arg, function);
    }

    public <R, T> R tx(TransactionStatus status, T arg, TxFunction<R, T> function) {
        try {
            R ret = function.run(arg);
            commit(status);
            return ret;
        } catch (Throwable e) {
            rollback(status);
            throw new TxUnhandledException(e.getMessage(), e);
        }
    }

    @FunctionalInterface
    public interface TxExecute {
        void run() throws Throwable;
    }

    public void tx(TxExecute task) {
        tx(beginTimeout(), task);
    }

    public void tx(TransactionStatus status, TxExecute task) {
        try {
            task.run();
            commit(status);
        } catch (Throwable e) {
            rollback(status);
            throw new TxUnhandledException(e.getMessage(), e);
        }
    }

    @FunctionalInterface
    public interface TxConsumer<T> {
        void run(T arg) throws Throwable;
    }

    public <T> void tx(T arg, TxConsumer<T> task) {
        tx(beginTimeout(), arg, task);
    }

    public <T> void tx(TransactionStatus status, T arg, TxConsumer<T> task) {
        try {
            task.run(arg);
            commit(status);
        } catch (Throwable e) {
            rollback(status);
            throw new TxUnhandledException(e.getMessage(), e);
        }
    }

    @FunctionalInterface
    public interface TxSupplier<R> {
        R run() throws Throwable;
    }

    public <R> R tx(TransactionStatus status, TxSupplier<R> task) {
        try {
            R ret = task.run();
            commit(status);
            return ret;
        } catch (Throwable e) {
            rollback(status);
            throw new TxUnhandledException(e.getMessage(), e);
        }
    }
}

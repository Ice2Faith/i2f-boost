package i2f.core.delegate.batch;


import i2f.core.type.tuple.impl.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author ltb
 * @date 2022/11/21 14:30
 * @desc
 */
public class BatchDelegator {
    public static <R, T> List<Tuple2<R, Exception>> batch(Iterator<T> col, int batchCount, IBatchResolver<R, T> resolver) {
        return batch(col, batchCount, resolver, true);
    }

    public static <R, T> List<Tuple2<R, Exception>> batch(Iterator<T> col, int batchCount, IBatchResolver<R, T> resolver, boolean throwEx) {
        List<Tuple2<R, Exception>> ret = new ArrayList<Tuple2<R, Exception>>();
        List<T> once = new LinkedList<T>();
        int onceCount = 0;
        while (col.hasNext()) {
            T item = col.next();
            once.add(item);
            onceCount++;
            if (onceCount == batchCount) {
                R onceRet = null;
                Exception onceEx = null;
                try {
                    onceRet = resolver.resolve(once);
                } catch (Exception e) {
                    onceEx = e;
                    if (throwEx) {
                        throw new RejectedExecutionException(e.getMessage(), e);
                    }
                }
                ret.add(new Tuple2<>(onceRet, onceEx));
                once = new LinkedList<T>();
                onceCount = 0;
            }
        }
        if (onceCount > 0) {
            R onceRet = null;
            Exception onceEx = null;
            try {
                onceRet = resolver.resolve(once);
            } catch (Exception e) {
                onceEx = e;
                if (throwEx) {
                    throw new RejectedExecutionException(e.getMessage(), e);
                }
            }
            ret.add(new Tuple2<>(onceRet, onceEx));
        }
        return ret;
    }
}

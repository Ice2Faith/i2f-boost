package i2f.core.streaming.base.process;

import i2f.core.streaming.AbsStreaming;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class LimitStreaming<E> extends AbsStreaming<E, E> {
    public int skip = -1;
    public int limit = -1;

    public LimitStreaming(int skip, int limit) {
        this.skip = skip;
        this.limit = limit;
    }

    @Override
    public Iterator<E> apply(Iterator<E> rs, ExecutorService pool) {
        List<E> ret = new LinkedList<E>();
        if (skip < 0 && limit < 0) {
            return rs;
        }
        int idx = 0;
        int cnt = 0;
        while (rs.hasNext()) {
            E item = rs.next();
            if (idx >= skip || skip < 0) {
                if (cnt < limit || limit < 0) {
                    ret.add(item);
                }
                cnt++;
            }
            idx++;
        }
        return ret.iterator();
    }
}

package i2f.core.streaming.base.sink;

import i2f.core.streaming.AbsStreaming;

import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/11/22 10:08
 * @desc
 */
public abstract class AbsSinkStreaming<R, M, E> extends AbsStreaming<M, E> {
    @Override
    public Iterator<M> apply(Iterator<E> iterator) {
        return null;
    }

    protected Iterator result() {
        AbsStreaming curr = this;
        while (curr.prev != null) {
            curr = curr.prev;
        }
        AbsStreaming head = curr;
        head.create();
        Iterator after = head.apply(null);
        curr = head.next;
        while (curr.next != null) {
            curr.create();
            after = curr.apply(after);
            curr = curr.next;
        }
        return after;
    }

    public R sink() {
        Iterator rs = result();
        this.create();
        R ret = sink((Iterator<M>) rs);
        AbsStreaming curr = this;
        while (curr != null) {
            curr.destroy();
            curr = curr.prev;
        }
        return ret;
    }

    protected abstract R sink(Iterator<M> iterator);
}

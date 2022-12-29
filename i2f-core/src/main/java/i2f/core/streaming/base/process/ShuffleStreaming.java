package i2f.core.streaming.base.process;

import i2f.core.iterator.impl.LazyIterator;
import i2f.core.streaming.AbsStreaming;

import java.security.SecureRandom;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/11/22 9:47
 * @desc
 */
public class ShuffleStreaming<E> extends AbsStreaming<E, E> {

    public ShuffleStreaming() {

    }

    @Override
    public Iterator<E> apply(Iterator<E> iterator, ExecutorService pool) {
        return new LazyIterator<>(() -> {
            SecureRandom random = new SecureRandom();
            List<E> ret = new LinkedList<E>();
            int size = 0;
            while (iterator.hasNext()) {
                E item = iterator.next();
                ret.add(item);
                size++;
            }
            for (int i = size - 1; i > 1; i--) {
                int bi = random.nextInt(i);
                int ei = random.nextInt(i);
                if (bi != ei) {
                    E tmp = ret.get(bi);
                    ret.set(bi, ret.get(ei));
                    ret.set(ei, tmp);
                }
            }

            return ret.iterator();
        });
    }
}

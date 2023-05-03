package i2f.core.streaming.keyed.process;

import i2f.core.streaming.iterator.LazyIterator;
import i2f.core.streaming.iterator.OptionalDecodeIterator;

import java.util.Iterator;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2023/4/22 22:13
 * @desc
 */
public abstract class KeyedStreamingProcessCollectWrapper<K, IN, OUT> extends KeyedStreamingProcessWrapper<K, IN, OUT> {
    @Override
    public Iterator<OUT> process(K key, Iterator<IN> iterator) {
        LazyIterator<OUT> ret = new LazyIterator<>(() -> {
//            SyncQueueIterator<OUT> iter = new SyncQueueIterator<OUT>();
//            Consumer<OUT> consumer=new Consumer<OUT>() {
//                @Override
//                public void accept(OUT out) {
//                    iter.put(out);
//                }
//            };
//            getContext().getPool().submit(()->{
//                collect(key,iterator,consumer);
//                iter.finish();
//            });
//            return iter;

            Queue<Optional<OUT>> list = new ConcurrentLinkedQueue<>();
            Consumer<OUT> consumer = new Consumer<OUT>() {
                @Override
                public void accept(OUT out) {
                    list.add(Optional.ofNullable(out));
                }
            };
            collect(key, iterator, consumer);
            return new OptionalDecodeIterator<>(list.iterator());
        });

        return ret;
    }

    public abstract void collect(K key, Iterator<IN> iterator, Consumer<OUT> consumer);
}

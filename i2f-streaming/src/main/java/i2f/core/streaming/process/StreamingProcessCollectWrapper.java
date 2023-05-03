package i2f.core.streaming.process;

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
public abstract class StreamingProcessCollectWrapper<IN, OUT> extends StreamingProcessWrapper<IN, OUT> {
    @Override
    public Iterator<OUT> process(Iterator<IN> iterator) {
        LazyIterator<OUT> ret = new LazyIterator<>(() -> {
//            SyncQueueIterator<OUT> iter = new SyncQueueIterator<OUT>();
//            Consumer<OUT> consumer=new Consumer<OUT>() {
//                @Override
//                public void accept(OUT out) {
//                    iter.put(out);
//                }
//            };
//            getContext().getPool().submit(()->{
//                collect(iterator,consumer);
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
            collect(iterator, consumer);
            return new OptionalDecodeIterator<>(list.iterator());
        });

        return ret;
    }

    public abstract void collect(Iterator<IN> iterator, Consumer<OUT> consumer);
}

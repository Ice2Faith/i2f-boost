package i2f.core.streaming.keyed.process;

import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2023/4/22 22:06
 * @desc
 */
public interface KeyedStreamingProcessor<K, IN, OUT> {
    Iterator<OUT> process(K key, Iterator<IN> iterator);
}

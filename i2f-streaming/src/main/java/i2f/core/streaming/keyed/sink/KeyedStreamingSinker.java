package i2f.core.streaming.keyed.sink;

import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2023/4/22 22:50
 * @desc
 */
public interface KeyedStreamingSinker<K, IN, OUT> {
    OUT sink(K key, Iterator<IN> iterator);
}

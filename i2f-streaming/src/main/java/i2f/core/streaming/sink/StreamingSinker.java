package i2f.core.streaming.sink;

import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2023/4/22 22:50
 * @desc
 */
public interface StreamingSinker<IN, OUT> {
    OUT sink(Iterator<IN> iterator);
}
